package graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopupMenu {
	private GraphComponent com;
	int currentLine;
	int currently;
	
	public PopupMenu(GraphComponent com){
		this.com = com;
	}
	public void popupMenu(JPopupMenu popup) {
		currentLine = com.getCurrentLine();
		currently = com.getCurrently();
		// New project menu item
		JMenuItem menuItem = new JMenuItem("Delete");
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.getAccessibleContext().setAccessibleDescription("Delete");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (com.getCurrentLine() > -1) {
					com.getListPoints().get(com.getListLine().get(currentLine).getsourceParent()).setHaveLine(false);
					com.getListPoints().get(com.getListLine().get(currentLine).getsourceParent()).setLineIndex(-1);
					com.getListPoints().get(com.getListLine().get(currentLine).getdestinationParent()).setHaveLine(false);
					com.getListPoints().get(com.getListLine().get(currentLine).getdestinationParent()).setLineIndex(-1);
					com.getListLine().remove(currentLine);
					com.getTransitions().remove(currentLine);
				} else {
					if (com.getListPoints().get(currently).isHaveLine()) {
						for (int i = com.getListLine().size() - 1; i >= 0; i--) {

							if (com.getListLine().get(i).getsourceParent() == currently
									|| com.getListLine().get(i).getdestinationParent() == currently) {
								com.getListLine().remove(i);
								com.getTransitions().remove(i);
							}
						}
						for (int j = 0; j < com.getListLine().size(); j++) {
							if (com.getListLine().get(j).getsourceParent() > currently) {
								int dp = com.getListLine().get(j).getdestinationParent() - 1;
								com.getListLine().get(j).setsourceParent(dp);
							}
							if (com.getListLine().get(j).getdestinationParent() > currently) {
								int dp = com.getListLine().get(j).getdestinationParent() - 1;
								com.getListLine().get(j).setdestinationParent(dp);
							}
						}
					}
					com.getListPoints().remove(currently);
				}
				currently = currentLine = -1;
				com.setCurrently(currently);				
				com.setCurrentLine(currentLine);	
				com.repaint();
			}
		});
		popup.add(menuItem);

		// show menu start and end state when select state
		if (currentLine == -1) {
			// New File menu item
			menuItem = new JMenuItem("Initial State");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					// remove start of current state
					if (com.getStartState() > -1)
						com.getListPoints().get(com.getStartState()).getState().setInitial(false);
					com.setStartState(currently);
					// set start state for new state
					com.getListPoints().get(currently).getState().setInitial(true);
					com.repaint();
				}
			});
			popup.add(menuItem);

			// New File menu item
			menuItem = new JMenuItem("Terminal State");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (!com.getEndState().contains(currently))
						com.getEndState().add(currently);
					com.getListPoints().get(currently).getState().setTerminal(true);
					com.repaint();
				}
			});
			popup.add(menuItem);

			// New File menu item
			menuItem = new JMenuItem("Normal State");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (com.getEndState().contains(currently))
						com.getEndState().remove(com.getEndState().indexOf(currently));
					com.getListPoints().get(currently).getState().setTerminal(false);
					com.repaint();
				}
			});
			popup.add(menuItem);
		} else {
			// New File menu item
			menuItem = new JMenuItem("Edit label");
			menuItem.setMnemonic(KeyEvent.VK_F);
			menuItem.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					com.setCurrently(com.getListLine().get(currentLine).getsourceParent());
					com.setCurrentlyMove(com.getListLine().get(currentLine).getdestinationParent());
					// Edit lable
					String newLable = com.InputLabel(com.getListLine().get(currentLine).getLabel(), "Enter lable", "Enter label", 1);
					if (!newLable.equals("")){
						com.getListLine().get(currentLine).setLabel(newLable);
						com.getTransitions().get(currentLine).setLabel(newLable);
					}
					com.repaint();
				}
			});
			popup.add(menuItem);
		}
	}
}
