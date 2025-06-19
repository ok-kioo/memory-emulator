package src.swap;

import src.memory.Disk;
import src.memory.Page;
import src.memory.PhysicalMemory;

import java.util.LinkedList;

public class SwapAlgorithm {
    private static final SwapAlgorithm instance = new SwapAlgorithm();
    private LinkedList<Page> secondChanceList = new LinkedList<>();
    private Disk disk = Disk.getInstance();
    private PhysicalMemory physicalMemory = PhysicalMemory.getInstance();

    private SwapAlgorithm() { }

    public static SwapAlgorithm getInstance() {
        return instance;
    }

    public void swap(Page page) {
        for(Page index : secondChanceList) {
            if(index.referenceBit) {
                index.referenceBit = false;
                Page temp = secondChanceList.removeFirst();
                secondChanceList.addLast(temp);

            } else {
                index.presentBit = false;
                int indexFrameNumber = index.frameNumber;
                secondChanceList.removeFirst();

                secondChanceList.addLast(page);
                int pageFrameNumber = page.frameNumber;
                page.presentBit = true;

                page.frameNumber = indexFrameNumber;
                index.frameNumber = pageFrameNumber;

                physicalMemory.physicalMemoryArray[page.frameNumber] = page;
                disk.diskMemoryArray[index.frameNumber] = index;
            }
        }
    }

    public void addPage(Page page){
        secondChanceList.addLast(page);
    }
}
