package br.upe.siga.management;

import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;

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

    public int readPage(int virtualAddress) {
        Page page = pageTable.verifyPage(virtualAddress);
        if (page == null) {
            throw new IllegalArgumentException("Page not found for reading in memory for address: " + virtualAddress);
        } else {
            if (!page.getPresentBit()) {
                System.out.println("Page not present in physical memory, swapping page...");
                pageFault.swapPage(page);
            }
            page.setReferenceBit(true);

            return physicalMemory.getMemoryArray()[page.getFrameNumber()];
        }
    }

    public void writePage(int virtualAddress, int newValue) {
        Page page = pageTable.verifyPage(virtualAddress);

        if (page == null) {
            if(pageTable.getSizeCount() == 32){
                throw new IllegalArgumentException("All memory address fully");
            }

            if (physicalMemory.isFull()) {
                Page newPage = pageFault.createPage(disk.getFreeFrameIndex().getFirst(), virtualAddress, newValue, disk);
                pageFault.swapPage(newPage);
            } else {
                Page newPage = pageFault.createPage(physicalMemory.getFreeFrameIndex().getFirst(), virtualAddress, newValue, physicalMemory);
                swapAlgorithm.addPage(newPage);
            }
        } else{
            if (!page.getPresentBit()) {
                pageFault.swapPage(page);
            }

            physicalMemory.getMemoryArray()[page.getFrameNumber()] = newValue;
            page.setReferenceBit(true);
        }
    }

}
