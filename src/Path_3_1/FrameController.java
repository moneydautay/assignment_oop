package Path_3_1;

import javax.swing.JFrame;

public interface FrameController {
	public void quit();
	public JFrame createFrame();
	public void deleteFrame(JFrame frame);
}
