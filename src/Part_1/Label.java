package Part_1;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Label {
	
	private static Font FONT =  new Font("Serif", Font.BOLD, 20);
	private Point2D point;
	
	public Label(Font font){
		FONT = font;
	}
	
	public Label(Font font, Point2D point){
		FONT = font;
		this.point = point;
	}
	
	public static Font getFONT() {
		return FONT;
	}

	public static void setFONT(Font fONT) {
		FONT = fONT;
	}

	public Point2D getPoint() {
		return point;
	}

	public void setPoint(Point2D point) {
		this.point = point;
	}

	public void drawLabel(String label, Graphics2D g2d){ 
		g2d.setFont(FONT);
		FontMetrics fm = g2d.getFontMetrics(FONT);
		int widthLable = fm.stringWidth(label);
		// center String/text
		int cx = ((int) point.getX() - widthLable / 2);
		int cy = ((int) point.getY() + fm.getHeight() / 4);
		g2d.drawString(label, cx, cy);
	}
}
