package br.upe.siga.swap;

import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;

import java.util.LinkedList;

public class SwapAlgorithm {
    private static final SwapAlgorithm instance = new SwapAlgorithm();
    private final LinkedList<Page> secondChanceList = new LinkedList<>();

    private final PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
    private final Disk disk = Disk.getInstance();

    private SwapAlgorithm() { }

    public static SwapAlgorithm getInstance() {
        return instance;
    }

    public synchronized Page selectUnusedPage() {
        synchronized (secondChanceList) {
            int size = secondChanceList.size();
            Page returnPage = null;

            if (!physicalMemory.isFull()) {
                return returnPage;
            } else {

                for (int i = 0; i < size; i++) {
                    Page page = secondChanceList.peekFirst();
                    if (page.getReferenceBit()) {
                        page.setReferenceBit(false);
                        secondChanceList.removeFirst();
                        secondChanceList.addLast(page);
                    } else {
                        int freeAddress = disk.getFreeFrameIndex().getFirst();
                        disk.getMemoryArray()[freeAddress] = physicalMemory.getMemoryArray()[page.getFrameNumber()];
                        physicalMemory.releaseMemory(page.getFrameNumber());

                        page.setFrameNumber(freeAddress);
                        page.setPresentBit(false);

                        returnPage = secondChanceList.removeFirst();
                    }
                }

                if (returnPage == null) {
                    throw new IllegalArgumentException("Not found pages for swap");
                }

                return returnPage;
            }
        }
    }

    public synchronized void addPage(Page page){
        secondChanceList.addLast(page);
    }

    public LinkedList<Page> getSecondChanceList(){
        return this.secondChanceList;
    }
}
