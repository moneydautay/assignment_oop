package Path_3_1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3305426536904044526L;
	public static final String MENU_FILE = "File";
	public static final String MENU_FILE_NEW = "New";
	public static final String MENU_FILE_CLOSE = "Close";
	public static final String DIALOG_QUIT_TITLE = "Eixt Graphic Editor";
	public static final String DIALOG_QUIT_MSG = "Do you want exit this Graphic Editor?";

	private int size = 800;
	private GraphComponent com = new GraphComponent(size);
	private FrameController controller;
	
	public GraphFrame(FrameController controller, String title) {
		super(title);
		this.controller = controller;
				
		// set resizable is false
		setResizable(false);
		setLayout(new BorderLayout());

		menuBar();
				
		JScrollPane scrollPane = new JScrollPane(com);
		scrollPane.setPreferredSize(new Dimension(800, 800));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(com);
				
		JPanel leftPN = new JPanel();

		// add tool bar
		GridLayout layout = new GridLayout(3, 1);
		leftPN.setLayout(layout);
	
		
		JButton cirButton = new JButton("State");
		cirButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				com.setCurrentButton("state");
			}
		});
		JButton squButton = new JButton("Line");
		squButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				com.setCurrentButton("line");
			}
		});
		
		leftPN.add(cirButton);
		leftPN.add(squButton);
		add(leftPN);
		add(scrollPane, BorderLayout.EAST);
	}
	
	/**
	 * function execute draw shapes defend on button is 
	 * Rectangle or Circle or Square. if Rectangle we 
	 * will raw rectangles, else if Circle raw circles
	 * else draw Squares.
	 * 
	 */
	
	private void menuBar() {

		// Menu
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu(GraphFrame.MENU_FILE);
		menuBar.add(menu);

		// button new
		createMenuItem(menu, GraphFrame.MENU_FILE_NEW, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GraphFrame.this.controller.createFrame();
			}
		});

		// button close frame
		createMenuItem(menu, GraphFrame.MENU_FILE_CLOSE, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				GraphFrame.this.controller.deleteFrame(GraphFrame.this);
			}
		});

		// close window browser
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				GraphFrame.this.controller.deleteFrame(GraphFrame.this);
			}
		});
	}

	public void createMenuItem(JMenu menu, String name, ActionListener action) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.addActionListener(action);
		menu.add(menuItem);
	}
}
