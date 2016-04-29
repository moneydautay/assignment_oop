package graph;
import java.awt.*;

import javax.swing.JComponent;

public class AnnotateComponent extends JComponent {
	
	private static final long serialVersionUID = -6012190119823771505L;
	private static final int recSize = 10;

	public AnnotateComponent(Dimension size){
		 super();
		setPreferredSize(size);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
		g2d.setColor(Color.RED);
		g2d.fill(new Rectangle(110, 10, recSize, recSize));
		
		g2d.setColor(Color.BLACK);
		g2d.fill(new Rectangle(220, 10, recSize, recSize));
		
		g2d.setColor(Color.BLUE);
		g2d.fill(new Rectangle(350, 10, recSize, recSize));
		
		g2d.setColor(Color.ORANGE);
		g2d.fill(new Rectangle(470, 10, recSize, recSize));
		
		g2d.setColor(Color.BLACK);
		g2d.drawString("Initial State", 130, 20);
		g2d.drawString("Terminal State", 240, 20);
		g2d.drawString("Normal State", 370, 20);
		g2d.drawString("Initial & Terminal State", 490, 20);

		g2d.dispose();
	}	
}
