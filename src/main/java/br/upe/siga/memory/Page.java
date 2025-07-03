package br.upe.siga.memory;

// Classe que representa uma página na memória física ou no disco
public class Page {
    private boolean referenceBit; // Indica se a página foi referenciada
    private boolean presentBit; // Indica se a página está presente na memória física
    private Integer frameNumber; // Número do quadro (frame) onde a página está armazenada

    public Page(boolean referenceBit, boolean presentBit, Integer frameNumber) {
        this.referenceBit = referenceBit;
        this.presentBit = presentBit;
        this.frameNumber = frameNumber;
    }

    public boolean getReferenceBit() {
        return referenceBit;
    }

    public void setReferenceBit(boolean referenceBit) {
        this.referenceBit = referenceBit;
    }

    public boolean getPresentBit() {
        return presentBit;
    }

    public void setPresentBit(boolean presentBit) {
        this.presentBit = presentBit;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }
}
