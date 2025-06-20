package br.upe.siga.management;

import br.upe.siga.memory.Page;
import com.sun.source.tree.SynchronizedTree;

import java.util.HashMap;
import java.util.Map;

public class PageTable {
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
        virtualHash.put(virtualAddress, page);
    }
}
