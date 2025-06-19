package src.management;

import src.memory.Disk;
import src.memory.Page;
import src.memory.PhysicalMemory;
import src.swap.SwapAlgorithm;

public class MMU {
    private final PageTable pageTable = PageTable.getInstance();
    private final PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
    private final Disk disk = Disk.getInstance();
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageFault pageFault = new PageFault();

    public int readPage(int virtualAddress) {
        Page page = pageTable.verifyPage(virtualAddress);
        if (page == null) {
            throw new IllegalArgumentException("Page not found for address: " + virtualAddress);
        }

        if (!page.presentBit) {
            pageFault.swapPage(page);
        }

        page.referenceBit = true;
        return page.value;
    }

    public void writePage(int virtualAddress, int newValue) {
        Page page = pageTable.verifyPage(virtualAddress);

        if (page == null) {
            if(disk.isFull()){
                throw new IllegalArgumentException("All memory address fully");
            }

            if (physicalMemory.getFreeFrameIndex().isEmpty()) {
                Page newPage = pageFault.createPage(disk.getFreeFrameIndex().getFirst(), virtualAddress);
                pageFault.swapPage(newPage);
            } else {
                Page newPage = pageFault.createPage(physicalMemory.getFreeFrameIndex().getFirst(), virtualAddress);
                swapAlgorithm.addPage(newPage);
            }
        }

        if (!page.presentBit) {
            pageFault.swapPage(page);
        }

        page.value = newValue;
        page.referenceBit = true;
    }

}
