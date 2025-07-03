package br.upe.siga;

import br.upe.siga.clock.ClockObserver;
import br.upe.siga.clock.Sweeper;
import br.upe.siga.management.MMU;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.process.InstructionFactory;
import br.upe.siga.process.Process;
import br.upe.siga.clock.AssistantClock;

public class Main {

    //inicializa o sistema, cria os processos e inicia a execução
    public static void main(String[] args) {

        //Inicializa as Instruções dos Processos
        InstructionFactory instructionFactoryProcess1 = new InstructionFactory(32);
        InstructionFactory instructionFactoryProcess2 = new InstructionFactory(32);

        //Cria as Memórias Físicas e Disco
        PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
        Disk disk = Disk.getInstance();
        ClockObserver sweeper = new Sweeper(); //Limpador de Disco

        //Inicializa o Clock
        AssistantClock assistantClock = new AssistantClock(sweeper);
        assistantClock.setProcessRunning(true);
        assistantClock.clock();
        
        MMU mmu = new MMU(physicalMemory, disk);

        //Cria as Threads dos Processos
        Process process = new Process();
        Thread threadProcess1 = process.thread(instructionFactoryProcess1.getNewInstruction(), mmu);
        Thread threadProcess2 = process.thread(instructionFactoryProcess2.getNewInstruction(), mmu);

        try {
            //Aguarda o término das Threads
            threadProcess1.join();
            threadProcess2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Finaliza o clock
        assistantClock.setProcessRunning(false);
    }

}
