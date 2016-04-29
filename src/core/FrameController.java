package core;

import javax.swing.JFrame;

public interface FrameController {
	public void quit();
	public JFrame createFrame();
	public void deleteFrame(JFrame frame);
}
