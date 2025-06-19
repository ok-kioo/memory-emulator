package src.management;
import src.memory.Memory;
import src.memory.Page;
import src.swap.SwapAlgorithm;


public class PageFault {
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageTable pageTable = PageTable.getInstance();

    public void swapPage(Page page) {
        swapAlgorithm.swap(page);
    }

    public Page createPage(int freeAddress, int virtualAddress, Integer value, Memory memory) {
        Page newPage = new Page(true, true, freeAddress);
        pageTable.setPageTable(virtualAddress, newPage);
        memory.getMemoryArray()[freeAddress] = value;

        return newPage;
    }
}
