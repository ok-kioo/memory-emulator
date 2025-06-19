package br.upe.siga.memory;

import java.util.ArrayList;
import java.util.List;

public class Disk implements  Memory {
    private static final Disk instance = new Disk();
    public Integer[] memoryArray = new Integer[MAX_MEMORY_SLOTS];

    private Disk() {
    }

    public static Disk getInstance() {
        return instance;
    }

    @Override
    public Integer[] getMemoryArray() {
        return memoryArray;
    }

    @Override
    public boolean isFull() {
        for (Integer value : this.memoryArray){
            if (value == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Integer> getFreeFrameIndex() {
        List<Integer> freeSlots = new ArrayList<>();
        for (int i = 0; i < memoryArray.length; i++) {
            if (memoryArray[i] == null) {
                freeSlots.add(i);
            }
        }
        return freeSlots;
    }

}

