public class SmoothLifeAlex extends CellularAutomata {

	public double RADIUS_INNER = (20/3);
	public double RADIUS_OUTER = 20;
	public double BLUR_RADIUS_INNER = 2;
	public double BLUR_RADIUS_OUTER = 2;
	public double BIRTH_LOWER = 0.257;
	public double BIRTH_UPPER = 0.336;
	public double DEATH_LOWER = 0.365;
	public double DEATH_UPPER = 0.549;
	public double STEP_DIFFERENCE_1 = 0.028;
	public double STEP_DIFFERENCE_2 = 0.147;
	public double NORMAL_INNER = 1;
	public double NORMAL_OUTER = 1;
	public int SCALE = 1000;
	public double[][] curGrid = new double[0][0];
	public double DELTA_T = 0.25;
	public int TYPE = 1;
  
	public double sigma1a(double a, double b) { return (double)(1/((1+Math.exp(((a-b)*-4)/STEP_DIFFERENCE_1)))); }
	public double sigma1b(double a, double b) { return (double)(1/((1+Math.exp(((a-b)*-4)/STEP_DIFFERENCE_2)))); }
	public double sigma2(double a, double b, double c) { return sigma1a(a,b)*(1-sigma1b(a,c)); }
	public double sigma3(double a, double b, double c) { return b*(1-sigma1a(a,0.5)) + c*sigma1b(a,0.5); }
	public double sigma4(double a, double b) { return sigma2(a,sigma3(BIRTH_LOWER,DEATH_LOWER,b),sigma3(BIRTH_UPPER,DEATH_UPPER,b)); }	
	public double vert(double a, double b) {return (double)(Math.sqrt(a*a + b*b)); }

	public int getNeighbour (int x, int y, int ind){
		return super.getMooreNeighbour(x, y, ind);
	}
	
	public int rules (int x, int y){
		return 1;
	}
	

	public SmoothLifeAlex (int[][] grid, int scale, int type, double dt){
		super(grid);
		TYPE = type;
		SCALE = scale;
		DELTA_T = dt;
		curGrid = new double[grid.length][grid[0].length];
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[0].length; j++)
			{
				curGrid[i][j] = 1;
			}
		}


		double[] temp = getMN(grid.length/2,grid[0].length/2);
		NORMAL_INNER = temp[1];
		NORMAL_OUTER = temp[0];
		
		if((TYPE == 2)||(TYPE == 3))
		{
			for(int i = 0; i < grid.length; i++)
			{
				for(int j = 0; j < grid[0].length; j++)
				{
					if(scale > 2)
						curGrid[i][j] = ((double)grid[i][j])/scale;
					else
						curGrid[i][j] = (double)grid[i][j];
				}

			}
		}
		else
		{
			for(int i = 0; i < grid.length; i++)
			{
				for(int j = 0; j < grid[0].length; j++)
				{
					if ((i > (grid[0].length/4))&&(i < (3*grid[0].length/4))&&(j > (grid.length/4))&&(j < (3*grid.length/4))) 
						if(scale > 2)
							curGrid[i][j] = ((double)grid[i][j])/scale;
						else
							curGrid[i][j] = (double)grid[i][j];
					else
					{
						curGrid[i][j] = 0;
						currentGrid[i][j] = 0;
					}
				}
			}
		}

	}

	public void toIntGrid()
	{

		if (SCALE > 2)
		{
	
			for(int i = 0; i < curGrid.length; i++)
			{

				for(int j = 0; j < curGrid[0].length; j++)
				{
					currentGrid[i][j] = (int)(curGrid[i][j]*SCALE);
				}
			}
		} else
		{

			for(int i = 0; i < curGrid.length; i++)
			{

				for(int j = 0; j < curGrid[0].length; j++)
				{
					if((currentGrid[i][j] == 0)&&(BIRTH_LOWER < curGrid[i][j])&&(BIRTH_UPPER > curGrid[i][j]))
					{
						currentGrid[i][j] = 1;
					}
					else if((currentGrid[i][j] == 1)&&(DEATH_LOWER < curGrid[i][j])&&(DEATH_UPPER > curGrid[i][j]))
					{
						currentGrid[i][j] = 0;
					}
					
				}
			}
		}

	}

	public double[] getMN(int a, int b)
	{
		
		double[] returnGrid = new double[2];
		for(int i = a-(int)RADIUS_OUTER; i < a+(int)RADIUS_OUTER+1; i++)
		{
			for(int j = b-(int)RADIUS_OUTER; j < b+(int)RADIUS_OUTER+1; j++)
			{
				int x = i;
				int y = j;
				while((x < 0)||(y<0)||(x > curGrid.length - 1)||(y > curGrid[0].length - 1)) 
				{	
					if (x < 0) 
					{
						x = curGrid.length + x;
					}
					if (x > curGrid.length - 1) 
					{ 
						x = x - curGrid.length;
					}	
					if (y < 0) { y = curGrid.length + y; }
					if (y > curGrid[0].length - 1) { y = y - curGrid.length; }
				}

				double rad = vert(i - a, j - b);
				if (rad > (RADIUS_OUTER+(BLUR_RADIUS_OUTER/2))) { continue; }
				else if (rad > (RADIUS_OUTER-(BLUR_RADIUS_OUTER/2))) { returnGrid[0] += ((RADIUS_OUTER+(BLUR_RADIUS_OUTER/2)-rad)/BLUR_RADIUS_OUTER)*curGrid[x][y]; }
				else if (rad > (RADIUS_INNER + (BLUR_RADIUS_INNER/2))) {returnGrid[0] += curGrid[x][y]; }
				else if (rad > (RADIUS_INNER - (BLUR_RADIUS_INNER/2))) 
				{
					returnGrid[0] += (1 - ((RADIUS_INNER+(BLUR_RADIUS_INNER/2)-rad)/BLUR_RADIUS_INNER))*curGrid[x][y];
					returnGrid[1] += ((RADIUS_INNER+(BLUR_RADIUS_INNER/2)-rad)/BLUR_RADIUS_INNER)*curGrid[x][y];
				}
				else { returnGrid[1] += curGrid[x][y]; }
			
			}
		}
		
		returnGrid[0] /= NORMAL_OUTER;
		returnGrid[1] /= NORMAL_INNER;

		return returnGrid;
	}


	public void move()
	{
		double[][] temp = new double[curGrid.length][curGrid[0].length]; 
		for(int i = 0; i < curGrid.length; i++)
		{
			for(int j = 0; j < curGrid[0].length; j++)
			{
				double[] mN = getMN(i,j);
				if((TYPE == 1)||(TYPE == 3))
					temp[i][j] = DELTA_T*sigma4(mN[0],mN[1]) + (1-DELTA_T)*curGrid[i][j];
				else 
					temp[i][j] = sigma4(mN[0],mN[1]);
			}
		}
		curGrid = temp;
	}


	public int[][] update (){
		toIntGrid();
		move();
		return currentGrid;
	}
	
}