import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
import java.awt.Insets;
import java.util.*;
//import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 125;
	private static final int GRID_Y = 100;
	private static final int INNER_CELL_SIZE = 70;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 10; 

	public int sec=0;
	public int min= 0;
	
	public int count = 0;
	
	public int score=0;
	
	public int timeX = 250;
	public int timeY = 5;
	
	public static Boolean bombsDisplayed = false;
	public static Boolean wonGame = false;
	
	Date startDate = new Date();
	
	public Graphics h;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public Boolean[][] bombArray = new Boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	
	public String[][] stringArray = new String[TOTAL_COLUMNS][TOTAL_ROWS];
	public Boolean[][] flagArray = new Boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] bombAdjacent = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public Boolean[][]freeArray  = new Boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}

		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 0; y < TOTAL_ROWS ; y++) { 
				colorArray[x][y] = Color.WHITE;
				bombArray[x][y] = false;
				stringArray[x][y] = "";
				bombAdjacent[x][y] = 0;
				freeArray[x][y] = false;
			}
		}
		GenerateMines();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;
		
		
		//Paint the background
		g.setColor(Color.PINK);
		g.fillRect(x1, y1, width + 1, height + 1);
		
		

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 

		/////////////////////////CAMBIA LAS LINEAS QUE DIVIDE EL GRID DE MINSWEEPER///////////////////
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS -1))); 
		}
		
		if (bombsDisplayed==true) {
			///////////////////CREATES BOX FOR THE TIMER//////////////////////////
			
			
			////////////////////CREATES THE TIMER FOR THE BOX//////////////////////
			score=sec;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans", Font.PLAIN, 80));
			g.drawString("TIMER: " + Integer.toString(sec), timeX, timeY+65);
			//System.out.println(score);
			
			
		}
		else {
			///////////////////CREATES BOX FOR THE TIMER//////////////////////////
			
			
			////////////////////CREATES THE TIMER FOR THE BOX//////////////////////
			sec = (int) ((new Date().getTime()-startDate.getTime())/ 1000);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans", Font.PLAIN, 80));
			g.drawString("TIMER: " + Integer.toString(sec), timeX, timeY+65);
		}
		repaint();
		//Draw an additional cell at the bottom left
		//g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS-1; y++) { ////added -1 para que no pinte el cuadrito extra

				Color c = colorArray[x][y];
				String d = stringArray[x][y];
				
				///////////////////////////////Si lo cambias cambia todo a un mismo color ////////////////////////
				
				g.setColor(c);
				g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1))+1 , INNER_CELL_SIZE, INNER_CELL_SIZE);
				
				g.setFont(new Font ("Comic Sans", Font.PLAIN, 40));
				g.setColor(new Color(139,69,19));
				g.drawString(d, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 25,  y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1))+45);
				
				
				
				g.setColor(c);
			}
		}
	}
	/////////////////////METHOD THAT CREATES MINES AT RANDOM PLACES And tells each square how many bombs are around it////////////////////
	public void GenerateMines() {
		int numOfBombs = 0;
		int totalBombs = 10;


		while(numOfBombs<totalBombs) {
			int randomX = new Random().nextInt(9);
			int randomY = new Random().nextInt(9);
			if (!(bombArray[randomX][randomY])) {
				bombArray[randomX][randomY] = true;	
				
				System.out.println("");
				System.out.println(randomX);
				System.out.println(randomY);
				System.out.println("");
				numOfBombs++;
				/////////////////////////
				for (int i= randomX-1; i<= randomX +1; i++) {
					if( (i >= 0)&& (i<TOTAL_COLUMNS)) {
						for(int j = randomY -1; j<= randomY+1; j++) {
							if((j >= 0)&&( j< TOTAL_ROWS -1)){
								if(randomX == i && randomY == j) {
									continue;
								}
								bombAdjacent[i][j] = bombAdjacent[i][j] + 1;
							}

						}
					}

				}
				/////// CHECKS IF MINES ARE BEING CREATED///
				//colorArray[randomX][randomY]=Color.BLACK;
				//repaint();
			}
		}

	}
	/////goes through all squares, if there'a a mine, the square turns black
	public void DisplayMines() {
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 0; y < TOTAL_ROWS ; y++) { 
				if(bombArray[x][y] ) { 
					colorArray[x][y] = new Color(25,25,25);
					
				}
			}
		}
	}

	// This method helps to find the adjacent boxes that don't have a mine.
	// It is partially implemented since the verify hasn't been discussed in class
	// Verify that the coordinates in the parameters are valid.
	// Also verifies if there are any mines around the x,y coordinate

	///////REVEALS THE NUMBER OF BOMBS SORROUNDING THE SQUARE
	public void revealAdjacent(int x, int y){
		if((x<0) || (y<0) || (x>=9) || (y>=9)){return;}
		else {
			Color newColor = Color.LIGHT_GRAY;
			String newString = "Hello";
			
			System.out.println("Count" + count);
			
			switch(bombAdjacent[x][y]) {
			case 1:
				newColor = new Color(255,250,215);
				newString = "1";
				break;
			case 2:
				newColor = new Color(220,255,255);
				newString = "2";
				break;
			case 3:
				newColor = new Color(221,160,221);
				newString = "3";
				break;
			case 4:
				newColor = new Color(255,235,238);
				newString = "4";
				break;
			case 5:
				newColor = new Color(244,238,224);
				newString = "5";
				break;
			case 6:
				newColor = new Color(255,228,225);
				newString = "6";
				break;
			case 7:
				newColor = new Color(255,228,181);
				newString = "7";
				break;
			case 8:
				newColor = new Color(230,230,250); ////lavender
				newString = "8";
				break;
			default:
				newColor = Color.LIGHT_GRAY;
				newString = "";
				break;
			}
			colorArray[x][y] = newColor;
			stringArray[x][y] = newString;
			

			repaint();
			
			
			

			if(bombAdjacent[x][y] == 0 && !freeArray[x][y]) {
				freeArray[x][y] = true;
				count += 1;



				//			colorArray[x][y] = Color.GRAY;
				//if(flagArray[x][y] != true){
					revealAdjacent(x-1, y);
					revealAdjacent(x+1, y);
					revealAdjacent(x, y-1);
					revealAdjacent(x, y+1);
					revealAdjacent(x-1, y-1);
					revealAdjacent(x+1, y-1);
					revealAdjacent(x+1, y+1);
					revealAdjacent(x-1, y+1);
			//	}
				//else{
					
			//	}
				
			}
			else if(bombAdjacent[x][y] != 0 && !freeArray[x][y]) {
				freeArray[x][y] = true;
				count += 1;
			}
		}

		//System.out.println("Test");

	}


	public void Win() {
				if(count>=71) { 

					wonGame= true;
					
				}
				
				return ;
			}
		



	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS -1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	
}