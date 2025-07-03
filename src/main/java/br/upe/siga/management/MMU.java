package br.upe.siga.management;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;


//Classe de Gerenciamento de Memória
public class MMU {
    private final PhysicalMemory physicalMemory;
    private final Disk disk;
    private final PageTable pageTable = PageTable.getInstance();
    private final PageFault pageFault = new PageFault();
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();


    public MMU(PhysicalMemory physicalMemory, Disk disk) {
        this.physicalMemory = physicalMemory;
        this.disk = disk;
    }

    // Método para ler uma página da memória física
    public int readPage(int virtualAddress) {
        Page page = pageTable.verifyPage(virtualAddress);
        if (page == null) {
            throw new IllegalArgumentException("Page not found for reading in memory for address: " + virtualAddress); //erro caso a página não esteja na tabela de páginas
        } else {
            if (!page.getPresentBit()) {
                System.out.println("Page not present in physical memory, swapping page...");
                pageFault.swapPage(page); // chama o método de troca de página caso a página não esteja presente na memória física
            }
            page.setReferenceBit(true); // marca a página como referenciada

            return physicalMemory.getMemoryArray()[page.getFrameNumber()];
        }
    }

    // Método para escrever uma nova página na memória física
    public void writePage(int virtualAddress, int newValue) {
        Page page = pageTable.verifyPage(virtualAddress);

        if (page == null) {
            if(pageTable.getSizeCount() == 32){
                throw new IllegalArgumentException("All memory address fully"); // erro caso a tabela de páginas esteja cheia
            }

            if (physicalMemory.isFull()) {
                Page newPage = pageFault.createPage(disk.getFreeFrameIndex().getFirst(), virtualAddress, newValue, disk); // cria uma nova página no disco
                pageFault.swapPage(newPage);
            } else {
                Page newPage = pageFault.createPage(physicalMemory.getFreeFrameIndex().getFirst(), virtualAddress, newValue, physicalMemory); // cria uma nova página na memória física
                swapAlgorithm.addPage(newPage);
            }
        } else{
            if (!page.getPresentBit()) {
                pageFault.swapPage(page); // chama o método de troca de página caso a página não esteja presente na memória física
            }

            physicalMemory.getMemoryArray()[page.getFrameNumber()] = newValue;
            page.setReferenceBit(true);
        }
    }

}
