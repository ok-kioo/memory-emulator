package br.upe.siga.management;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Memory;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;


//Classe de Gerenciamento de Falta de Página
public class PageFault {
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageTable pageTable = PageTable.getInstance();
    private final Disk disk = Disk.getInstance();
    private final PhysicalMemory physicalMemory = PhysicalMemory.getInstance();

    // Método para trocar uma página
    public synchronized void swapPage(Page page) {
        System.out.println("PageFault initialized");

        Page oldPage = swapAlgorithm.selectUnusedPage(); 

        if(oldPage == null) { // Se não houver, significa que a memória física não está cheia
            int freeAddress = physicalMemory.getFreeFrameIndex().getFirst();
            physicalMemory.getMemoryArray()[freeAddress] = disk.getMemoryArray()[page.getFrameNumber()];
            disk.releaseMemory(page.getFrameNumber());

            page.setPresentBit(true);
            page.setFrameNumber(freeAddress);

            swapAlgorithm.addPage(page);

            System.out.printf("Page swapped: now in frame %d\n",
                    page.getFrameNumber());

        } else {
            int freePhysicalFrame;

            
            if (oldPage.getPresentBit()) { // Se estiver na memória física, troca com a página do disco
                freePhysicalFrame = oldPage.getFrameNumber();

                Integer tempValue = physicalMemory.getMemoryArray()[freePhysicalFrame];
                physicalMemory.getMemoryArray()[freePhysicalFrame] = disk.getMemoryArray()[page.getFrameNumber()];
                disk.getMemoryArray()[page.getFrameNumber()] = tempValue;
            } else { // Se estiver no disco, pega um novo espaço na memória física
                freePhysicalFrame = physicalMemory.getFreeFrameIndex().getFirst();

                physicalMemory.getMemoryArray()[freePhysicalFrame] = disk.getMemoryArray()[page.getFrameNumber()];
                disk.releaseMemory(page.getFrameNumber());
            }

            oldPage.setPresentBit(false);
            oldPage.setFrameNumber(page.getFrameNumber());

            page.setPresentBit(true);
            page.setReferenceBit(true);
            page.setFrameNumber(freePhysicalFrame);

            if (!swapAlgorithm.getSecondChanceList().contains(page)) { // Previne a duplicação de páginas na lista de segunda chance
                swapAlgorithm.addPage(page);
            }

            System.out.println("Page value: " + physicalMemory.getMemoryArray()[page.getFrameNumber()]);

            System.out.printf("Page swapped: old frame %d (→ disk), now in frame %d\n",
                    page.getFrameNumber(), oldPage.getFrameNumber());
        }

    }


    // Método para criar uma nova página na memória física ou no disco
    public Page createPage(int freeAddress, int virtualAddress, Integer value, Memory memory) {
        System.out.println("PageFault initialized");
        Page newPage = new Page(true, true, freeAddress);
        pageTable.setPageTable(virtualAddress, newPage);
        memory.getMemoryArray()[freeAddress] = value;

        System.out.println("Page created: " + virtualAddress + " at frame " + freeAddress + " " + memory);

        return newPage;
    }
}