package Part_1;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;

public class GraphFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3305426536904044526L;
	public static final String MENU_FILE = "File";
	public static final String MENU_FILE_NEW = "New";
	public static final String MENU_EXPORT = "Export";
	public static final String MENU_IMPORT = "Import";
	public static final String MENU_FILE_CLOSE = "Close";
	public static final String DIALOG_QUIT_TITLE = "Eixt Graphic Editor";
	public static final String DIALOG_QUIT_MSG = "Do you want exit this Graphic Editor?";
	public static String PATH_FILE = "D:\\res.xml";
	private static final String PATH_IMAGE = "images/";
	private static final String PATH_IMAGE_POINTER = "pointer.png";
	private static final String PATH_IMAGE_LINE = "line.png";
	private static final String PATH_IMAGE_ARC = "arc.png";
	private static final String PATH_IMAGE_STATE = "state.png";
	private static final Color DEFAULT_COLOR_BUTTON = Color.WHITE;

	private int size = 800;
	private GraphComponent com = new GraphComponent(size);
	private FrameController controller;
	private FileXML file;
	private JFileChooser fileChoise;
	
	public GraphFrame(FrameController controller, String title) {
		super(title);
		try {
			this.controller = controller;

			// set resizable is false
			setResizable(false);
			setLayout(new BorderLayout());

			menuBar();

			JScrollPane scrollPane = new JScrollPane(com);
			scrollPane.setPreferredSize(new Dimension(600, 600));
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setViewportView(com);

			JPanel leftPN = new JPanel();
			
			// add tool bar
			GridLayout layout = new GridLayout(5, 1);
			leftPN.setLayout(layout);
	
			Image img = ImageIO.read(getClass().getResource(PATH_IMAGE+PATH_IMAGE_POINTER));
			JButton pointButton = new JButton(new ImageIcon(img));
			
			img = ImageIO.read(getClass().getResource(PATH_IMAGE+PATH_IMAGE_STATE));
			JButton cirButton = new JButton(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource(PATH_IMAGE+PATH_IMAGE_LINE));
			JButton lineButton = new JButton(new ImageIcon(img));
			img = ImageIO.read(getClass().getResource(PATH_IMAGE+PATH_IMAGE_ARC));
			JButton artButton = new JButton(new ImageIcon(img));
			JButton delButton = new JButton("Rec Words");
			
			Color defaultColorButton = pointButton.getBackground();
			
			
			pointButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cirButton.setBackground(defaultColorButton);
					lineButton.setBackground(defaultColorButton);
					artButton.setBackground(defaultColorButton);
					pointButton.setBackground(DEFAULT_COLOR_BUTTON);
					com.selectedPoint();
				}
			});
			
			
			cirButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					lineButton.setBackground(defaultColorButton);
					artButton.setBackground(defaultColorButton);
					pointButton.setBackground(defaultColorButton);
					cirButton.setBackground(DEFAULT_COLOR_BUTTON);
					com.setCurrentButton("state");
				}
			});
			
			
			lineButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					artButton.setBackground(defaultColorButton);
					pointButton.setBackground(defaultColorButton);
					cirButton.setBackground(defaultColorButton);
					lineButton.setBackground(DEFAULT_COLOR_BUTTON);
					com.setCurrentButton("line");
				}
			});
			
			
			artButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					pointButton.setBackground(defaultColorButton);
					cirButton.setBackground(defaultColorButton);
					lineButton.setBackground(defaultColorButton);
					artButton.setBackground(DEFAULT_COLOR_BUTTON);
					com.setCurrentButton("art");
				}
			});
			
			delButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					com.recognizeWords();

				}
			});

			leftPN.add(pointButton);
			leftPN.add(cirButton);
			leftPN.add(lineButton);
			leftPN.add(artButton);
			leftPN.add(delButton);
			add(leftPN);
			add(scrollPane, BorderLayout.EAST);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		
		// button export 
		createMenuItem(menu, GraphFrame.MENU_EXPORT, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChoise = new JFileChooser(PATH_FILE);
				
				FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("*.xml", "xml");
		        // add filters
				fileChoise.addChoosableFileFilter(xmlFilter);
				fileChoise.setFileFilter(xmlFilter);
		        
				if(fileChoise.showSaveDialog(GraphFrame.this) == JFileChooser.APPROVE_OPTION){

					String fileTemp = fileChoise.getSelectedFile().toString();
					PATH_FILE = !fileTemp.endsWith("xml") ? (fileTemp+".xml"): fileTemp;
					file = new FileXML();
					file.setListStates(com.getListPoints());
					file.setListEdges(com.getListLine());
					file.setStartState( com.getStartState());
					file.setEndState( com.getEndState());
					if(file.exportXml(PATH_FILE)){
						System.out.println("Export file success");
					}
				}
			}
		});
		
		// button import 
		createMenuItem(menu, GraphFrame.MENU_IMPORT, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				file = new FileXML();
				if(file.importXML(PATH_FILE)){
					
					com.cleanGraph();
					com.setStartState(file.getStartState());
					com.setEndState(file.getEndState());
					System.out.println(file.getListStates().size());
					com.setListPoints(file.getListStates());
					com.setListLine(file.getListEdges());
					com.repaint();
					System.out.println("Import file success");
				}
				
				
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
