//http://sndlib.zib.de/home.action
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class Topology {
	
	Nodes nodes[];
	
	public void setTopology(){
		try{
			File inputFile = new File("abilene.xml");
			DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getDocumentElement().getElementsByTagName("node");
			int n = nodeList.getLength();
			nodes = new Nodes[n];
			
			for(int i=0;i<n;i++){
				Element e = (Element)nodeList.item(i);
				
			}
			
			Element e = (Element)doc.getDocumentElement().getElementsByTagName("node").item(0);
			
//			double a = e.getElementsByTagName("x").item(0).getTextContent();

			
			System.out.println(e.getElementsByTagName("x").item(0).getTextContent());
		}catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	
}
