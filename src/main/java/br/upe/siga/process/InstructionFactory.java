package br.upe.siga.process;

import java.util.ArrayList;
import java.util.Random;

public class InstructionFactory {
	public static final int ENTER_ACCESS_QTD = 100;
	private int virtualMemorySize = 0;
	private int seed;

	public InstructionFactory(int memorySize) {
		this.virtualMemorySize = memorySize;

		Random r = new Random();
		this.seed = r.nextInt(memorySize);
		System.out.println("Seed: " + seed);

		if(memorySize < 10) {
			throw new IllegalArgumentException("Memory very small - minimum value 10");
		}
		if(memorySize > 40) {
			throw new IllegalArgumentException("Memory too big - minimum value 40");
		}

		if(ENTER_ACCESS_QTD < 50) {
			throw new IllegalArgumentException("Input Size can't be smaller than 50");
		}
	}

	public String[] getNewInstruction() {
		String value = "";
		Random r = new Random(this.seed);
		Random r1 = new Random();

		ArrayList<Integer> reads = new ArrayList<Integer>();
		String[] loop = new String[4];
		int indexLoop = 0;

		StringBuffer sb = new StringBuffer();
		int loop1 = ENTER_ACCESS_QTD/3 * 1;
		int loop2 = ENTER_ACCESS_QTD/3 * 2;
		//System.out.println("Loop1 = "  + loop1 + " - loop2 = " + loop2);

		for (int i = 0; i < ENTER_ACCESS_QTD; i++) {
			if(i == loop1 || i == loop2) {
				//sb.append("|- ");
				for (int j = 0; j < loop.length; j++) {
					sb.append(loop[indexLoop++]);
					indexLoop = indexLoop % loop.length;
				}
				//sb.append(" -|");
				i = i + loop.length-1;
				continue;
			}

			int address = r.nextInt(virtualMemorySize);

			int index = reads.indexOf(address);
			if(index == -1) {
				int aux = r1.nextInt(100);
				sb.append(address + "-W-" + aux + ",");
				loop[indexLoop++] = address + "-W-" + aux + ",";
				indexLoop = indexLoop % loop.length;
				reads.add(address);
			} else {
				boolean accessType = r.nextBoolean();
				if(accessType) {
					sb.append(address + "-R,");
					loop[indexLoop++] = address + "-R,";
					indexLoop = indexLoop % loop.length;
				} else {
					int aux = r1.nextInt(100);
					sb.append(address + "-W-" + aux + ",");
					loop[indexLoop++] = address + "-W-" + aux + ",";
					indexLoop = indexLoop % loop.length;
				}
			}
		}

		value = sb.substring(0, sb.length()-1);
		return value.split(",");
	}

}
