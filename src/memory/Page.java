package src.memory;

public class Page {
    private boolean referenceBit;
    private boolean presentBit;
    private Integer frameNumber;

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
