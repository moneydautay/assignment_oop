package Path_3_1;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Edge {
	
	//source and destination is index of shape in list shape
	private int source;
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	private int destination;
	private boolean movable;
	private String label;
	private ArrayList<Shape> joinPoint;
	
	Edge(int source, int destination, ArrayList<Shape> joinPoint, String label){
		this.source = source;
		this.destination = destination; 
		this.joinPoint =  (ArrayList<Shape>) joinPoint.clone();
		this.label = label;
	}
	
	public ArrayList<Shape> getJoinPoint() {
		return joinPoint;
	}
	
	public Shape getEdge(int index) {
		return joinPoint.get(index);
	}
	
	public Shape removeEdge(int index) {
		return joinPoint.remove(index);
	}

	Edge(int source, int destination, boolean movable){
		this.source = source;
		this.destination = destination; 
		this.movable = movable;
	}
	
	public boolean isMovable() {
		return movable;
	}
	
	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}
	

	public int pointToRaw(){
		int sizeEdge = joinPoint.size();
		int index = Math.round((sizeEdge-1)/2);
		
		return index;
	}
}
