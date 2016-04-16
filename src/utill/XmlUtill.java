package utill;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlUtill {
	
	private static XmlUtill instance = new XmlUtill();
	private Node root = null;
	
	private XmlUtill(){}
	
	public static XmlUtill getInstance(){
		return instance;
	}
	
	public void init(String xmlFilePath, String rootName){
		
		try {
			
			File configFile = new File(xmlFilePath);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(configFile);
			doc.getDocumentElement().normalize();
			
			Node root = doc.getDocumentElement();
			String nodeName = root.getNodeName();
			
			if(root != null && rootName.equalsIgnoreCase(nodeName)){
				this.root = root;
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getAttributeValue(String AttributeName){
		
		String value = null;
		
		if(root != null){

			NamedNodeMap attmap = root.getAttributes();
			Node item = attmap.getNamedItem(AttributeName);
			
			if(item != null){
				value = item.getNodeValue();
			}
			
		}
		return value;
	}
}
