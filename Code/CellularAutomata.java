import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

public abstract class CellularAutomata {

	public int[][] currentGrid;
	public int width;
	public int height;
	
	
	public int getCell (int x, int y){
		return currentGrid[x][y];
	}
	
	abstract public int getNeighbour (int x, int y, int ind);


	abstract public int rules (int xPos, int yPos);
	
	
	/*public int[][] updateGrid (){
		//int[][] newGrid = new int[width][height];
	}*/
	
	
	public CellularAutomata (int[][] grid){
		currentGrid = grid;
		width = grid.length;
		height = grid[0].length;
		
	}
	
	
	public int[][] update (){
		int [][] newGrid = new int[width][height];
		
		for(int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				newGrid[x][y] = rules(x, y);
			}
		}
		currentGrid = newGrid;
		return newGrid;
	}
	
	public int[][] getGrid (){
		return currentGrid;
	}
	
	public int getMooreNeighbour (int x, int y, int ind){
		if(x >= width-1 || x <= 0 || y >= height-1 || y <= 0){
			return 0;
		}
		
		switch (ind){
		
			case 0:
				return currentGrid[x-1][y-1];
			
			case 1:
				return currentGrid[x][y-1];
			
			case 2:
				return currentGrid[x+1][y-1];
			
			case 3:
				return currentGrid[x-1][y];
			
			case 4:
				return currentGrid[x+1][y];
			
			case 5:
				return currentGrid[x-1][y+1];
			
			case 6:
				return currentGrid[x][y+1];
			
			case 7:
				return currentGrid[x+1][y+1];
			
			
			default:
			System.out.println("out of bounds shit");
			break;
		}
		
		
		
		/*try {
			if(ind <= 2){
				return currentGrid[(x-1+ind)%(width-1)][(y-1)%(height-1)];
			}
			if(ind == 3){
				return currentGrid[(x-1)%(width-1)][y%(height-1)];
			}
			if(ind == 4){
				return currentGrid[(x+1)%(width-1)][y%(height-1)];
			}
			if(ind <= 7){
				return currentGrid[(x-6+ind)%(width-1)][(y+1)%(height-1)];
			}
		} catch (Exception e){
			System.out.println("butts");
			System.out.println(x+"  "+y+"  "+ind);
			
			//System.out.println(e.getMessage());
			e.printStackTrace();
			
			return 0;
		}*/
		System.out.println("ERR: Get neighbour function called out of bounds index");
		return -1;
	}
}




/*



TO DO:




*/