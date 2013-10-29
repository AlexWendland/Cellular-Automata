public class ElementaryCA extends CellularAutomata {
	
	public int lineNo;
	public int[] binRules;
	
	
	public ElementaryCA (int[][] grid, int ruleNumber){
		super(grid);
		lineNo = 0;
		
		
		binRules = intToBin(ruleNumber);
		/*intToBin(60);
		intToBin(10);
		intToBin(1);
		intToBin(4);
		intToBin(255);*/
		
		
		
	}
	
	public int getNeighbour (int x, int y, int ind){
		if(y == 0 || x+ind >= currentGrid.length || x+ind <= 0){
			return currentGrid[x][y];
		}
		return currentGrid[x+ind][y-1];
	}
		
	public int rules (int x, int y){
		
		int count = 4 * getNeighbour(x, y, -1) + 2 * getNeighbour(x, y, 0) + getNeighbour(x, y, 1);
		
		/*if(count == 1 || count == 2 || count == 3 || count == 5 || count == 6){
			return 1;
		}*/
		
		if(binRules[count] == 1){
			return 1;
		}
		return currentGrid[x][y];
	}
	
	
	public int[][] update (){
		
		while(lineNo < currentGrid[0].length){
		
			for(int i = 0; i < currentGrid.length; i++){
				currentGrid[i][lineNo] = rules(i, lineNo);
			}
		
			lineNo++;
		}
		return currentGrid;
	}
	
	
	public int[] intToBin (int in){
		int[] bin = new int[8];
		int count = 0;
		
		//System.out.println(in+" in binary is....");
		
		for(int i = 0; i<8; i++){
			if(in%2 == 1){
				bin[count] = 1;
		//		System.out.print(1);
				in -= 1;
			}else{
				bin[count] = 0;
		//		System.out.print(0);
			}
			in /= 2;
			count++;
		}
	//	System.out.println("\nHopefully");
		
		return bin;
	}
}
