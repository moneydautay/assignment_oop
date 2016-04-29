package core;

import graph.GraphComponent;

public interface FileXML {
	
	public void addGraphComponent(GraphComponent com);
	public boolean importXML(String path_flie);
	public boolean exportXML(String path_flie);
}
