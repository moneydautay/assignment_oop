package graph;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import core.FileXML;
import machine.StateImpl;
import machine.Transition;
import machine.TransitionImpl;

public class FileXMLImp implements FileXML{
	
	private File inputFile;
	private GraphComponent com;
	
	public FileXMLImp(GraphComponent com){
		this.com = com;
	}
	
	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
	}

	public boolean exportXML(String path_file){
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			// root element
			Element rootElement = doc.createElement("res");
			doc.appendChild(rootElement);
			
			// Start State element
			Element startstate = doc.createElement("startstate");
			startstate.appendChild(doc.createTextNode(com.getStartState()+""));
			rootElement.appendChild(startstate);
			
			// end State element
			Element endstate = doc.createElement("endstates");
			rootElement.appendChild(endstate);
			Element valueET;
			for (Integer es : com.getEndState()) {
				
				valueET = doc.createElement("endstate");
				valueET.appendChild(doc.createTextNode(es.toString()));
				endstate.appendChild(valueET);

			}
			
			//state element
			Element states = doc.createElement("states");
			rootElement.appendChild(states);
			Element state;
			//String type;
			//Point2D point;
			//boolean haveLine;
			//int lineIndex;
			//int radius;
			// setting attribute to element
			
			for (Shape point : com.getListPoints()) {
				
				state = doc.createElement("state");
				states.appendChild(state);
				
				Element valuePoint;
				valuePoint = doc.createElement("type");
				valuePoint.appendChild(doc.createTextNode(point.getType()));
				state.appendChild(valuePoint);
				
				valuePoint = doc.createElement("haveline");
				valuePoint.appendChild(doc.createTextNode(point.isHaveLine()+""));
				state.appendChild(valuePoint);
				
				valuePoint = doc.createElement("lineindex");
				valuePoint.appendChild(doc.createTextNode(point.getLineIndex()+""));
				state.appendChild(valuePoint);
				
				valuePoint = doc.createElement("x");
				valuePoint.appendChild(doc.createTextNode(Integer.toString((int)point.getPoint().getX())));
				state.appendChild(valuePoint);
				
				valuePoint = doc.createElement("y");
				valuePoint.appendChild(doc.createTextNode(Integer.toString((int)point.getPoint().getY())));
				state.appendChild(valuePoint);
				
				valuePoint = doc.createElement("initial");
				valuePoint.appendChild(doc.createTextNode(point.getState().initial()+""));
				state.appendChild(valuePoint);
				
				valuePoint = doc.createElement("terminal");
				valuePoint.appendChild(doc.createTextNode(point.getState().terminal()+""));
				state.appendChild(valuePoint);
			}
			
			Element edges = doc.createElement("edges");
			rootElement.appendChild(edges);
			Element edge;
			for (Edge line : com.getListLine()) {
				edge = doc.createElement("edge");
				edges.appendChild(edge);
			
				//String type;
				//String label;
				//int source;
				//int destination;
				//boolean movable;
				Element valueEdge;
				valueEdge = doc.createElement("type");
				valueEdge.appendChild(doc.createTextNode(line.getType()));
				edge.appendChild(valueEdge);
				
				valueEdge = doc.createElement("source");
				valueEdge.appendChild(doc.createTextNode(line.getsourceParent()+""));
				edge.appendChild(valueEdge);
				
				valueEdge = doc.createElement("des");
				valueEdge.appendChild(doc.createTextNode(line.getdestinationParent()+""));
				edge.appendChild(valueEdge);
				
				valueEdge = doc.createElement("label");
				valueEdge.appendChild(doc.createTextNode(line.getLabel()+""));
				edge.appendChild(valueEdge);
				
				valueEdge = doc.createElement("ctrl1");
				edge.appendChild(valueEdge);
				
				Element valueCtrl = doc.createElement("x");
				valueCtrl.appendChild(doc.createTextNode((int)line.getCtrl1().getX()+""));
				valueEdge.appendChild(valueCtrl);	
				
				valueCtrl = doc.createElement("y");
				valueCtrl.appendChild(doc.createTextNode((int)line.getCtrl1().getY()+""));
				valueEdge.appendChild(valueCtrl);
				
				valueEdge = doc.createElement("ctrl2");
				edge.appendChild(valueEdge);
				
				valueCtrl = doc.createElement("x");
				valueCtrl.appendChild(doc.createTextNode((int)line.getCtrl2().getX()+""));
				valueEdge.appendChild(valueCtrl);	
				
				valueCtrl = doc.createElement("y");
				valueCtrl.appendChild(doc.createTextNode((int)line.getCtrl2().getY()+""));
				valueEdge.appendChild(valueCtrl);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path_file));
			transformer.transform(source, result);						
						
		} catch (ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean importXML(String path_flie){
			
		try {
			List<Shape> listStates = new ArrayList<Shape>();
			List<Edge> listEdges = new ArrayList<Edge>();
			ArrayList<Transition<String>> transitions = new ArrayList<Transition<String>>();
			Integer startState;
			List<Integer> endState = new ArrayList<Integer>();
			//Clear current graph
			com.cleanGraph();
			
			inputFile = new File(path_flie);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Node rootNode = doc.getDocumentElement();
			Node initState = rootNode.getFirstChild(); 
			startState = Integer.parseInt(initState.getTextContent());
			
			NodeList endStates = doc.getElementsByTagName("endstate");
			for (int i = 0 ; i < endStates.getLength(); i++) {
				Node nNode = endStates.item(i);
				endState.add(Integer.parseInt(nNode.getTextContent()));
			}
			
			NodeList states = doc.getElementsByTagName("state");
			for (int i = 0 ; i < states.getLength(); i++) {
				Node nNode = states.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE){
					 Element eElement = (Element) nNode;
					 int x = Integer.parseInt(eElement.getElementsByTagName("x").item(0).getTextContent());
					 int y = Integer.parseInt(eElement.getElementsByTagName("y").item(0).getTextContent());
					 Shape shape = new Shape();
					 shape.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
					 shape.setPoint(new Point2D.Float(x, y));
					 shape.setRadius(30);
					 shape.setHaveLine((eElement.getElementsByTagName("haveline").item(0).getTextContent().equals("true"))?true:false);
					 shape.setLineIndex(Integer.parseInt(eElement.getElementsByTagName("lineindex").item(0).getTextContent()));
					 boolean initial = (eElement.getElementsByTagName("initial").item(0).getTextContent().equals("true"))? true : false;
					 boolean terminal = (eElement.getElementsByTagName("terminal").item(0).getTextContent().equals("true"))? true : false;
					 shape.setState(new StateImpl(initial, terminal));
					 listStates.add(shape);
				}
			}
			
			NodeList edges = doc.getElementsByTagName("edge");
			for (int i = 0 ; i < edges.getLength(); i++) {
				Node nNode = edges.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE){
					 Element eElement = (Element) nNode;

					 String type = eElement.getElementsByTagName("type").item(0).getTextContent();
					 int source = Integer.parseInt(eElement.getElementsByTagName("source").item(0).getTextContent());
					 int des = Integer.parseInt(eElement.getElementsByTagName("des").item(0).getTextContent());
					 String label = eElement.getElementsByTagName("label").item(0).getTextContent();
					 int x1 = Integer.parseInt(eElement.getElementsByTagName("ctrl1").item(0).getFirstChild().getTextContent());
					 int y1 = Integer.parseInt(eElement.getElementsByTagName("ctrl1").item(0).getFirstChild().getNextSibling().getTextContent());
					 int x2 = Integer.parseInt(eElement.getElementsByTagName("ctrl2").item(0).getFirstChild().getTextContent());
					 int y2 = Integer.parseInt(eElement.getElementsByTagName("ctrl2").item(0).getFirstChild().getNextSibling().getTextContent());
					 Edge ed = new Edge(source, des, label, type, new Point2D.Float(x1, y1),  new Point2D.Float(x2, y2));
					 listEdges.add(ed);
					 transitions.add(new TransitionImpl<String>(listStates.get(source).getState(), listStates.get(des).getState(), label));
				}
			}
			com.setStartState(startState);
			com.setEndState(endState);
			com.setListPoints(listStates);
			com.setListLine(listEdges);
			com.setTransition(transitions);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void addGraphComponent(GraphComponent com) {
		this.com = com;
	}
}
