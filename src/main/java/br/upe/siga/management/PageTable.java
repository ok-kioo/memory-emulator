package br.upe.siga.management;

import br.upe.siga.memory.Page;
import com.sun.source.tree.SynchronizedTree;

import java.util.HashMap;
import java.util.Map;

public class PageTable {
    private static final int SIZE_PAGE_TABLE = 32;
    private int sizeCount = 0;
    private static final PageTable instance = new PageTable();
    private Map<Integer, Page> virtualHash = new HashMap<Integer, Page>();

    private PageTable() { }

    public static PageTable getInstance() {
        return instance;
    }

    public synchronized Page verifyPage(int virtualAddress) {
        return virtualHash.getOrDefault(virtualAddress, null);
    }

    public synchronized void setPageTable(int virtualAddress, Page page) {
        if(this.sizeCount < SIZE_PAGE_TABLE) {
            this.sizeCount++;
            virtualHash.put(virtualAddress, page);
        } else {
            throw new IllegalStateException("Page table is full, cannot add more pages.");
        }
    }

    public int getSizeCount() {
        return sizeCount;
    }
}
