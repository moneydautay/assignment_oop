package graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.MouseInputListener;

import machine.ChangeTransition;
import machine.ObservableAutomaton;
import machine.StateImpl;
import machine.Transition;
import machine.TransitionImpl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class GraphComponent extends JComponent implements MouseInputListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = new Font("Serif", Font.BOLD, 20);
	private static Label labelFont = new Label(FONT);
	private static final Color DELINE_COLOR_AUTOMATA = Color.GREEN;
	private List<Shape> listPoints = new ArrayList<Shape>();
	private List<Edge> listLine = new ArrayList<Edge>();
	private ArrayList<Shape> tempState = new ArrayList<Shape>();
	private List<Integer> dectectAutomata = new ArrayList<Integer>();
	private int startState = -1;
	private List<Integer> endState;
	private static int radius = 30;
	private int dx = 0;
	private int dy = 0;
	private int currently = -1;
	private int currentLine = -1;
	private int currentlyMove = -1;
	private boolean altPressed = false;
	private Point2D sourcePoint;
	private Point2D desPoint;
	private int currentCtrl = -1;
	private boolean movableCtrl = false;
	private boolean selectedCubi = false;
	private Point2D ctrlPoint1 = null;
	private Point2D ctrlPoint2 = null;
	private int countSpace = 0;
	private Point2D currentPointState;

	// current button to draw Rectangles or Circles or Squares
	private String currentButton = null;
	private ArrayList<Transition<String>> transitions = new ArrayList<Transition<String>>();

	public GraphComponent(int size) {
		setPreferredSize(new Dimension(size, size));
		setFocusable(true);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		desPoint = new Point2D.Float();
		endState = new ArrayList<Integer>();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		pain(g);
	};

	protected void pain(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		Color defaultColor = g2d.getColor();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// draw line
		int index = 0;
		for (Edge edge: listLine) {
			boolean selected = (currentLine == index) ? true : false;	
			
			if(dectectAutomata.size() > 0)
				if(dectectAutomata.contains(index)){
					g2d.setColor(DELINE_COLOR_AUTOMATA);
				}
			edge.drawEdge(g2d, listPoints, labelFont ,selected);
			g2d.setColor(defaultColor);
			index++;
		}

		// draw shape
		int labelCount = 0;
		for(Shape p: listPoints){
			String labelNumber = Integer.toString(labelCount);

			g2d.setColor(Color.BLUE);
			if (labelCount == startState)
				g2d.setColor(Color.RED);

			if (endState.contains(labelCount))
				g2d.setColor(Color.BLACK);
			
			if (labelCount == startState && endState.contains(labelCount))
				g2d.setColor(Color.ORANGE);
					
			p.drawState(g2d, labelNumber, labelFont);
			g2d.setColor(defaultColor);
			labelCount++;
		}

		// draw line while mouse move
		if (currently > -1) {
			if ((altPressed)) {
				Edge edge = new Edge(currentButton);
				edge.drawEdgeImp(g2d, sourcePoint,ctrlPoint1, ctrlPoint2,desPoint , true);
			}
		}
		
		if(currentButton == "state"){
			String label = listPoints.size()+"";
			g2d.setColor(Color.BLUE);
			Shape shape = new Shape(currentButton, currentPointState, radius);
			shape.drawState(g2d, label, labelFont);
		}

		if(currentLine > -1)
			listLine.get(currentLine).selected(g2d, listPoints);
		
		//clear dectectAutomata after repaint
		if(dectectAutomata.size() > 0)
			dectectAutomata.clear();
	}
    
	public boolean checkPointIsCircle(MouseEvent e) {

		Iterator<Shape> listIterator = listPoints.iterator();
		int index = 0;
		while (listIterator.hasNext()) {

			Shape p = listIterator.next();
			boolean founded = false;
			if (((Ellipse2D) p.getBounds()).contains(e.getPoint())) {
				founded = true;
			}
			if (founded) {
				currently = index;
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
			Shape p = listIterator.next();
			if (((Ellipse2D) p.getBounds()).contains(point)) {
				currentlyMove = index;
				return true;
			}
			index++;
		}
		return false;
	}

	private boolean findLine(MouseEvent e) {
		
		int index = 0;
		for (Edge edge: listLine) {
			Line2D line = new Line2D.Float(listPoints.get(edge.getsourceParent()).getPoint(),
					listPoints.get(edge.getdestinationParent()).getPoint());
			if (line.ptSegDist(e.getPoint()) <= 2.0) {
				currentLine = index;
				return true;
			}
			index++;
		}
		return false;
	}

	private void drawShape(int x, int y) {
		addShape(x, y);
		currently = listPoints.size() - 1;
		if(startState == -1)
			startState = currently;
		boolean st = (currently == startState)? true : false;
		listPoints.get(currently).setState(new StateImpl(st, false));
	}

	private void addShape(int x, int y) {
		Shape newShape = new Shape("state", new Point(x, y), radius);
		listPoints.add(newShape);
	}

	public void setCurrentButton(String currentButton) {
		this.currentButton = currentButton;
	}
	
	public String getCurrentButton() {
		return this.currentButton;
	}


	@Override
	public void mousePressed(MouseEvent e) {
		requestFocusInWindow();
		currently = -1;
		currentLine = -1;

		if (checkPointIsCircle(e) || altPressed) {
			dx = e.getX() - (int) listPoints.get(currently).getPoint().getX();
			dy = e.getY() - (int) listPoints.get(currently).getPoint().getY();
			sourcePoint = e.getPoint();
		} else if (findLine(e)) {
			repaint();
		} else if (isCubiCurve(e)) {
			selectedCubi = true;
			repaint();
		}

		if (selectedCubi) {
			if (isAnchorCtrl(e)) {
				movableCtrl = true;
			}
		}
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (currentButton == "state") {
				drawShape(e.getX(), e.getY());
				repaint();
			}
		}

		if (e.getButton() == MouseEvent.BUTTON3) {
			if (currently > -1 || currentLine > -1) showPopup(e);
			else{ 
				currentButton = null;
				repaint();
			}
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON3) {
			if (currently > -1 || currentLine > -1) showPopup(e);
			else currentButton = null;
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
			if (currently > -1 && (currentButton == "line" || currentButton == "art")) {

				// add label for line
				String label = InputLabel("", "Enter label", "Enter label", 1);
				if (!label.isEmpty() && label != null) {
					if (!checkPointIsCircle(desPoint)) {
						e.getPoint();
						int x = e.getX();
						int y = e.getY();
						addShape(x, y);
						currentlyMove = listPoints.size() - 1;
					}
					listLine.add(new Edge(currently, currentlyMove, label, currentButton, ctrlPoint1, ctrlPoint2));
					transitions.add(new TransitionImpl<String>(
										listPoints.get(currently).getState(), 
										listPoints.get(currentlyMove).getState(), label)
								);

					listPoints.get(currently).setHaveLine(true);
					listPoints.get(currentlyMove).setHaveLine(true);
					currentLine = listLine.size() - 1;
				}
				currentButton = null;
				ctrlPoint1 = ctrlPoint2 = null;
				countSpace = 0;
				selectedCubi = true;
				repaint();
			}
		}
		if (movableCtrl) {
			movableCtrl = false;
			currentCtrl = -1;
		}
		altPressed = false;
		tempState.clear();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (currently > -1) {
			if (currentButton == "line" || currentButton == "art") {
				desPoint = e.getPoint();
			} else {
				listPoints.get(currently).getPoint().setLocation(x - dx, y - dy);
			}
		}
		if (movableCtrl) {
			if (currentCtrl == 1)
				listLine.get(currentLine).setCtrl1(e.getPoint());
			else
				listLine.get(currentLine).setCtrl2(e.getPoint());
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.isAltDown()) {
			altPressed = true;
			currentButton = "line";
		}
		
		if(altPressed){
			if (e.getKeyCode() == KeyEvent.VK_SPACE){
				currentButton = "art";
				if(countSpace == 0){
					ctrlPoint1 = new Point2D.Float((int)desPoint.getX(), (int)desPoint.getY());
					ctrlPoint2 = new Point2D.Float((int)desPoint.getX(), (int)desPoint.getY());
					countSpace++;
				}else if(countSpace == 1){
					ctrlPoint2 = new Point2D.Float((int)desPoint.getX(), (int)desPoint.getY());
					countSpace++;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!e.isAltDown()) {
			//altPressed = false;
		}
	}
	
	private boolean isAnchorCtrl(MouseEvent e) {
		int index = 0;
		for (Edge edge : listLine) {
			if (edge.isAnchorCtrl(e, 1)) {
				currentLine = index;
				currentCtrl = 1;
				return true;
			} else if (edge.isAnchorCtrl(e, 2)) {
				currentLine = index;
				currentCtrl = 2;
				return true;
			}
			index++;
		}
		return false;
	}

	private boolean isCubiCurve(MouseEvent e) {
		int index = 0;
		for (Edge edge : listLine) {
			Point2D source = listPoints.get(edge.getsourceParent()).getPoint();
			Point2D des = listPoints.get(edge.getdestinationParent()).getPoint();
			CubicCurve2D cubi = new CubicCurve2D.Float((int) source.getX(), (int) source.getY(),
					(int) edge.getCtrl1().getX(), (int) edge.getCtrl1().getY(), (int) edge.getCtrl2().getX(),
					(int) edge.getCtrl2().getY(), (int) des.getX(), (int) des.getY());
			if (cubi.contains(e.getPoint())) {
				currentLine = index;
				return true;
			}
			index++;
		}
		return false;
	}

	

	public void recognizeWords() {
		dectectAutomata.clear();
		
		if(endState.size() == 0){
			JOptionPane.showMessageDialog(this, "This automata has not terminal state. Please using right mouse to set terminal state.", "Error Automata", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String words = InputRecognizedWord();
		
		if(words != ""){
			String[] word = words.split("");
			ChangeTransition ct = new ChangeTransition(this);
	
			try {
				ObservableAutomaton<String> obs = new ObservableAutomaton<String>(transitions);
				obs.addObserver(ct);
				if (obs.recognize(word))
					JOptionPane.showMessageDialog(this ,"This words was accepted!", "Automata messgage", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(this ,"This words was not accepted!","Automata messgage" ,JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showPopup(MouseEvent e) {

		if (e.isPopupTrigger()) {
			JPopupMenu popup = new JPopupMenu();
			PopupMenu pp = new PopupMenu(this);
			pp.popupMenu(popup);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private String InputRecognizedWord(){
		String words = InputWord("", "Enter regconized words",  "Enter regconized words", 100);
		if(words != "")
			return words;
		return "";
	}

	private String InputWord(String lb, Object message, String title, int thresshold) {
		String label = (String) JOptionPane.showInputDialog(this, message , title,
				JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE, null, null,  lb);
		if (label == null)
			return "";
		else if (label.equals(""))
			label = InputLabel(lb, message, title, thresshold);
		else if(label.length() > thresshold){
			JOptionPane.showMessageDialog(this, "Label length is not greater than "+thresshold+"!");
			label = InputLabel(lb, message, title, thresshold);
		}
		return label;
	}

	public String InputLabel(String lb, Object message, String title, int thresshold) {
		String label = InputWord(lb, message, title, thresshold);
		if (label != "") {
			for (Edge edge : listLine) {
				if (edge.getsourceParent() == currently && edge.getLabel().equals(label)) {
					JOptionPane.showMessageDialog(this, "Label " + label + " existed on this edge.", "Error",
							JOptionPane.ERROR_MESSAGE);
					label = InputLabel(lb, message, title, thresshold);
				}
			}
			return label;
		}
		return "";
	}

	public List<Shape> getListPoints() {
		return listPoints;
	}

	public void setListPoints(List<Shape> listPoints) {
		this.listPoints =  new ArrayList<Shape>(listPoints);
	}

	public List<Edge> getListLine() {
		return listLine;
	}

	public void setListLine(List<Edge> listLine) {
		this.listLine = new ArrayList<Edge>(listLine);
	}

	public int getStartState() {
		return startState;
	}

	public void setStartState(int startState) {
		this.startState = startState;
	}

	public List<Integer> getEndState() {
		return endState;
	}
	
	public  List<Integer> getDectectAutomata(){
		return dectectAutomata;
	}
	
	public void setDectectAutomataItem(Integer dectectAutomataItem){
		this.dectectAutomata.add(dectectAutomataItem);
	}
	
	public void setEndState(List<Integer> endState) {
		this.endState = endState;
	}
	
	public void setTransition(ArrayList<Transition<String>> transitions){
		this.transitions = transitions;
	}

	public ArrayList<Transition<String>> getTransitions() {
		return transitions;
	}
	public Point2D getCodinateMouseMove(){
		return currentPointState;
	}
	
	public int getCurrently(){
		return this.currently;
	}
	public int getCurrentLine() {
		return currentLine;
	}
	public void setCurrentLine(int currentLine) {
		this.currentLine = currentLine;
	}
	public void setCurrently(int currently) {
		this.currently = currently;
	}
	public void setCurrentlyMove(int currentMove) {
		this.currentlyMove = currentMove;
	}
	
	public void cleanGraph() {
		this.listPoints.clear();
		this.listLine.clear();
		this.endState.clear();
		this.tempState.clear();
		this.transitions.clear();
		this.dectectAutomata.clear();
		this.startState = -1;
		this.currently = -1;
		this.currentLine = -1;
		this.currentCtrl = -1;
		this.movableCtrl = false;
		this.currentlyMove = -1;
		this.currentButton = "";
	}

	public void selectedPoint() {
		this.currentButton = "";
		this.currently = -1;
		this.currentLine = -1;
		this.currentCtrl = -1;
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		if(currentButton=="state"){
			currentPointState = e.getPoint();
			repaint();
		}
	}
	
	public void setAltPressed(boolean altPressed) {
		this.altPressed = altPressed;
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void move(Graphics g) {}
	public void keyTyped(KeyEvent arg0) {}
}
