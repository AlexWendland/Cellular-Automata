public class BMLTraffic extends CellularAutomata {
	public Boolean redTurn;
	
	public int getNeighbour (int x, int y, int ind){
		return super.getMooreNeighbour(x, y, ind);
	}
	
	public BMLTraffic (int[][] grid){
		super(grid);
		redTurn = true;
	}
	
	public int[][] update (){
		redTurn = !redTurn;
		return super.update();
	}
	
	public int rules (int x, int y){
		
		if(currentGrid[x][y] == 0){
			if(redTurn){ 
				if(getNeighbour(x, y, 3) == 1){
					return 1;
				}
				return 0;
			}else{
				if(getNeighbour(x, y, 1) == 2){
					return 2;
				}
				return 0;
			}
		}
		
		if(redTurn && currentGrid[x][y] == 1){
			if(getNeighbour(x, y, 4) == 0){
				return 0;
			}else{
				return 1;
			}
		}
		
		if(!redTurn && currentGrid[x][y] == 2){
			if(getNeighbour(x, y, 6) == 0){
				return 0;
			}else{
				return 2;
			}
		}
		//System.out.println("????????");
		return currentGrid[x][y];
	}
	
}
