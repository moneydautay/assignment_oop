package Path_3_1;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class App implements FrameController{
	
	public static final String MENU_FILE = "File";
	public static final String MENU_FILE_NEW = "New";
	public static final String MENU_FILE_CLOSE = "Close";
	public static final String DIALOG_QUIT_TITLE = "Eixt Graphic Editor";
	public static final String DIALOG_QUIT_MSG = "Do you want exit this Graphic Editor?";
	
	private static final List<JFrame> frames = new ArrayList<JFrame>();
	
	public void quit() {
		int answer = JOptionPane.showConfirmDialog(null, DIALOG_QUIT_MSG, DIALOG_QUIT_TITLE, JOptionPane.YES_NO_OPTION);
		if(answer == JOptionPane.YES_OPTION){
			System.exit(0);
		}
	}

	public JFrame createFrame() {
		GraphFrame frame = new GraphFrame(this, MENU_FILE_NEW);
		int pos = 30 * (frames.size() % 5);
		frame.setLocation(pos,pos);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frames.add(frame);
		return frame;
	}

	public void deleteFrame(JFrame frame) {
		if(frames.size() > 1){
			frames.remove(frame);
			frame.dispose();
		}else{
			quit();
		}
	}
	
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				new App().createFrame();
			}
		});
	}
}
