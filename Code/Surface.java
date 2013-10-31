import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.Color;
import java.awt.Stroke;
import java.io.*;
import java.awt.image.*;
import java.awt.image.RescaleOp;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;

public class Surface extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	public int gridWidth;
	public int gridHeight;
	int gridCellSize = 1;
	
	BufferedImage buffImg;
	BufferedImage pausedImage;
	File img;
	
	int[][] grid;
	public int maxCols = 20;
	
	int xOffset = 0;
	int yOffset = 0;
	
	public Boolean drawLines = false;
	public Boolean repeatImage = false;
	
	Skeleton skeleton;
	
	int drawingVal = 0;
	
	int gridMouseX;
	int gridMouseY;
	
	public Surface (int[][] ingrid, Skeleton in){
		grid = ingrid;
		skeleton = in;
		
		img = new File("pausedImage.png");
		
		
		try {
			pausedImage = ImageIO.read(img);
         } catch (IOException e) {
			System.out.println(e.getMessage());
         }
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void init (int colNum, int wid, int high){
		maxCols = colNum;
		
		gridWidth = grid.length;
		gridHeight = grid[0].length;
		
		buffImg = new BufferedImage(grid.length, grid[0].length, BufferedImage.TYPE_INT_RGB);
	
		int xCent = wid - 100;
    	int yCent = high - 100;
		
		//gridCellSize = (int)Math.floor(Math.max((float)gridWidth / xCent*2, (float)gridHeight / yCent*2)); 
		
		int maxXCell = (int)((float)wid / (float)gridWidth);
		int maxYCell = (int)((float)high / (float)gridHeight);
		
		gridCellSize = (int)Math.max(1, Math.min(15, Math.min(maxXCell, maxYCell)));
		
		xOffset = xCent/2 - (gridWidth * gridCellSize)/2;
		yOffset = yCent/2 - (gridHeight * gridCellSize)/2;
		
	}
	
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        //colorGrid(g2d);
        
        //
        
       	buffImg.setRGB(0, 0, gridWidth, gridHeight, gridToRGBArray(grid), 0, gridWidth);
	
		int bitmSizeX = gridWidth * gridCellSize;
       	int bitmSizeY = gridHeight * gridCellSize;
       	
        
        //g2d.drawImage(buffImg, xOffset+gridWidth*gridCellSize, yOffset, gridWidth*gridCellSize, gridHeight*gridCellSize, null);
        if(repeatImage){
        	int screenX = getSize().width;
        	int screenY = getSize().height;
        	
        	
        	int xRepeats = screenX / bitmSizeX;
        	int yRepeats = screenY / bitmSizeY;
        	
        	int mXOffset = xOffset % bitmSizeX;
        	int mYOffset = yOffset % bitmSizeY;
        	
			for(int i = -1; i < xRepeats+2; i++){
				for(int j = -1; j < yRepeats+2; j++){
					g2d.drawImage(buffImg, mXOffset + i*bitmSizeX, mYOffset+j*bitmSizeY, gridWidth*gridCellSize, gridHeight*gridCellSize, null);
				}
			}
			
			xOffset = mXOffset;
			yOffset = mYOffset;
			
        }else{
			g2d.drawImage(buffImg, xOffset, yOffset, gridWidth*gridCellSize, gridHeight*gridCellSize, null);
        }
        
        
        
        if(drawLines && gridCellSize > 2){
        	drawGrid(g2d);
        }
        
        if(skeleton.paused){
        	g2d.drawImage(pausedImage, 0, 0, null);
		}
    }
    
    
    
    private int [] gridToRGBArray (int[][] grid){
    
    
    	int[] RGBArray = new int[grid.length * grid[0].length];
    	int k = 0;
    	
    	//if(maxCols > 3){
  		  	
			float val = 1 / (float)(maxCols);
    		for(int i = 0; i < grid[0].length; i++){
    			for(int j = 0; j<grid.length; j++){
    				
    				Boolean fake = false;
    				int drawCol = grid[j][i];
    				
    				if(mouseIn && i == gridMouseY && j == gridMouseX){
    					drawCol = drawingVal;
    					fake = true;
    				}
    				
    				if(drawCol != 0){
    					int temp = Color.getHSBColor(val * (float)drawCol, 0.8f, 1.0f).getRGB();
    					if(fake){
    						temp = Color.getHSBColor(val * (float)drawCol, 0.4f, 0.8f).getRGB();
    					}
						RGBArray[k] = temp;
    				}else{
    					RGBArray[k] = Color.black.getRGB();
    				}
    			
    				k++;
    			}
 		   	}
    		return RGBArray;
	}
    
    
    
    
    
	public void setGrid (int[][] inGrid){
		grid = inGrid;
		gridWidth = grid.length;
		gridHeight = grid[0].length;	
	};
	
    private void drawGrid(Graphics2D g2d){
    	//g2d.setStroke(Stroke.green);
    	for(int i = 0; i < gridWidth; i++){
    		g2d.drawLine(xOffset + i*gridCellSize, yOffset, xOffset + i*gridCellSize, yOffset + gridCellSize*gridHeight);
    	}
    	for(int i = 0; i < gridHeight; i++){
    		g2d.drawLine(xOffset, yOffset + i*gridCellSize, xOffset + gridCellSize*gridWidth, yOffset + i*gridCellSize);   			
    	}
    }
    
        
    public int getMaxColors (){
    	return maxCols;
    }
    
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
    
    
    
    /////////UI INTEGRATION SHIT
    
    public void zoomIn (){
    	if(gridCellSize < 15){
    				
    		
    		int xCent = this.getSize().width / 2;
    		int yCent = this.getSize().height / 2;
    		
    		int xDiff = xCent - xOffset;
    		int yDiff = yCent - yOffset;
    		
    		int cellsX = xDiff / gridCellSize;
    		int cellsY = yDiff / gridCellSize;
    		
    		gridCellSize++;
    		
    		int newX = xOffset + cellsX * gridCellSize;		
    		int newY = yOffset + cellsY * gridCellSize;		
    		
    		xOffset -= newX - xCent;
    		yOffset -= newY - yCent;
    		
    		repaint();
    		
    	}
    }
    public void zoomOut(){
    	
    	if(gridCellSize > 1){
    		    		
    		
    		int xCent = this.getSize().width / 2;
    		int yCent = this.getSize().height / 2;
    		
    		int xDiff = xCent - xOffset;
    		int yDiff = yCent - yOffset;
    		
    		int cellsX = xDiff / gridCellSize;
    		int cellsY = yDiff / gridCellSize;
    		
    		gridCellSize--;
    		
    		int newX = xOffset + cellsX * gridCellSize;		
    		int newY = yOffset + cellsY * gridCellSize;		
    		
    		xOffset -= newX - xCent;
    		yOffset -= newY - yCent;
    		
    		repaint();
    	}
    }
    
    public void drawPixelFromMouse(){
    
       	skeleton.Automata.setCell(gridMouseX, gridMouseY, drawingVal);
    	repaint();
    }
    
    
    /*
    
    mousy mousy
    
    */
    
    public Boolean mouseDown = false;
    public Boolean mouseIn = false;
    int oldMX = 0;
    int oldMY = 0;
    
    
    public void mousePressed(MouseEvent e){
		if(SwingUtilities.isRightMouseButton(e)){
			mouseDown = true;
			oldMX = e.getX();
			oldMY = e.getY();
		}
		
		if(SwingUtilities.isLeftMouseButton(e)){
			drawPixelFromMouse();
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	public void mouseEntered(MouseEvent e) {
		mouseIn = true;
	}

	public void mouseExited(MouseEvent e) {
		mouseIn = false;
	}
	
	public void mouseClicked(MouseEvent e) { }
	
	public void keyPressed (KeyEvent e){
		//System.out.println(e.getKeyCode());
		if(e.getKeyCode() == 32){
			skeleton.pausePlay();
		}else{
			drawingVal = e.getKeyCode() - 49;
		}
		repaint();
	}
	
	public void keyReleased (KeyEvent e){ }
    
    public void keyTyped (KeyEvent e){
    	//System.out.println(e.getKeyCode());
    }
    
    public void mouseDragged(MouseEvent e) {
    	gridMouseX = (((e.getX()-xOffset)/gridCellSize)+gridWidth) % gridWidth;
    	gridMouseY = (((e.getY()-yOffset)/gridCellSize)+gridHeight) % gridHeight;
    	
    	if(SwingUtilities.isRightMouseButton(e)){
    		xOffset += (e.getX() - oldMX);
    		yOffset += (e.getY() - oldMY);
    	
    		oldMX = e.getX();
			oldMY = e.getY();
		
			repaint();
		}
		if(SwingUtilities.isLeftMouseButton(e)){
			drawPixelFromMouse();
		}
		repaint();
    }
    
    public void mouseMoved(MouseEvent e){
    	gridMouseX = (((e.getX()-xOffset)/gridCellSize)+gridWidth*20) % gridWidth;
    	gridMouseY = (((e.getY()-yOffset)/gridCellSize)+gridHeight*20) % gridHeight;
    	repaint();
    }
    
    
    ////
    
    
    
}
