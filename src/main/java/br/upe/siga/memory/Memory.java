package br.upe.siga.memory;

import java.util.List;

public interface Memory {
    int MAX_MEMORY_SLOTS = 16;

    Integer[] getMemoryArray();
    boolean isFull();
    List<Integer> getFreeFrameIndex();
}
