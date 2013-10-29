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
	public void setCell (int x, int y, int val){
		currentGrid[x][y] = val;
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
	public void setGrid (int[][] grid){
		currentGrid = grid;
	}
	
	public int getMooreNeighbour (int x, int y, int ind){
		int xp;
		int yp;
		
		/*if(x >= width-1 || x <= 0 || y >= height-1 || y <= 0){
			return 0;
		}*/
		
		switch (7-ind){
		
			case 0:
				xp = -1;
				yp = -1;
				break;
			
			case 1:
				xp = 0;
				yp = -1;
				break;
			
			case 2:
				xp = 1;
				yp = -1;
				break;
				
			case 3:
				xp = -1;
				yp = 0;
				break;
				
			case 4:
				xp = 1;
				yp = 0;
				break;
				
			case 5:
				xp = -1;
				yp = 1;
				break;
				
			case 6:

				xp = 0;
				yp = 1;
				break;
			
			case 7:
				xp = 1;
				yp = 1;
				break;
		
			default:
			System.out.println("out of bounds shit");
			xp = 0;
			yp = 0;
			break;
			
			/*case 0:
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
			
			*/
		}
		
		int mx = x + xp;
		int my = y + yp;
		
		if(mx >= width){
			mx = xp;
		}
		if(mx < 0){
			mx = width-1 + xp;
		}
		if(my >= height){
			my = yp;
		}
		if(my < 0){
			my = height-1 + yp;
		}
		
		return currentGrid[mx][my];
	
		
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
		}
		System.out.println("ERR: Get neighbour function called out of bounds index");
		return -1;
		*/
	}
}




/*



TO DO:




*/