package src.management;
import src.memory.Page;
import src.swap.SwapAlgorithm;


public class PageFault {
    private final SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    private final PageTable pageTable = PageTable.getInstance();

    public void swapPage(Page page) {
        swapAlgorithm.swap(page);
    }

    public Page createPage(int freeAddress, int virtualAddress) {
        Page newPage = new Page(true, true, freeAddress, -1);
        pageTable.setPageTable(virtualAddress, newPage);

        return newPage;
    }
}
