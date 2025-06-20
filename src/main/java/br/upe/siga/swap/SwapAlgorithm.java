package br.upe.siga.swap;

import br.upe.siga.memory.Page;

import java.util.LinkedList;

public class SwapAlgorithm {
    private static final SwapAlgorithm instance = new SwapAlgorithm();
    private final LinkedList<Page> secondChanceList = new LinkedList<>();

    private SwapAlgorithm() { }

    public static SwapAlgorithm getInstance() {
        return instance;
    }

    public Page selectUnusedPage() {
        synchronized (secondChanceList) {
            int size = secondChanceList.size();
            for (int i = 0; i < size; i++) {
                Page candidate = secondChanceList.peekFirst();
                assert candidate != null;
                if (candidate.getReferenceBit()) {
                    candidate.setReferenceBit(false);
                    secondChanceList.removeFirst();
                    secondChanceList.addLast(candidate);
                } else {
                    return secondChanceList.removeFirst();
                }
            }
        }
        throw new IllegalArgumentException("Not found pages for swap");
    }


    public void addPage(Page page){
        secondChanceList.addLast(page);
    }

    public LinkedList<Page> getSecondChanceList(){
        return this.secondChanceList;
    }
}
