//http://sndlib.zib.de/home.action
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class Topology {
	
	public static void main(String[] args){
		try{
			File inputFile = new File("abilene.xml");
			DocumentBuilderFactory dbFactory  = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			
			Element e = (Element)doc.getDocumentElement().getElementsByTagName("node").item(0);

			
			System.out.println(e.getElementsByTagName("x").item(0).getTextContent());
		}catch (Exception e) {
	         e.printStackTrace();
	      }
	}
	
	
}
