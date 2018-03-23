import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;



public class Main {
	public static void main(String[] args) {
		JFrame myFrame = new JFrame("Minesweeper");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 150);
		myFrame.setSize(900, 900);
		
		
		
		/*Container contentPane = myFrame.getContentPane();
        contentPane.setLayout(null);
        JLabel img = new JLabel(new ImageIcon("icon.gif"));
        img.setBounds(200, 300, 100, 100); // x, y, width, height
        contentPane.add(img);*/

		
		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);

		MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);
		
		/*JMenuBar menubar = new JMenuBar();
		
		JMenu opt = new JMenu("Options");
		JMenu bkg = new JMenu("Background Colour");
		
		JMenuItem blue= new JMenuItem("Blue");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem reset = new JMenuItem("Reset");

		menubar.add(opt);
		menubar.add(bkg);
		
		opt.add(reset);
		opt.add(exit);
		
		
		bkg.add(blue);
		
		myFrame.setJMenuBar(menubar);*/
		
		
		
		myFrame.setVisible(true);
	}
}
