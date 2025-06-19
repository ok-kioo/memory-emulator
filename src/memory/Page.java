package src.memory;

public class Page {
    public boolean referenceBit;
    public boolean presentBit;
    public int frameNumber;
    public int value;

    public Page(boolean referenceBit, boolean presentBit, int frameNumber, int value) {
        this.referenceBit = referenceBit;
        this.presentBit = presentBit;
        this.frameNumber = frameNumber;
        this.value = value;
    }
}
