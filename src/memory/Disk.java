package src.memory;

import java.util.ArrayList;
import java.util.List;

public class Disk {
    private static final int MAX_MEMORY_SLOTS = 16;
    private static final Disk instance = new Disk();
    public Page[] diskMemoryArray = new Page[MAX_MEMORY_SLOTS];

    private Disk() {
    }

    public static Disk getInstance() {
        return instance;
    }

    public boolean isFull() {
        for (Page page : this.diskMemoryArray){
            if (page == null) {
                return true;
            }
        }
        return false;
    }

    public List<Integer> getFreeFrameIndex() {
        List<Integer> freeSlots = new ArrayList<>();
        for (int i = 0; i < diskMemoryArray.length; i++) {
            if (diskMemoryArray[i] == null) {
                freeSlots.add(i);
            }
        }
        return freeSlots;
    }

}

