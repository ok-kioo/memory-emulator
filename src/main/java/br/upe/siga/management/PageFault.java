package br.upe.siga.management;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Memory;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;

public class PageFault {
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageTable pageTable = PageTable.getInstance();
    private Disk disk = Disk.getInstance();
    private PhysicalMemory physicalMemory = PhysicalMemory.getInstance();

    public void swapPage(Page page) {
        System.out.println("PageFault initialized");

        Page oldPage = swapAlgorithm.selectUnusedPage();

        oldPage.setPresentBit(false);
        int oldPageFrameNumber = oldPage.getFrameNumber();
        swapAlgorithm.getSecondChanceList().remove(oldPage);

        swapAlgorithm.getSecondChanceList().addLast(page);
        int pageFrameNumber = page.getFrameNumber();
        page.setPresentBit(true);

        page.setFrameNumber(oldPageFrameNumber);
        oldPage.setFrameNumber(pageFrameNumber);

        Integer temp = physicalMemory.getMemoryArray()[page.getFrameNumber()];
        physicalMemory.getMemoryArray()[page.getFrameNumber()] = physicalMemory.getMemoryArray()[oldPage.getFrameNumber()];
        disk.getMemoryArray()[oldPage.getFrameNumber()] = temp;

        System.out.printf("Page swapped: old frame %d (→ disk), now in frame %d\n",
                page.getFrameNumber(), oldPage.getFrameNumber());
    }


    public Page createPage(int freeAddress, int virtualAddress, Integer value, Memory memory) {
        System.out.println("PageFault initialized");
        Page newPage = new Page(true, true, freeAddress);
        pageTable.setPageTable(virtualAddress, newPage);
        memory.getMemoryArray()[freeAddress] = value;

        System.out.println("Page created: " + virtualAddress);
        return newPage;
    }
}
