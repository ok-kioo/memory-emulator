package src.memory;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMemory {
    private static final int MAX_MEMORY_SLOTS = 16;
    private static final PhysicalMemory instance = new PhysicalMemory();
    public Page[] physicalMemoryArray = new Page[MAX_MEMORY_SLOTS];


    private PhysicalMemory() { }

    public static PhysicalMemory getInstance() {
        return instance;
    }

    public List<Integer> getFreeFrameIndex() {
        List<Integer> freeSlots = new ArrayList<>();
        for (int i = 0; i < physicalMemoryArray.length; i++) {
            if (physicalMemoryArray[i] == null) {
                freeSlots.add(i);
            }
        }
        return freeSlots;
    }
}

