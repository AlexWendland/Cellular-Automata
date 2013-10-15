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
import java.io.*;

class Surface extends JPanel {
	
	int gridWidth;
	int gridHeight;
	int gridCellSize = 1;
	
	
	int[][] grid;
	
	Color[]cols = new Color[102];	
	
	
	public Surface (int[][] ingrid){
		grid = ingrid;
		
		/*cols[0] = Color.black;
		cols[1] = Color.white;
		
		for(int i = 2; i<cols.length; i++){
			cols[i] = cols[i-1].darker();
			if(cols[i].equals(Color.black)){
				cols[i] = Color.red;
			}
		}*/
		
		
		for(int i = 0; i < cols.length; i++){
			float ratio = (float)i/(float)cols.length;
			cols[i] = new Color(ratio, ratio, ratio);
		}
	}
	
    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawGrid(g2d);
        colorGrid(g2d);
    }
    
	public void setGrid (int[][] inGrid){
		grid = inGrid;
		gridWidth = grid.length;
		gridHeight = grid[0].length;
	};
	
    private void drawGrid(Graphics2D g2d){
    	for(int i = 0; i < gridWidth; i++){
    		g2d.drawLine(i*gridCellSize,  0, i*gridCellSize, gridCellSize*gridHeight);
    	}
    	for(int i = 0; i < gridHeight; i++){
    		g2d.drawLine(0, i*gridCellSize, gridCellSize*gridWidth, i*gridCellSize);   			
    	}
    }
    
    private void colorGrid (Graphics2D g2d){
    
    	for (int i = 0; i<grid.length; i++){
			for(int j = 0; j<grid[i].length; j++){
				
				if(grid[i][j] < cols.length){
					g2d.setPaint(cols[grid[i][j]]);
				}else{
					System.out.println("ColNo:"+grid[i][j]+" You haven't defined enough colours you shitstain");
				}
				
				int xpos = i*gridCellSize;
				int ypos = j*gridCellSize;
				g2d.fill(new Rectangle(xpos, ypos, gridCellSize, gridCellSize));
			}
		}
    }
    
    @Override


    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}

public class Skeleton extends JFrame {
	
	Surface surface;
	
	int width = 300;
	int height = 300;
	
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
				}else if (CurLine.toUpperCase().equals("CYCLIC")){
				
					System.out.println("Cycle Length?");
					CurLine = in.readLine();
					int cycleLength = Integer.parseInt(CurLine);
					
					Automata = new CyclicAutomata(makeRandomGrid(width, height, cycleLength), cycleLength);
				} else if (CurLine.toUpperCase().equals("SMOOTHLIFE")){
					System.out.println("good luck");
					
					Automata = new SmoothLife (makeRandomGrid(width, height, 100));
				}
			}
			
		} catch (Exception e){
		
		}
		
		
		//Automata = new CyclicAutomata(makeRandomGrid(width, height, 25), 25);
      
		initUI();
		
		JFrame toolbox = new Toolbox();
		
		runCA();
	}

	private void runCA(){
		int delay = 10; //milliseconds
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Automata.update();
				renderGrid(Automata.getGrid());
			}
		};
		new Timer(delay, taskPerformer).start();
	}
	
	private void initUI() {

		setTitle("waddup");
		
		surface = new Surface(Automata.getGrid());
		//Automata.setSurface(surface);
		
		add(surface);
		
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
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
    
}


class Toolbox extends JFrame {
	public Toolbox (){
		setVisible(true);
		setSize(100, 400);
		setLocation(200, 300);
		
		ToolboxPanel tbp = new ToolboxPanel();
		
		add(tbp);
	}
}

class ToolboxPanel extends JPanel implements ActionListener {
	
	JButton button;
	public ToolboxPanel () {
		button = new JButton(" HEELOOO");
		add(button);
		button.setVisible(true);
		button.addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button){
			System.out.println("btttsts");
		}
	}
}








