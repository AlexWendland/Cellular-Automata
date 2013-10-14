public class GOLabs extends CellularAutomata {
	public int getNeighbour (int x, int y, int ind){
		return super.getMooreNeighbour(x, y, ind);
	}
	
	public GOLabs (int[][] grid){
		super(grid);
	}
	
	public int rules (int x, int y){
		int neighbourCount = 0;
		for(int i = 0; i<=7; i++){
			neighbourCount += getNeighbour(x, y, i);
		}
		
		if(currentGrid[x][y] == 1){
			if(neighbourCount < 2 || neighbourCount > 3){
				return 0;
			}else {
				return 1;
			}
		}
	
		else{
			if(neighbourCount == 3){
				return 1;
			}
		}
		return 0;
	}
	
}
