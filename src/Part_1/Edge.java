package Part_1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import java.awt.Shape;
import java.awt.Stroke;

public class Edge{
	
	//source and destination is index of shape in list shape
	private String type;
	private float dashes[] = { 5f, 5f };
	private int sourceParent;
	private int destinationParent;
	private boolean movable;
	private Point2D ctrl1;
	private Point2D ctrl2;
	private String label;
	
	Edge(String type){
		this.type = type;
		ctrl1 = new Point2D.Float();
		ctrl2 = new Point2D.Float();
	}
	
	Edge(int source, int destination, String label, String type){
		this.sourceParent = source;
		this.destinationParent = destination; 
		this.label = label;
		this.type = type;
		ctrl1 = new Point2D.Float();
		ctrl2 = new Point2D.Float();
	}
	
	Edge(int source, int destination, String label, String type, Point2D ctrl1,  Point2D ctrl2){
		this.sourceParent = source;
		this.destinationParent = destination; 
		this.label = label;
		this.type = type;
		this.ctrl1 = ctrl1;
		this.ctrl2 = ctrl2;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	Edge(int source, int destination, boolean movable){
		this.sourceParent = source;
		this.destinationParent = destination; 
		this.movable = movable;
	}
	
	public boolean isMovable() {
		return movable;
	}
	
	public int getsourceParent() {
		return sourceParent;
	}

	public void setsourceParent(int source) {
		this.sourceParent = source;
	}

	public int getdestinationParent() {
		return destinationParent;
	}

	public void setdestinationParent(int destination) {
		this.destinationParent = destination;
	}

	public Point2D getCtrl1() {
		return ctrl1;
	}

	public void setCtrl1(Point2D ctrl1) {
		this.ctrl1 = ctrl1;
	}

	public Point2D getCtrl2() {
		return ctrl2;
	}

	public void setCtrl2(Point2D ctrl2) {
		this.ctrl2 = ctrl2;
	}
	
	public boolean isAnchorCtrl(MouseEvent e, int type){
		if(type==1)
			return (new Ellipse2D.Float((int)ctrl1.getX()- 5,(int)ctrl1.getY() - 5, 10, 10).contains(e.getPoint()));
		else
			return (new Ellipse2D.Float((int)ctrl2.getX()- 5,(int)ctrl2.getY() - 5, 10, 10).contains(e.getPoint()));
	}
	
	public Shape createArrowShape(Point2D fromPt, Point2D toPt, String type) {
		Polygon arrowPolygon = new Polygon();

		arrowPolygon.addPoint(3, 0);
		arrowPolygon.addPoint(0, 3);
		arrowPolygon.addPoint(3, 0);
		arrowPolygon.addPoint(0, -3);
		arrowPolygon.addPoint(3, 0);

		Point2D midPoint = midpoint(fromPt, toPt);

		double rotate = Math.atan2(toPt.getY() - fromPt.getY(), toPt.getX() - fromPt.getX());

		AffineTransform transform = new AffineTransform();

		transform.translate(midPoint.getX(), midPoint.getY());
		double scale = 3; // 12 because it's the length of the arrow polygon.
		transform.scale(scale, scale);
		transform.rotate(rotate);

		return transform.createTransformedShape(arrowPolygon);
	}
	
	private Point2D midpoint(Point2D fromPt, Point2D toPt) {
		return new Point2D.Double(fromPt.getX() + (toPt.getX() - fromPt.getX()) / 2,
				fromPt.getY() + (toPt.getY() - fromPt.getY()) / 2);
	}
	
	public void drawEdge(Graphics2D g2d, List<Part_1.Shape> states, Label labelFont, boolean selected){
		Point2D source = states.get(this.sourceParent).getPoint();
		Point2D des = states.get(this.destinationParent).getPoint();
		Point2D mid2SourceDes = midpoint(source, des);

		ctrl1  = (ctrl1 == null)? new Point2D.Float((int)midpoint(source, mid2SourceDes).getX(), (int)midpoint(source, mid2SourceDes).getY()): ctrl1;
		ctrl2  = (ctrl2  == null)? new Point2D.Float((int)midpoint(mid2SourceDes, des).getX(), (int)midpoint(mid2SourceDes, des).getY()): ctrl2;
		drawEdge(g2d, source, des, labelFont , selected);
	}
	
	public void drawEdgeImp(Graphics2D g2d, Point2D source, Point2D ctrl1, Point2D ctrl2, Point2D des, boolean selected){
		Point2D mid = midpoint(source, des);
		this.ctrl1  =  (ctrl1 == null)? midpoint(source, mid): ctrl1;
		this.ctrl2 =   (ctrl2 == null)? midpoint(mid, des) : ctrl2;
		drawEdge(g2d, source, des, null ,selected);
	}
	public void drawEdge(Graphics2D g2d, Point2D source, Point2D des, Label labelFont, boolean selected){
			
		Point2D mid2SourceDes = midpoint(source, des);
		Point2D mid2Ctr = des;
			
		Point2D mid;
		if (type.equals("art")) {
			
			mid2Ctr = midpoint(ctrl1, ctrl2);
			mid2SourceDes = midpoint(midpoint(source, des), mid2Ctr);
			Point2D midCtrl1 = midpoint(ctrl1, mid2Ctr);
			Point2D midCtrl2 = midpoint(ctrl2, mid2Ctr);
			
			g2d.draw(createArrowShape(source, midCtrl1, type));
			g2d.draw(createArrowShape(midCtrl2, des, type));

		} else {
			mid = midpoint(source, des);
			g2d.draw(createArrowShape(source, mid, type));
			g2d.draw(createArrowShape(mid, des, type));
		}
		if(type.equals("art")){
			drawCubiCurve(g2d, source, des, selected);
		}else
			g2d.draw(new Line2D.Float(source, des));
		
		if(labelFont != null){
			Color defaultColor = g2d.getColor();
			g2d.setColor(Color.BLUE);
			labelFont.setPoint(mid2SourceDes);
			labelFont.drawLabel(label.toString(), g2d);
			g2d.setColor(defaultColor);
		}
		
	}
	
	public void drawLine(Point2D source, Point2D des, Graphics2D g2d){
		
		g2d.draw(new Line2D.Float(source, des));
	}
	
	private void drawCubiCurve(Graphics2D g2d,Point2D source, Point2D des, boolean selected) {

		if (selected) {
			Stroke currentStroke = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10f, dashes, 0f));
			g2d.draw(new Line2D.Float(source, des));
			g2d.draw(new Line2D.Float(source, ctrl1));
			g2d.draw(new Line2D.Float(des, ctrl2));
			/*Point2D mid = midpoint(ctrl1, ctrl2);
			Point2D midCtrl1 = midpoint(ctrl1, mid);
			Point2D midCtrl2 = midpoint(ctrl2, mid);
			g2d.draw(new Line2D.Float(source, midCtrl1));
			g2d.draw(new Line2D.Float(des, midCtrl2));*/

			g2d.setStroke(currentStroke);
		}

		CubicCurve2D cubi = new CubicCurve2D.Float((int) source.getX(), (int) source.getY(), (int) ctrl1.getX(),
				(int) ctrl1.getY(), (int) ctrl2.getX(), (int) ctrl2.getY(), (int) des.getX(), (int) des.getY());
		
		g2d.draw(cubi);

		if (selected) {
			g2d.setColor(Color.RED);
			g2d.fill(new Ellipse2D.Float((int) source.getX() - 5, (int) source.getY() - 5, 5 * 2, 5 * 2));
			g2d.fill(new Ellipse2D.Float((int) des.getX() - 5, (int) des.getY() - 5, 5 * 2, 5 * 2));
			g2d.fill(new Ellipse2D.Float((int) ctrl1.getX() - 5, (int) ctrl1.getY() - 5, 5 * 2, 5 * 2));
			g2d.fill(new Ellipse2D.Float((int) ctrl2.getX() - 5, (int) ctrl2.getY() - 5, 5 * 2, 5 * 2));
		}
	}
	
		
	public void selected(Graphics2D g2d, List<Part_1.Shape> states){
		Point2D source = states.get(this.sourceParent).getPoint();
		Point2D des = states.get(this.destinationParent).getPoint();
		g2d.setColor(Color.YELLOW);
		Ellipse2D cirSource = new Ellipse2D.Double(source.getX() - 5, source.getY() - 5, 10, 10);
		g2d.fill(cirSource);
		Ellipse2D cirDes = new Ellipse2D.Double(des.getX() - 5, des.getY() - 5, 10, 10);
		g2d.fill(cirDes);
	}
}
