package Part_1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.Observer;

import javax.swing.event.MouseInputListener;

import java.util.Observable;
import machine.ObservableAutomaton;
import machine.StateImpl;
import machine.Transition;
import machine.TransitionImpl;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class GraphComponent extends JComponent implements MouseInputListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = new Font("Serif", Font.BOLD, 20);
	private static Label labelFont = new Label(FONT);
	private static final Color DELINE_COLOR_AUTOMATA = Color.GREEN;
	private static final Color DESTATE_COLOR_AUTOMATA = Color.DARK_GRAY;
	private List<Shape> listPoints = new ArrayList<Shape>();
	private List<Edge> listLine = new ArrayList<Edge>();
	private ArrayList<Shape> tempState = new ArrayList<Shape>();
	private List<Integer> dectectAutomata = new ArrayList<Integer>();
	private static int radius = 30;
	private int dx = 0;
	private int dy = 0;
	private int startState = 0;
	private List<Integer> endState;

	// index of circle have been clicked or dragged
	private int currently = -1;
	private int currentLine = -1;
	private int currentlyMove = -1;
	private boolean altPressed = false;
	
	private Point2D sourcePoint;
	private Point2D desPoint;
	private int currentCtrl = -1;
	private boolean movableCtrl = false;
	private boolean selectedCubi = false;
	
	
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
			
			/*if(dectectAutomata.size() > 0){
				for(Integer i : dectectAutomata)
					if(listLine.get(i).getsourceParent() == labelCount || listLine.get(i).getdestinationParent() == labelCount)
						g2d.setColor(DESTATE_COLOR_AUTOMATA);
			}*/
			
			p.drawState(g2d, labelNumber, labelFont);
			g2d.setColor(defaultColor);
			labelCount++;
		}

		// draw line while mouse move
		if (currently > -1) {
			if ((altPressed)) {
				Edge edge = new Edge(currentButton);
				edge.drawEdgeImp(g2d, sourcePoint,desPoint , true);
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
		boolean st = (currently == startState)?true : false;
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

		if (checkPointIsCircle(e)) {
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
			else currentButton = null;
			
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
					listLine.add(new Edge(currently, currentlyMove, label, currentButton));
					transitions.add(new TransitionImpl<String>(
										listPoints.get(currently).getState(), 
										listPoints.get(currentlyMove).getState(), label)
								);

					listPoints.get(currently).setHaveLine(true);
					listPoints.get(currentlyMove).setHaveLine(true);
					currentLine = listLine.size() - 1;
				}
				currentButton = null;
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
		altPressed = false;
		int x = e.getX();
		int y = e.getY();

		if (currently > -1) {
			if (currentButton == "line" || currentButton == "art") {
				desPoint = e.getPoint();
				altPressed = true;
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
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				tempState.add(new Shape("circle", desPoint, 5));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!e.isAltDown()) {
			altPressed = false;
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

	public void popupMenu(JPopupMenu popup) {
		
		// New project menu item
		JMenuItem menuItem = new JMenuItem("Delete");
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.getAccessibleContext().setAccessibleDescription("Delete");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// listLine.get(currentLine).removeEdge(currentJoinPoint);
				// repaint();
				if (currentLine > -1) {
					listPoints.get(listLine.get(currentLine).getsourceParent()).setHaveLine(false);
					listPoints.get(listLine.get(currentLine).getsourceParent()).setLineIndex(-1);
					listPoints.get(listLine.get(currentLine).getdestinationParent()).setHaveLine(false);
					listPoints.get(listLine.get(currentLine).getdestinationParent()).setLineIndex(-1);
					listLine.remove(currentLine);
					transitions.remove(currentLine);
				} else {
					if (listPoints.get(currently).isHaveLine()) {
						for(int i =  listLine.size() - 1; i >= 0; i--){
							
							if(listLine.get(i).getsourceParent() == currently || listLine.get(i).getdestinationParent() == currently){
								System.out.println("currently "+currently);
								System.out.println(listLine.get(i).getsourceParent());
								System.out.println(listLine.get(i).getdestinationParent());
							
								listLine.remove(i);
								transitions.remove(i);
							}
						}
						for(int j = 0; j < listLine.size(); j++){
							if(listLine.get(j).getsourceParent() > currently ){
								int dp = listLine.get(j).getdestinationParent() - 1;
								listLine.get(j).setsourceParent(dp);
							}
							if(listLine.get(j).getdestinationParent() > currently){
								int dp = listLine.get(j).getdestinationParent() - 1;
								listLine.get(j).setdestinationParent(dp);
							}
						}
					}
					listPoints.remove(currently);
				}
				currently = currentLine = -1;
				repaint();
			}
		});
		popup.add(menuItem);

		// show menu start and end state when select state
		if (currentLine == -1) {
									
			// New File menu item
			menuItem = new JMenuItem("Start");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// remove start of current state
					listPoints.get(startState).getState().setInitial(false);
					startState = currently;
					// set start state for new state
					listPoints.get(currently).getState().setInitial(true);
					repaint();
				}
			});
			popup.add(menuItem);

			// New File menu item
			menuItem = new JMenuItem("End");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (!endState.contains(currently))
						endState.add(currently);
					listPoints.get(currently).getState().setTerminal(true);
					repaint();
				}
			});
			popup.add(menuItem);

			// New File menu item
			menuItem = new JMenuItem("Normal State");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (endState.contains(currently))
						endState.remove(endState.indexOf(currently));
					listPoints.get(currently).getState().setTerminal(false);
					repaint();
				}
			});
			popup.add(menuItem);
		}else{
			// New File menu item
			menuItem = new JMenuItem("Edit label");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					currently = listLine.get(currentLine).getsourceParent();
					currentlyMove =  listLine.get(currentLine).getdestinationParent();
					// Edit lable
					String newLable = InputLabel(listLine.get(currentLine).getLabel(),"Enter lable", "Enter label",1);
					if (!newLable.equals(""))
						listLine.get(currentLine).setLabel(newLable);
					repaint();
				}
			});
			popup.add(menuItem);
		}
	}

	public void recognizeWords() {
		dectectAutomata.clear();
		String words = InputRecognizedWord();
		if(words != ""){
			String[] word = words.split("");
			ChangTransitionImpl ct = new ChangTransitionImpl();
	
			try {
				ObservableAutomaton<String> obs = new ObservableAutomaton<String>(transitions);
				obs.addObserver(ct);
				if (obs.recognize(word))
					JOptionPane.showMessageDialog(this ,"This words was recognized!", "Automata messgage", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(this ,"This words was not recognized!","Automata messgage" ,JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class ChangTransitionImpl implements Observer{

		public void update(Observable obs, Object o) {
			Transition<?> t = (Transition<?>)o;
			
			//initial = true
			if(t.source().initial()){
				int i = 0;
				for(Edge edge : listLine){
					if(edge.getsourceParent() == startState && edge.getLabel() == t.label()){
						dectectAutomata.add(i);
						break;
					}
					i++;
				}
			}else{
				int sizeDA = dectectAutomata.size();
				int i = 0;
				for(Edge edge : listLine){
					if(edge.getsourceParent() ==  listLine.get(sizeDA - 1).getdestinationParent() && edge.getLabel() == t.label()){
						dectectAutomata.add(i);
						break;
					}
					i++;
				}
			}
			repaint();
		}	
	}
	
	private void showPopup(MouseEvent e) {

		if (e.isPopupTrigger()) {
			JPopupMenu popup = new JPopupMenu();
			popupMenu(popup);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	private String InputRecognizedWord(){
		String words = InputLabel("", "Enter regconized words",  "Enter regconized words", 100);
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

	private String InputLabel(String lb, Object message, String title, int thresshold) {
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

	public void setEndState(List<Integer> endState) {
		this.endState = endState;
	}
	
	public void setTransition(ArrayList<Transition<String>> transitions){
		this.transitions = transitions;
	}
	
	public Point2D getCodinateMouseMove(){
		return currentPointState;
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
	
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void move(Graphics g) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
}
