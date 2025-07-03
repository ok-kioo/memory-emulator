package br.upe.siga.process;

import br.upe.siga.management.MMU;

// Classe que representa uma instrução a ser executada por um processo
public class Instruction {
    private int memoryAddress;
    private String operation;
    private int dataValue;
    private MMU mmu;

    public Instruction(int memoryAddress, String operation, int dataValue, MMU mmu) {
        this.memoryAddress = memoryAddress; // Endereço de memória onde a operação será realizada
        this.operation = operation; // Tipo de operação (leitura ou escrita)
        this.dataValue = dataValue; // Valor a ser escrito na memória (se aplicável)
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
