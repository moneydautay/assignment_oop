package graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import machine.StateImpl;

public class Shape {
	private String type;
	private Point2D point;
	private boolean haveLine;
	private boolean movable;
	private int lineIndex;
	private int radius;
	private StateImpl state;
	
	public Shape(){
		this.type = null;
		this.point = null;
		this.radius = 0;
		haveLine = false;
		lineIndex = -1;
	}
	
	public Shape(String type, Point2D point, int radius) {
		this.type = type;
		this.point = point;
		this.radius = radius;
		haveLine = false;
		lineIndex = -1;
	}

	public StateImpl getState() {
		return state;
	}

	public void setState(StateImpl stateImpl) {
		this.state = stateImpl;
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
	
	public Point2D getPointClick(){
		return new Point2D.Double(point.getX(), point.getY());
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
	
	public void drawState(Graphics2D g2d, String label, Label labelFont){
		g2d.fill(new Ellipse2D.Double((int) point.getX() - radius,
				(int) point.getY() - radius, radius*2, radius*2));
		g2d.setColor(Color.white);
		labelFont.setPoint(point);
		labelFont.drawLabel(label, g2d);
	}
	
	public Object getBounds(){
		if(this.type=="line"){
			return new Ellipse2D.Double(point.getX() - radius, point.getY() - radius, radius*2, radius*2);
		}
		return new Ellipse2D.Double(point.getX() - radius, point.getY() - radius, radius*2, radius*2);
	}
		
}
