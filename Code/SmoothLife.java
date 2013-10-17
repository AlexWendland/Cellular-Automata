public class SmoothLife extends CellularAutomata {
	
	int cycleLength;
	
	//float[][] f;
	int innerRad;
	int outerRad;
	
	float b1;
	float b2;
	float d1;
	float d2;
	
	int scale;
	
	public int getNeighbour (int x, int y, int ind){
		return super.getMooreNeighbour(x, y, ind);
	}

	
	public SmoothLife (int[][] grid){
		super(grid);
		
		
 		scale = 10000;
		
		innerRad = 7;
		outerRad = 21;
		
		b1 = 0.278f;
		b2 = 0.365f;
		d1 = 0.267f;
		d2 = 0.445f;
	}
	
	public int torus (int x, int max){
		if ( x < 0 ){
			return max + x;
		}
		return x % max; 
	}
	
	public float getFilling (int x, int y, int ri, int ro){
		int integral = 0;
		float tempDist;
		int riSq = ri * ri;
		int roSq = ro * ro;
		int scaler = 0;
		for (int xp = -ro; xp<ro; xp++){
			for (int yp = -ro; yp < ro; yp++){
				tempDist = xp * xp + yp * yp;
				
				if(tempDist < roSq && tempDist > riSq){
					scaler += scale;
					integral += currentGrid[torus(x+xp, super.width)][torus(y+yp, super.height)];
				}
			}
		}
		return (float)((float)integral / ((float)scaler));
	}
	
	public int rules (int x, int y){
		
		float m = getFilling(x, y, 0, innerRad);
		float n = getFilling(x, y, innerRad, outerRad);
		
		if(x == 300 && y == 200){
			System.out.println("M: "+m + ", N:"+ n);
		}
		
		return  (int) ((float)(scale) * sigma2(n, sigmaM(b1, d1, m), sigmaM(b2, d2, m)));
		
		/*if(m > 0.5f){
			if(n > d2 || n < d1){
				return 0;
			}else{
				return scale;
			}
		}
		if(m < 0.5f){
			if(n > b1 && n <b2){
				return scale;
			}else{
				return 0;
			}
		}*/
		
		//return scale;
	}
	
	public float sigma1 (float x, float a) {
		return ( 1.0f / ( 1.0f + (float)Math.exp(-(x-a) * 4.0f / a)) );
	}
	
	public float sigma2 (float x, float a, float b) {
		return ( sigma1(x, a) * (1.0f - sigma1 (x, b)) );
	}
	
	public float sigmaM (float x, float y, float m){
		return ( x * (1.0f - sigma1(m, 0.5f)) + y * sigma1(m, 0.5f) );
	}
	
	public int[][] update (){
		System.out.println("tick");
		return super.update();
	}
}
