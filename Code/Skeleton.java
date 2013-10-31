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
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;

public class Skeleton extends JFrame {
	
	Surface surface;
	
	int width = 300;
	int height = 300;
	
	int delay = 100;
	Timer timer;
	boolean paused = false;
	
	int colNum;
	
	public CellularAutomata Automata;
	
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
				System.out.println("\nType?");
				CurLine = in.readLine();
				if (CurLine.toUpperCase().equals("GOL")){
					
					float blankPerc = 2.0f;
					while(blankPerc > 1.0f){
						System.out.println("\nBlank Percent?");
						CurLine = in.readLine();
					
						blankPerc = Float.parseFloat(CurLine);
					}
				
					Automata = new GOLabs(makeRandomGrid(width, height, 2, blankPerc));
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
				} else if (CurLine.toUpperCase().equals("1D")){
								
					colNum = 2;
					
					System.out.println("\nRule Number?");
					CurLine = in.readLine();
					
					Automata = new ElementaryCA(makeBlankGrid(width, height), Integer.parseInt(CurLine));
					
					for(int i = 0; i < width; i++){
						Automata.setCell(i, 1, (int)(0.5f+Math.random()));
					}
					//Automata.setCell((int)(width/2), 1, 1);
					
					surface.repeatImage = false;
				} else if (CurLine.toUpperCase().equals("BML")){
					
					colNum = 3;
					
					float blankPerc = 2.0f;
					
					while(blankPerc > 1.0f){
						System.out.println("\nBlank Percent?");
						CurLine = in.readLine();
					
						blankPerc = Float.parseFloat(CurLine);
					}
					
					Automata = new BMLTraffic(makeRandomGrid(width, height, 3, blankPerc));
					
				}
				else if (CurLine.toUpperCase().equals("ALEX")) {
					System.out.println("Colour number?");
					colNum = Integer.parseInt(in.readLine());
					System.out.println("Continuous time? (0 for No, 1 for Yes)");
					int type = Integer.parseInt(in.readLine());
					System.out.println("Full box? (0 for No, 1 for Yes)");
					type += 2*Integer.parseInt(in.readLine());
					Automata = new SmoothLifeAlex(makeRandomGrid(width, height, colNum), colNum, type);
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
		if(delay > 1){
			delay = Math.min(delay - (int)(0.3 * delay), delay - 1);
			System.out.println(delay);
		}else{
			delay = 1;
		}
		
		
		if(!paused){
			timer.stop();
			runCA();
		}
	}
	
	public void slowDown(){
		if(delay < 1500){
			delay = Math.max(delay + (int)(0.3 * delay), delay + 3);
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
    		surface.repaint();
    	}
    	paused = !paused;
    }
	
	
	private void initUI() {

		setTitle("waddup");
		
		surface = new Surface(Automata.getGrid(), this);
		//Automata.setSurface(surface);
		add(surface);
		setSize(1000, 800);
		
		this.addKeyListener(surface);
		
		
		
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
				
				int rand = (int)((float)(nos) * Math.random());
				
				rGrid[i][j] = rand;
			}
		}
		return rGrid;
	}
	
	public int[][] makeRandomGrid (int w, int h, int nos, float blanks){
    	int[][] rGrid = new int[w][h];
		for(int i = 0; i < w; i++){
			for(int j = 0; j<h; j++){
				
				float rand = (float)Math.random();
				
				if(rand < blanks){
					rGrid[i][j] = 0;
				}else{
					rGrid[i][j] = (int)(((float)(nos-1)) * Math.random()) + 1;
				}
			}
		}
		return rGrid;
	}
	
	
	public int [][] makeBlankGrid (int w, int h){
		int[][] rGrid = new int[w][h];
		for(int i = 0; i < w; i++){
			for(int j = 0; j<h; j++){
				rGrid[i][j] = 0;
			}
		}
		return rGrid;
	}
	
	public int getGridWidth() {
		return width;
	}
	public int getGridHeight (){
		return height;
	}
	
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
	
	JButton randomGrid;
	JButton drawGridLines;
	JButton tileImage;
	
	JButton loadFile;
	JButton saveFile;

	Surface surface;
	Skeleton skeleton;
	
	final JFileChooser fc = new JFileChooser();
	
	public ToolboxPanel (Surface inSurface, Skeleton inSkeleton) {
		surface = inSurface;
		skeleton = inSkeleton;
		
		zoomIn = initBut(zoomIn, "Zoom In");
		zoomOut = initBut(zoomOut, "Zoom Out");
		slowDown = initBut(slowDown, "Slow Down");
		speedUp = initBut(speedUp, "Speed Up");
		pausePlay = initBut(pausePlay, "Pause / Play");
		randomGrid = initBut(randomGrid, "Random Grid");
		drawGridLines = initBut(drawGridLines, "Grid Lines On/Off");
		tileImage = initBut(tileImage, "Tile Image");
		loadFile = initBut(loadFile, "Load File");
		saveFile = initBut(saveFile, "Save File");
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
		
		if(src == randomGrid){
			skeleton.Automata.setGrid(skeleton.makeRandomGrid(skeleton.getGridWidth(), skeleton.getGridHeight(), surface.getMaxColors()));
			surface.setGrid(skeleton.Automata.getGrid());
			surface.repaint();
		}
		
		if(src == drawGridLines){
			surface.drawLines = !surface.drawLines;
		}
		
		if(src == tileImage){
			surface.repeatImage = !surface.repeatImage;
			surface.repaint();
		}
		
		if(src == loadFile){
			loadFile();
		}
		
		if(src == saveFile){
			saveFile();
		}
	}
	
	public JButton initBut (JButton button, String label){
		button = new JButton(label);
		add(button);
		button.setVisible(true);
		button.addActionListener(this);
		
		return button;
	}
	
	public void saveFile (){
		int returnVal = fc.showSaveDialog(this);
		
		//BufferedWriter
	}
	
	public void loadFile(){
		int returnVal = fc.showOpenDialog(this);
	}
	
	/*public gridToString(int[][] grid){
		
	}*/
}


















