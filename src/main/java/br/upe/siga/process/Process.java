package br.upe.siga.process;

import br.upe.siga.management.MMU;

// Classe que representa um processo no sistema
public class Process {
    public Thread thread(String[] process, MMU mmu) {
        Thread processThread = new Thread(() -> {
            for (String commands : process) {
                String[] instructions = commands.split("-");

                int memoryAddress = Integer.parseInt(instructions[0]);
                String operation = instructions[1];
                int dataValue = instructions.length > 2 ? Integer.parseInt(instructions[2]) : -1;

                Instruction instruction = new Instruction(memoryAddress, operation, dataValue, mmu);

                try{
                    switch (operation) {
                        case "R":
                            System.out.println("Reading from memory address: " + memoryAddress);
                            int value = instruction.read();
                            System.out.println("Value read: " + value);
                            break;

                        case "W":
                            synchronized (mmu){
                                System.out.println("Writing to memory address: " + memoryAddress + " with value: " + dataValue);
                                instruction.write();
                            }
                            System.out.println("Value written successfully");
                            break;

                        default:
                            System.out.println("Unknown operation: " + operation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        processThread.start();
        return processThread;
    }
}