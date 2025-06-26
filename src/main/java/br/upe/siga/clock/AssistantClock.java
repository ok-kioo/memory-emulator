package br.upe.siga.clock;

public class AssistantClock {
    private ClockObserver observer;

    public boolean isProcessRunning() {
        return isProcessRunning;
    }

    public void setProcessRunning(boolean processRunning) {
        isProcessRunning = processRunning;
    }

    private boolean isProcessRunning;

    public AssistantClock(ClockObserver observer) {
        this.observer = observer;
    }

    public void clock() {
        new Thread(() -> {
            while (this.isProcessRunning) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                notifyObserver();
                System.out.println("[Clock] Clock trigger");
            }
        }).start();
    }

    private void notifyObserver() {
        observer.listenClock();
    }

}
