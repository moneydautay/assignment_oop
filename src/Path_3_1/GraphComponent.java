package Path_3_1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.MouseInputListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class GraphComponent extends JComponent implements MouseInputListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private List<Shape> listPoints = new ArrayList<Shape>();
	private List<Edge> listLine = new ArrayList<Edge>();
	private ArrayList<Shape> tempEdge = new ArrayList<Shape>();
	private static int radius = 30;
	private Point2D initialPoint;
	private int dx = 0;
	private int dy = 0;

	// index of circle have been clicked or dragged
	private int currently = -1;
	private int currentJoinPoint = -1;
	private int currentLine = -1;
	private int currentlyMove = -1;
	private int creatly = -1;
	private boolean altPressed = false;
	private boolean spacePressed = false;
	private boolean movableLinePoint = false;
	private boolean movableJoinPoint = false;

	private Point2D sourcePoint;
	private Point2D desPoint;

	// current button to draw Rectangles or Circles or Squares
	private String currentButton = "rectangle";

	public GraphComponent(int size) {
		setPreferredSize(new Dimension(size, size));

		setFocusable(true);

		// trigger mouser listener
		addMouseListener(this);
		// trigger event move mouse
		addMouseMotionListener(this);

		addKeyListener(this);

		initialPoint = new Point2D.Float();
		desPoint = new Point2D.Float();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		pain(g);
	};

	protected void pain(Graphics g) {
		Font font = new Font("Serif", Font.BOLD, 20);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics(font);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// draw line
		Iterator<Edge> iteLine = listLine.iterator();
		while (iteLine.hasNext()) {
			Edge linePoint = iteLine.next();
			Point2D source = listPoints.get(linePoint.getSource()).getPointClick();
			Point2D des = listPoints.get(linePoint.getDestination()).getPointClick();
			String label = linePoint.getLabel();

			if (linePoint.getJoinPoint().size() > 0) {
				int index = linePoint.pointToRaw();

				Point2D desPoint;
				if (linePoint.getJoinPoint().size() == 1)
					desPoint = des;
				else
					desPoint = linePoint.getEdge(index + 1).getPoint();

				createArrowLabel(g2d, label, linePoint.getEdge(index).getPoint(), desPoint);
				g2d.setColor(Color.BLACK);
				g2d.fill(createArrowShape(linePoint.getEdge(index).getPoint(), desPoint));
			} else {
				createArrowLabel(g2d, label, source, des);
				g2d.setColor(Color.BLACK);
				g2d.fill(createArrowShape(source, des));
			}

			drawEdge(source, des, linePoint.getJoinPoint(), g2d);
		}

		// draw shape
		Iterator<Shape> listIterator = listPoints.iterator();
		int labelCount = 0;
		while (listIterator.hasNext()) {
			String labelNumber = Integer.toString(labelCount);
			Shape p = listIterator.next();
			int internalRadius = p.getRadius();
			int height = internalRadius * 2, width = internalRadius * 2;

			g2d.setColor(Color.BLUE);

			Ellipse2D ellipse = new Ellipse2D.Double((int) p.getPoint().getX() - internalRadius,
					(int) p.getPoint().getY() - internalRadius, width, height);
			g2d.fill(ellipse);

			int widthLable = fm.stringWidth(labelNumber);

			// center String/text
			int cx = ((int) p.getPoint().getX() - widthLable / 2);
			int cy = ((int) p.getPoint().getY() + fm.getHeight() / 4);
			g2d.setColor(Color.white);
			g2d.drawString(labelNumber, cx, cy);
			labelCount++;
		}

		// draw line while mouse move
		if (currently > -1) {
			if ((altPressed)) {
				drawEdge(sourcePoint, desPoint, tempEdge, g2d);
			}
		}
	}

	private void drawEdge(Point2D source, Point2D des, List<Shape> listEdge, Graphics2D g2d) {
		Point2D nextPoint = source;
		if (listEdge.size() > 0) {
			Iterator<Shape> edge = listEdge.iterator();
			while (edge.hasNext()) {
				Point2D point = edge.next().getPoint();
				drawLine(nextPoint, point, g2d);
				nextPoint = point;
			}
		}
		drawLine(nextPoint, des, g2d);
	}

	private void drawLine(Point2D source, Point2D des, Graphics2D g2d) {
		Line2D line = new Line2D.Float(source, des);
		g2d.setColor(Color.BLACK);
		g2d.draw(line);
		g2d.setColor(Color.RED);
		Ellipse2D cirSource = new Ellipse2D.Double(source.getX() - 5, source.getY() - 5, 10, 10);
		g2d.fill(cirSource);
		Ellipse2D cirDes = new Ellipse2D.Double(des.getX() - 5, des.getY() - 5, 10, 10);
		g2d.fill(cirDes);
	}

	public boolean checkPointIsCircle(MouseEvent e) {

		Iterator<Shape> listIterator = listPoints.iterator();
		int index = 0;
		while (listIterator.hasNext()) {
			currently = index;
			Shape p = listIterator.next();
			boolean founded = false;
			if (((Ellipse2D) p.getBounds()).contains(e.getPoint())) {
				founded = true;
			}
			if (founded) {
				if (p.isCirclePointSelected().contains(e.getPoint()))
					movableLinePoint = true;

				return true;
			}
			index++;
		}
		return false;
	}

	public boolean checkPointIsCircle(Point2D point) {

		Iterator<Shape> listIterator = listPoints.iterator();
		int index = 0;
		while (listIterator.hasNext()) {
			currentlyMove = index;
			Shape p = listIterator.next();
			if (((Ellipse2D) p.getBounds()).contains(point)) {
				return true;
			}
			index++;
		}
		return false;
	}

	public boolean findPointInLine(MouseEvent e) {

		Iterator<Edge> edges = listLine.iterator();
		int index = 0;
		while (edges.hasNext()) {
			currentLine = index;
			Edge edge = edges.next();
			if (isJoinPoint(edge.getJoinPoint(), e.getPoint())) {
				return true;
			}
			index++;
		}
		return false;
	}

	private boolean isJoinPoint(ArrayList<Shape> joinPoits, Point2D point) {
		Iterator<Shape> jpIter = joinPoits.iterator();
		int index = 0;
		while (jpIter.hasNext()) {
			currentJoinPoint = index;
			Shape sh = jpIter.next();
			if (((Ellipse2D) sh.getBounds()).contains(point)) {
				return true;
			}
			index++;
		}
		return false;
	}

	private void drawShape(int x, int y) {
		addShape(x, y);
		currently = listPoints.size() - 1;
		creatly = 1;
	}

	private void addShape(int x, int y) {

		Shape newShape = new Shape("circle", new Point(x, y), radius);

		listPoints.add(newShape);
	}

	public void setCurrentButton(String currentButton) {
		this.currentButton = currentButton;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			if (findPointInLine(e)) {
				listLine.get(currentLine).removeEdge(currentJoinPoint);
				repaint();
			} else if (checkPointIsCircle(e)) {

				if (currentLine > -1) {
					listPoints.get(currentLine).setHaveLine(false);
					listPoints.get(currentLine).setHaveLine(false);
					listLine.remove(currentLine);
					repaint();
				} else {
					listLine.contains(listPoints.get(currently).getLineIndex());
					listPoints.remove(currently);
					repaint();
				}
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		requestFocusInWindow();
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (findPointInLine(e)) {
				movableJoinPoint = true;
				dx = e.getX() - (int) listLine.get(currentLine).getEdge(currentJoinPoint).getPoint().getX();
				dy = e.getY() - (int) listLine.get(currentLine).getEdge(currentJoinPoint).getPoint().getY();
			} else if (checkPointIsCircle(e)) {
				dx = e.getX() - (int) listPoints.get(currently).getPoint().getX();
				dy = e.getY() - (int) listPoints.get(currently).getPoint().getY();
				sourcePoint = e.getPoint();
			} else {
				initialPoint = e.getPoint();
				int x = e.getX();
				int y = e.getY();
				drawShape(x, y);
				repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if ((altPressed || spacePressed) && currently > -1) {
			// listPoints.get(currently).setDxy(dx, dy);

			if (checkPointIsCircle(desPoint)) {
				int mdx = (int) (desPoint.getX() - (int) listPoints.get(currentlyMove).getPoint().getX());
				int mdy = (int) (desPoint.getY() - (int) listPoints.get(currentlyMove).getPoint().getY());
				listPoints.get(currentlyMove).setDxy(mdx, mdy);
			} else {
				initialPoint = e.getPoint();
				int x = e.getX();
				int y = e.getY();

				addShape(x, y);

				currentlyMove = listPoints.size() - 1;
				listPoints.get(currentlyMove).setDxy(0, 0);
			}

			// add label for line
			String label = JOptionPane.showInputDialog(this, "Enter Label", "Enter Label",
					JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);

			if (label != null && label != "") {

				listLine.add(new Edge(currently, currentlyMove, tempEdge, label));

				listPoints.get(currently).setHaveLine(true);
				listPoints.get(currently).setMovable(spacePressed);
				listPoints.get(currently).setLineIndex(currentLine);
				listPoints.get(currentlyMove).setHaveLine(true);
				listPoints.get(currentlyMove).setMovable(spacePressed);
			}
			repaint();
		}
		currently = -1;
		currentlyMove = -1;
		creatly = -1;
		currentLine = -1;
		movableLinePoint = false;
		movableJoinPoint = false;
		altPressed = false;
		tempEdge.clear();
	}

	@Override
	public void mouseDragged(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();

		if (currently > -1) {
			if (altPressed) {
				desPoint = e.getPoint();

			} else {

				listPoints.get(currently).getPoint().setLocation(x - dx, y - dy);
			}
		}
		if (movableJoinPoint) {
			listLine.get(currentLine).getEdge(currentJoinPoint).getPoint().setLocation(x - dx, y - dy);
		}

		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public void move(Graphics g) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.isAltDown()) {
			altPressed = true;
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				tempEdge.add(new Shape("circle", desPoint, 5));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!e.isAltDown()) {
			altPressed = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static java.awt.Shape createArrowShape(Point2D fromPt, Point2D toPt) {
		Polygon arrowPolygon = new Polygon();
		// arrowPolygon.addPoint(-6,1);
		arrowPolygon.addPoint(3, 1);
		arrowPolygon.addPoint(3, 3);
		arrowPolygon.addPoint(6, 0);
		arrowPolygon.addPoint(3, -3);
		arrowPolygon.addPoint(3, -1);
		// arrowPolygon.addPoint(-6,-1);

		Point2D midPoint = midpoint(fromPt, toPt);

		double rotate = Math.atan2(toPt.getY() - fromPt.getY(), toPt.getX() - fromPt.getX());

		AffineTransform transform = new AffineTransform();
		transform.translate(midPoint.getX(), midPoint.getY());
		double scale = 3; // 12 because it's the length of the arrow polygon.
		transform.scale(scale, scale);
		transform.rotate(rotate);

		return transform.createTransformedShape(arrowPolygon);
	}

	public static void createArrowLabel(Graphics2D g2d, String label, Point2D fromPt, Point2D toPt) {
		AffineTransform oldXForm = g2d.getTransform();

		Point2D midPoint = midpoint(fromPt, toPt);

		double rotate = Math.atan2(toPt.getY() - fromPt.getY(), toPt.getX() - fromPt.getX());

		AffineTransform transform = new AffineTransform();
		transform.translate(midPoint.getX(), midPoint.getY());
		transform.rotate(rotate);
		g2d.setColor(Color.BLUE);
		g2d.setTransform(transform);
		g2d.drawString(label, -10, -10);
		g2d.setTransform(oldXForm);

	}

	private static Point2D midpoint(Point2D fromPt, Point2D toPt) {
		return new Point2D.Double(fromPt.getX() + (toPt.getX() - fromPt.getX()) / 2,
				fromPt.getY() + (toPt.getY() - fromPt.getY()) / 2);
	}
}
