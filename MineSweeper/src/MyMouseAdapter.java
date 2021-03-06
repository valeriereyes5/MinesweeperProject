import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MyMouseAdapter extends MouseAdapter {
	public void mousePressed(MouseEvent e) {
		
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component d = e.getComponent();
			while (!(d instanceof JFrame)) {
				d = d.getParent();
				if (d == null) {
					return;
				}
			}
			myFrame = (JFrame) d;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			myInsets = myFrame.getInsets();
			int w1 = myInsets.left;
			int z1 = myInsets.top;
			e.translatePoint(-w1, -z1);
			int w = e.getX();
			int z = e.getY();
			myPanel.x = w;
			myPanel.y = z;
			myPanel.mouseDownGridX = myPanel.getGridX(w, z);
			myPanel.mouseDownGridY = myPanel.getGridY(w, z);
			myPanel.repaint();
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
					
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
					
						//Released the mouse button on the same cell where it was pressed
						if (myPanel.bombArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY]) {
							///// colors square to black if theres a bomb
							myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.BLACK;
							myPanel.repaint();
							//// calles DisplayMines method which displays all other mines
							myPanel.DisplayMines();
							MyPanel.bombsDisplayed = true;
						}else {
							myPanel.revealAdjacent(myPanel.mouseDownGridX, myPanel.mouseDownGridY);
							

							myPanel.repaint();

						}


					}
				}
			}
			myPanel.Win();
			String wonOrLose = " ";
			if (MyPanel.bombsDisplayed == true || MyPanel.wonGame == true ) {
				if (MyPanel.bombsDisplayed ==true){
				 wonOrLose = "GameOver!";
				 
				}
				else if(MyPanel.wonGame ==true){
				 wonOrLose = "You Won!";
				 
					}
				Object[] options = {"Yes",
	                    "Exit"};
	int n = JOptionPane.showOptionDialog(myFrame,
	    "Would You Like To Play Again?",
	    wonOrLose,
	    JOptionPane.YES_NO_CANCEL_OPTION,
	    JOptionPane.QUESTION_MESSAGE,
	    null,
	    options,
	    options[1]);
	
	System.out.println(n);
	
	if (n == 0) {
		myFrame.dispose();
		MyPanel.bombsDisplayed = false;
		MyPanel.wonGame = false;
		Main.main(null);
		
		y=0;
			
	}
	if (n == 1) {
		myFrame.dispose();
		
		
	}
			}
			else {
				
				myFrame.setVisible(true);
				
			}
			myPanel.repaint();
			break;
		case 3:		//Right mouse button
			Component d = e.getComponent();
			while (!(d instanceof JFrame)) {
				d = d.getParent();
				if (d == null) {
					return;
				}
			}
			myFrame = (JFrame)d;
			myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			myInsets = myFrame.getInsets();
			int w1 = myInsets.left;
			int z1 = myInsets.top;
			e.translatePoint(-w1, -z1);
			int w = e.getX();
			int z = e.getY();
			myPanel.x = w;
			myPanel.y = z;
			gridX = myPanel.getGridX(w, z);
			gridY = myPanel.getGridY(w, z);
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
				//Had pressed outside
				//Do nothing
			} else {
				if ((gridX == -1) || (gridY == -1)) {
					//Is releasing outside
					//Do nothing
				} else {
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					} else {
						//Released the mouse button on the same cell where it was pressed

						//On the grid other than on the left column and on the top row:
						Color newColor = null;
						Boolean flag = true;
						/////////////Right click- white turns red////
						if (myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.WHITE)) {
							newColor = Color.RED;
						flag = true;
						}
						/////////Right click - red turns white///
						else if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED)) {
							newColor = Color.WHITE;
							flag = false;

						}	
						myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
						myPanel.flagArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = flag;
						myPanel.repaint();
						

					}
				}
			}
			myPanel.repaint();				
			break;
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
}