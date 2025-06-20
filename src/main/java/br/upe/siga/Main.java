package br.upe.siga;

import br.upe.siga.management.MMU;
import br.upe.siga.management.PageFault;
import br.upe.siga.management.PageTable;
import br.upe.siga.memory.Disk;
import br.upe.siga.memory.PhysicalMemory;
import br.upe.siga.process.Process;
import br.upe.siga.swap.AssistantClock;
import br.upe.siga.swap.SwapAlgorithm;

public class Main {
    private static final String[] process1 = {
            "0-W-10", "1-W-11", "2-W-12", "3-W-13", "8-W-18",
            "4-W-14", "5-W-15", "6-W-16",
            "12-W-22", "13-W-23", "14-W-24", "15-W-25",
            "0-R", "5-R",
            "20-W-108","10-R", "15-R",
            "20-W-100", "21-W-101"
    };

    private static final String[] process2 = {"7-W-17",
            "8-W-18", "9-W-19", "10-W-20", "11-W-21",
            "3-R", "7-R", "11-R",
            "1-R", "6-R", "12-R", "14-R",
            "22-W-102", "23-W-103", "24-W-104",
            "0-R", "20-R", "24-R", "1-R", "5-W-4", "2-R", "2-W-6"};

    public static void main(String[] args) {
        PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
        Disk disk = Disk.getInstance();
        PageTable pageTable = PageTable.getInstance();
        PageFault pageFault = new PageFault();
        SwapAlgorithm swapAlgorithm = SwapAlgorithm.getInstance();
        new AssistantClock(swapAlgorithm).assistantClock();

        MMU mmu = new MMU(physicalMemory, disk, pageTable, swapAlgorithm, pageFault);

        Process process = new Process();
        process.thread(process1, mmu);
        process.thread(process2, mmu);
    }

}
