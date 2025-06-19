package br.upe.siga.process;

import br.upe.siga.management.MMU;

public class Instruction {
    private int memoryAddress;
    private String operation;
    private int dataValue;
    private MMU mmu;

    public Instruction(int memoryAddress, String operation, int dataValue, MMU mmu) {
        this.memoryAddress = memoryAddress;
        this.operation = operation;
        this.dataValue = dataValue;
        this.mmu = mmu;
    }

    public int read(){
        int value = this.mmu.readPage(this.memoryAddress);
        return value;
    }

    public void write() {
        this.mmu.writePage(this.memoryAddress, this.dataValue);

    }
}
