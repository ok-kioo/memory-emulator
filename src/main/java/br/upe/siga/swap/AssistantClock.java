package br.upe.siga.swap;

import br.upe.siga.memory.Page;

import java.util.Iterator;
import java.util.LinkedList;

public class AssistantClock {
    private final SwapAlgorithm swapAlgorithm;

    public AssistantClock(SwapAlgorithm swapAlgorithm) {
        this.swapAlgorithm = swapAlgorithm;
    }

    public void assistantClock() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (swapAlgorithm.getSecondChanceList()) {
                    for (Page page : swapAlgorithm.getSecondChanceList()) {
                        if (page.getReferenceBit()) {
                            page.setReferenceBit(false);
                        }
                    }
                    System.out.println("[Clock] Reference bits reset.");
                }

            }
        }).start();
    }

}
