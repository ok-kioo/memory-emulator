package br.upe.siga;

import br.upe.siga.management.MMU;
import br.upe.siga.management.PageFault;
import br.upe.siga.management.PageTable;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.process.Process;
import br.upe.siga.swap.SwapAlgorithm;

public class Main {
    private static final String[] process1 = {"4-R", "5-R", "0-R", "4-W-2"};
    private static final String[] process2 = {"1-R", "5-W-4", "2-R", "2-W-6"};

    public static void main(String[] args) {
        PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
        Disk disk = Disk.getInstance();
        PageTable pageTable = PageTable.getInstance();
        PageFault pageFault = new PageFault();
        SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();

        MMU mmu = new MMU(physicalMemory, disk, pageTable, swapAlgorithm, pageFault);

        Process process = new Process();
        process.thread(process1, mmu);
        process.thread(process2, mmu);
    }

}
