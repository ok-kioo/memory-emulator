package br.upe.siga.memory;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMemory implements  Memory{
    private static final PhysicalMemory instance = new PhysicalMemory();
    private Integer[] memoryArray = new Integer[MAX_MEMORY_SLOTS];

    private PhysicalMemory() {
    }

    public static PhysicalMemory getInstance() {
        return instance;
    }

    @Override
    public Integer[] getMemoryArray() {
        return memoryArray;
    }

    @Override
    public synchronized boolean isFull() {
        for (Integer value : this.memoryArray){
            if (value == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public synchronized List<Integer> getFreeFrameIndex() {
        List<Integer> freeSlot = new ArrayList<>();
        for (int i = 0; i < memoryArray.length; i++) {
            if (memoryArray[i] == null) {
                allocateMemory(i);
                freeSlot.add(i);
                break;
            }
        }
        return freeSlot;
    }

    private synchronized void allocateMemory(int index){
        this.memoryArray[index] = -1;
    }

    public synchronized void releaseMemory(int index){
        this.memoryArray[index] = null;
    }
}

