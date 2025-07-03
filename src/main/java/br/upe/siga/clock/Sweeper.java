package br.upe.siga.clock;

import br.upe.siga.memory.Page;
import br.upe.siga.swap.SwapAlgorithm;

import java.util.LinkedList;

// Classe que implementa o algoritmo de limpeza de páginas (Sweeper)
public class Sweeper implements ClockObserver {
    private SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
    @Override
    public void listenClock() {
        LinkedList<Page> secondChanceList = swapAlgorithm.getSecondChanceList();

        // Limpa o bit de referência
        for (int i = 0; i < secondChanceList.size(); i++) {
            Page page = secondChanceList.peekFirst();
            if (page.getReferenceBit()) {
                page.setReferenceBit(false);
                secondChanceList.removeFirst();
                secondChanceList.addLast(page);
            }
        }
    }
}
