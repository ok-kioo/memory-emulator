package br.upe.siga.management;
import br.upe.siga.memory.Memory;
import br.upe.siga.memory.Page;
import br.upe.siga.swap.SwapAlgorithm;


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
