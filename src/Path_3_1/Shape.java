package Path_3_1;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Shape {
	private String type;
	private Point2D point;
	private int radius;
	private int dx,dy;
	private boolean haveLine;
	private boolean movable;
	private int lineIndex;
		
	public Shape(String type, Point2D point, int radius) {
		this.type = type;
		this.point = point;
		this.radius = radius;
		
		//dx = 0 and dy 0 get center of shape
		this.dx = 0;
		this.dy = 0;
		this.lineIndex = 0;
		haveLine = false;
		movable = false;
		
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public boolean isHaveLine() {
		return haveLine;
	}

	public void setHaveLine(boolean haveLine) {
		this.haveLine = haveLine;
	}

	public void setDxy(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public Point2D getPointClick(){
		return new Point2D.Double(point.getX() + dx, point.getY() + dy);
	}
	
	public void setRadius(int radius){
		this.radius = radius;
	}
	
	public int getRadius(){
		return radius;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Point2D getPoint() {
		return point;
	}
	
	public void setPoint(Point2D point) {
		this.point = point;
	}

	public void add(Shape newShape) {
		this.type = newShape.getType();
		this.point = newShape.getPoint();
	}
	
	public Object getBounds(){
		if(this.type=="line"){
			return new Ellipse2D.Double(point.getX() - radius, point.getY() - radius, radius*2, radius*2);
		}
		return new Ellipse2D.Double(point.getX() - radius, point.getY() - radius, radius*2, radius*2);
	}
	
	public Ellipse2D isCirclePointSelected(){
		return new Ellipse2D.Double(point.getX() + dx - 5, point.getY() + dy - 5, 10, 10);
	}
	
}
