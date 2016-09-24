
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class Topology {
	
	Nodes nodes[];
	Links links[];
	Demand demand[];
	int n;
	int m;
	int p;
	
	public void setTopology(String xmlFile){
		try{
			File inputFile = new File(xmlFile);
			DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getDocumentElement().getElementsByTagName("node");
			n = nodeList.getLength();
			nodes = new Nodes[n];
			for(int i = 0; i<n;i++)
				nodes[i] = new Nodes();
			
			for(int i=0;i<n;i++){
				Element e = (Element)nodeList.item(i);
				nodes[i].name = e.getAttribute("id");
				nodes[i].x = Double.parseDouble(e.getElementsByTagName("x").item(0).getTextContent());
				nodes[i].y = Double.parseDouble(e.getElementsByTagName("y").item(0).getTextContent());
				nodes[i].id = i;
			//	nodes[i].print();
			}
			
			nodeList = doc.getDocumentElement().getElementsByTagName("link");
			m = nodeList.getLength();
			links = new Links[m];
			for(int i=0;i<m;i++){
				links[i] = new Links();
				Element e = (Element)nodeList.item(i);
				links[i].source = e.getElementsByTagName("source").item(0).getTextContent();
				links[i].target = e.getElementsByTagName("target").item(0).getTextContent();
				links[i].capacity = Double.parseDouble(e.getElementsByTagName("capacity").item(0).getTextContent());
				links[i].setAll(nodes);
			//	links[i].print();
			}
			
			nodeList = doc.getDocumentElement().getElementsByTagName("demand");
			p = nodeList.getLength();
			demand = new Demand[p];
			for(int i=0;i<p;i++){
				demand[i] = new Demand();
				Element e = (Element)nodeList.item(i);
				demand[i].source = e.getElementsByTagName("source").item(0).getTextContent();
				demand[i].target = e.getElementsByTagName("target").item(0).getTextContent();
				demand[i].demandValue = Double.parseDouble(e.getElementsByTagName("demandValue").item(0).getTextContent());
				demand[i].setAll(nodes);
			//	demand[i].print();
			}
			
			
			
		}catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	
}
