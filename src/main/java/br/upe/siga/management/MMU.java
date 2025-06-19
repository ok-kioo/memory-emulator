package br.upe.siga.management;

import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;

public class MMU {
    private final PageTable pageTable;
    private final PhysicalMemory physicalMemory;
    private final Disk disk;
    private final SwapAlgorithm swapAlgorithm;
    private final PageFault pageFault;

    public MMU(PhysicalMemory physicalMemory, Disk disk, PageTable pageTable, SwapAlgorithm swapAlgorithm, PageFault pageFault) {
        this.physicalMemory = physicalMemory;
        this.disk = disk;
        this.pageTable = pageTable;
        this.swapAlgorithm = swapAlgorithm;
        this.pageFault = pageFault;
    }

    public int readPage(int virtualAddress) {
        Page page = pageTable.verifyPage(virtualAddress);
        if (page == null) {
            throw new IllegalArgumentException("Page not found for reading in memory for address: " + virtualAddress);
        } else {
            if (!page.getPresentBit()) {
                pageFault.swapPage(page);
            }
            page.setReferenceBit(true);
            return physicalMemory.getMemoryArray()[page.getFrameNumber()];
        }
    }

    public void writePage(int virtualAddress, int newValue) {
        Page page = pageTable.verifyPage(virtualAddress);

        if (page == null) {
            if(disk.isFull()){
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
