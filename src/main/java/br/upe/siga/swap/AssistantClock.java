package br.upe.siga.swap;

public class AssistantClock {
    private final SwapAlgorithm swapAlgorithm;

    public AssistantClock(SwapAlgorithm swapAlgorithm) {
        this.swapAlgorithm = swapAlgorithm;
    }

    public void assistantClock() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("[Clock] Clock trigger");

                swapAlgorithm.secondChance();
            }
        }).start();
    }

}
