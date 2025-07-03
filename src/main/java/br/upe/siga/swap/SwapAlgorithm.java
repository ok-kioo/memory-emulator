package br.upe.siga.swap;

import br.upe.siga.memory.Disk;
import br.upe.siga.memory.Page;
import br.upe.siga.memory.PhysicalMemory;

import java.util.LinkedList;

// Classe de Algoritmo de Troca de Páginas
public class SwapAlgorithm {
    private static final SwapAlgorithm instance = new SwapAlgorithm();
    private final LinkedList<Page> secondChanceList = new LinkedList<>();

    private final PhysicalMemory physicalMemory = PhysicalMemory.getInstance();
    private final Disk disk = Disk.getInstance();

    private SwapAlgorithm() {
    }

    public static SwapAlgorithm getInstance() {
        return instance;
    }

    // Método para selecionar uma página não utilizada
    public synchronized Page selectUnusedPage() {
        synchronized (secondChanceList) {
            int size = secondChanceList.size();
            Page returnPage = null;

            if (!physicalMemory.isFull()) { 
                return returnPage;
            } else {
                for (int i = 0; i < size; i++) {
                    Page page = secondChanceList.peekFirst();
                    if (page.getReferenceBit()) { // Se a página foi referenciada, limpa o bit de referência e move para o final da lista
                        page.setReferenceBit(false);
                        secondChanceList.removeFirst();
                        secondChanceList.addLast(page);
                    } else {
                        //swap out
                        if(!disk.isFull()) {
                            int freeAddress = disk.getFreeFrameIndex().getFirst();
                            disk.getMemoryArray()[freeAddress] = physicalMemory.getMemoryArray()[page.getFrameNumber()];
                            physicalMemory.releaseMemory(page.getFrameNumber());

                            secondChanceList.removeFirst(); 
                            page.setFrameNumber(freeAddress);
                            page.setPresentBit(false);
                            returnPage = page;
                            // continua o loop para verificar se há mais páginas não referenciadas
                        } else { // swap completo
                            returnPage = secondChanceList.removeFirst();
                            break; 
                        }
                    }
                }

                if (returnPage == null) {
                    throw new IllegalArgumentException("Not found pages for swap");
                }

                return returnPage;
            }
        }
    }

    // Método para adicionar uma página à lista de segunda chance
    public synchronized void addPage(Page page) {
        secondChanceList.addLast(page);
    }

    // Método para obter a lista de segunda chance
    public LinkedList<Page> getSecondChanceList() {
        return this.secondChanceList;
    }
}