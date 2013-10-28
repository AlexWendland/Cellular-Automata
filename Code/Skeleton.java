////////// idea - multiple windows - one for options another for the grid itself???

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

class Surface extends JPanel implements MouseListener, MouseMotionListener {
	
	int gridWidth;
	int gridHeight;
	int gridCellSize = 1;
	
	BufferedImage buffImg;
	
	int[][] grid;
	
	
	int maxCols = 20;
	
	int xOffset = 0;
	int yOffset = 0;
	
	
	public Surface (int[][] ingrid){
		grid = ingrid;
		
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
       	
        g2d.drawImage(buffImg, xOffset, yOffset, gridWidth*gridCellSize, gridHeight*gridCellSize, null);
        //g2d.drawImage(buffImg, xOffset+gridWidth*gridCellSize, yOffset, gridWidth*gridCellSize, gridHeight*gridCellSize, null);
        
        
        if(gridCellSize > 3){
        	//drawGrid(g2d);
        }
     
    }
    
    
    
    private int [] gridToRGBArray (int[][] grid){
    	int k = 0;
    	
    	float val = 1 / (float)(maxCols);
    	
    	int[] RGBArray = new int[grid.length * grid[0].length];
    	
    	for(int i = 0; i < grid[0].length; i++){
    		for(int j = 0; j<grid.length; j++){
    			int temp = Color.getHSBColor(val * (float)grid[j][i], 0.8f, 1.0f).getRGB();
    			
				RGBArray[k] = temp;
    			
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
    		g2d.drawLine(i*gridCellSize,  0, i*gridCellSize, gridCellSize*gridHeight);
    	}
    	for(int i = 0; i < gridHeight; i++){
    		g2d.drawLine(0, i*gridCellSize, gridCellSize*gridWidth, i*gridCellSize);   			
    	}
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
    		
    		
    		/*int newXCent = xOffset + (xCent - xOffset) * 2; 
    		int newYCent = yOffset + (yCent - yOffset) * 2;*/
    		/*
    		xOffset = (xCent - xOffset);
    		yOffset = (yCent - yOffset);
    		*/
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
    
    
    /*
    
    mousy mousy
    
    */
    
    public Boolean mouseDown = false;
    int oldMX = 0;
    int oldMY = 0;
    
    
    public void mousePressed(MouseEvent e){
		mouseDown = true;
		oldMX = e.getX();
		oldMY = e.getY();
	}
	
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
    
    public void mouseDragged(MouseEvent e) {
    	xOffset += (e.getX() - oldMX);
    	yOffset += (e.getY() - oldMY);
    	
    	oldMX = e.getX();
		oldMY = e.getY();
		
		repaint();
    }
    
    public void mouseMoved(MouseEvent e){
    	
    }
    
}

public class Skeleton extends JFrame {
	
	Surface surface;
	
	int width = 300;
	int height = 300;
	
	int delay = 100;
	Timer timer;
	boolean paused = false;
	
	int colNum;
	
	CellularAutomata Automata;
	
    public Skeleton() {
		
		//Automata = new GOLabs(makeRandomGrid(width, height, 2));
      	
      	System.out.println("\nWidth?");
		
		String CurLine = ""; // Line read from standard in
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		
		
		
		try{
			CurLine = in.readLine();
			
			width = Integer.parseInt(CurLine);
			
			System.out.println("\nHeight?");
			CurLine = in.readLine();
			height = Integer.parseInt(CurLine);
			
			while(Automata == null){
				System.out.println("Type?");
				CurLine = in.readLine();
				if (CurLine.toUpperCase().equals("GOL")){
					Automata = new GOLabs(makeRandomGrid(width, height, 2));
					colNum = 2;
				}else if (CurLine.toUpperCase().equals("CYCLIC")){
				
					System.out.println("Cycle Length?");
					CurLine = in.readLine();
					int cycleLength = Integer.parseInt(CurLine);
					
					colNum = cycleLength;
					
					Automata = new CyclicAutomata(makeRandomGrid(width, height, cycleLength), cycleLength);
				} else if (CurLine.toUpperCase().equals("SMOOTHLIFE")){
					System.out.println("good luck");
					
					colNum = 100;
					
					Automata = new SmoothLife (makeRandomGrid(width, height, 100));
				}
			}
			
		} catch (Exception e){
		
		}
		
		
		//Automata = new CyclicAutomata(makeRandomGrid(width, height, 25), 25);
      
		initUI();
		
		runCA();
	}

	private void runCA(){
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Automata.update();
				renderGrid(Automata.getGrid());
			}
		};
		timer = new Timer(delay, taskPerformer);
		timer.start();
	}
	
	
	public void speedUp (){
		if(delay >= 5){
			delay -= (int)(0.3 * delay);
			System.out.println(delay);
		}else{
			delay = 10;
		}
		
		
		if(!paused){
			timer.stop();
			runCA();
		}
	}
	
	public void slowDown(){
		if(delay < 1500){
			delay += (int)(0.3 * delay);
			System.out.println(delay);
		}else{
			delay = 1500;
		}
		
		if(!paused){
			timer.stop();
			runCA();
		}
	}
	
	
    public void pausePlay (){
    	if(paused){
    		runCA();
    	}else{
    		timer.stop();
    	}
    	paused = !paused;
    }
	
	
	private void initUI() {

		setTitle("waddup");
		
		surface = new Surface(Automata.getGrid());
		//Automata.setSurface(surface);
		add(surface);
		setSize(1000, 800);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JFrame toolbox = new Toolbox(surface, this);
		
		surface.init(colNum, getSize().width, getSize().height);
	}
	
	private void renderGrid (int [][] grid){
		surface.setGrid(grid);
		surface.repaint();
	}
	
	public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Skeleton sk = new Skeleton();
                sk.setVisible(true);
            }
        });
    }
    
    //
    
    public int[][] makeRandomGrid (int w, int h, int nos){
    	int[][] rGrid = new int[w][h];
		for(int i = 0; i < w; i++){
			for(int j = 0; j<h; j++){
				
				int rand = (int)((float)nos * Math.random());
				
				rGrid[i][j] = rand;
				
				/*if( < 0.3f){
					rGrid[i][j] = 1;
				}else{
					rGrid[i][j] = 0;
				}*/
			}
		}
		return rGrid;
	}
    
    //
    
    
    
}


class Toolbox extends JFrame {
	public Toolbox (Surface inSurface, Skeleton inSkeleton){
		setVisible(true);
		setSize(100, 400);
		setLocation(200, 300);
		
		ToolboxPanel tbp = new ToolboxPanel(inSurface, inSkeleton);
		
		add(tbp);
	}
}

class ToolboxPanel extends JPanel implements ActionListener {
	
	JButton zoomIn;
	JButton zoomOut;
	
	JButton slowDown;
	JButton speedUp;
	JButton pausePlay;
	
	
	Surface surface;
	Skeleton skeleton;
	
	public ToolboxPanel (Surface inSurface, Skeleton inSkeleton) {
		surface = inSurface;
		skeleton = inSkeleton;
		
		/*zoomIn = new JButton("Zoom In");
		add(zoomIn);
		zoomIn.setVisible(true);
		zoomIn.addActionListener(this);
		
		zoomOut = new JButton("Zoom Out");
		add(zoomOut);
		zoomOut.setVisible(true);
		zoomOut.addActionListener(this);*/
		
		zoomIn = initBut(zoomIn, "Zoom In");
		zoomOut = initBut(zoomOut, "Zoom Out");
		slowDown = initBut(slowDown, "Slow Down");
		speedUp = initBut(speedUp, "Speed Up");
		pausePlay = initBut(pausePlay, "Pause / Play");
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		
		JButton src = (JButton)e.getSource();
		
		if(src == zoomIn){
			surface.zoomIn();
			
		}
		if(src == zoomOut){
			surface.zoomOut();
		}
		
		if(src == slowDown){
			skeleton.slowDown();
		}
		
		if(src == speedUp){
			skeleton.speedUp();
		}
		
		if(src == pausePlay){
			skeleton.pausePlay();
		}
	}
	
	public JButton initBut (JButton button, String label){
		button = new JButton(label);
		add(button);
		button.setVisible(true);
		button.addActionListener(this);
		
		return button;
	}
}

class MouseInputs implements MouseListener {
	public void mousePressed(MouseEvent e){
		System.out.println(e.getX());
	}
	
	
	public void mouseReleased(MouseEvent e) {
		System.out.println(e.getX());
	}

	public void mouseEntered(MouseEvent e) {
		System.out.println(e.getX());
	}

	public void mouseExited(MouseEvent e) {
		System.out.println(e.getX());
	}
	
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getX());
	}
	
}






