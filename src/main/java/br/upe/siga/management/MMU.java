package br.upe.siga.management;

import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;

public class MMU {
    private final PageTable pageTable = PageTable.getInstance();
    private final PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
    private final Disk disk = Disk.getInstance();
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageFault pageFault = new PageFault();

    public int readPage(int virtualAddress) {
        Page page = pageTable.verifyPage(virtualAddress);
        if (page == null) {
            throw new IllegalArgumentException("Page not found for reading in memory for address: " + virtualAddress);
        } else {
            if (!page.getPresentBit()) {
                pageFault.swapPage(page);
            }
            page.setReferenceBit(true);
            return physicalMemory.memoryArray[page.getFrameNumber()];
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

            physicalMemory.memoryArray[page.getFrameNumber()] = newValue;
            page.setReferenceBit(true);
        }
    }

}
