package src.management;

import src.memory.Page;

import java.util.HashMap;
import java.util.Map;

public class PageTable {
    private static final PageTable instance = new PageTable();
    private Map<Integer, Page> virtualHash = new HashMap<Integer, Page>();

    private PageTable() { }

    public static PageTable getInstance() {
        return instance;
    }

    public Page verifyPage(int virtualAddress) {
        return virtualHash.getOrDefault(virtualAddress, null);
    }

    public void setPageTable(int virtualAddress, Page page) {
        virtualHash.put(virtualAddress, page);
    }
}
