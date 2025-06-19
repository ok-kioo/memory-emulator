package br.upe.siga.swap;

import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;

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
            if(index.getReferenceBit()) {
                index.setReferenceBit(false);
                Page temp = secondChanceList.removeFirst();
                secondChanceList.addLast(temp);

            } else {
                index.setPresentBit(false);
                int indexFrameNumber = index.getFrameNumber();
                secondChanceList.removeFirst();

                secondChanceList.addLast(page);
                int pageFrameNumber = page.getFrameNumber();
                page.setPresentBit(true);

                page.setFrameNumber(indexFrameNumber);
                index.setFrameNumber(pageFrameNumber);

                Integer temp = physicalMemory.memoryArray[page.getFrameNumber()];
                physicalMemory.memoryArray[page.getFrameNumber()] = physicalMemory.memoryArray[index.getFrameNumber()];
                disk.memoryArray[index.getFrameNumber()] = temp;
            }
        }
    }

    public void addPage(Page page){
        secondChanceList.addLast(page);
    }
}
