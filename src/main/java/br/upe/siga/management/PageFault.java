package br.upe.siga.management;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Memory;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.swap.SwapAlgorithm;

import java.util.Arrays;

public class PageFault {
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageTable pageTable = PageTable.getInstance();
    private final Disk disk = Disk.getInstance();
    private final PhysicalMemory physicalMemory = PhysicalMemory.getInstance();

    public synchronized void swapPage(Page page) {
        System.out.println("PageFault initialized");

        Page oldPage = swapAlgorithm.selectUnusedPage();

        if(oldPage == null) {
            System.out.println(Arrays.toString(physicalMemory.getMemoryArray()));

            swapAlgorithm.getSecondChanceList().addLast(page);
            int freeAddress = physicalMemory.getFreeFrameIndex().getFirst();
            physicalMemory.getMemoryArray()[freeAddress] = disk.getMemoryArray()[page.getFrameNumber()];
            disk.releaseMemory(page.getFrameNumber());

            page.setPresentBit(true);
            page.setFrameNumber(freeAddress);

            swapAlgorithm.addPage(page);

            System.out.printf("Page swapped: now in frame %d\n",
                    page.getFrameNumber());

        } else {
            Integer tempValue = physicalMemory.getMemoryArray()[oldPage.getFrameNumber()];
            physicalMemory.getMemoryArray()[oldPage.getFrameNumber()] = disk.getMemoryArray()[page.getFrameNumber()];
            disk.getMemoryArray()[page.getFrameNumber()] = tempValue;

            int tempFrameNumber = oldPage.getFrameNumber();
            oldPage.setFrameNumber(page.getFrameNumber());
            page.setFrameNumber(tempFrameNumber);

            page.setPresentBit(true);
            page.setReferenceBit(true);

            if (page.getPresentBit() && !swapAlgorithm.getSecondChanceList().contains(page)) {
                swapAlgorithm.getSecondChanceList().addLast(page);
            }

            System.out.println("Page value: " + physicalMemory.getMemoryArray()[page.getFrameNumber()]);

            System.out.printf("Page swapped: old frame %d (→ disk), now in frame %d\n",
                    page.getFrameNumber(), oldPage.getFrameNumber());
        }

    }


    public Page createPage(int freeAddress, int virtualAddress, Integer value, Memory memory) {
        System.out.println("PageFault initialized");
        Page newPage = new Page(true, true, freeAddress);
        pageTable.setPageTable(virtualAddress, newPage);
        memory.getMemoryArray()[freeAddress] = value;

        System.out.println("Page created: " + virtualAddress + " at frame " + freeAddress + " " + memory);

        return newPage;
    }
}
