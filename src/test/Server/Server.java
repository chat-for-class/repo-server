package test.Server;

import java.sql.SQLException;

import test.Connector.MYSQLConnector;
import utill.XmlUtill;

public class Server {
	public static void main(String[] args) {
		String config = System.getProperty("DBconfig");
    	String rootName = "MYSQLConfig";
    	
    	if(config != null && !config.equals("")){
    		
    	MYSQLConnector connector = MYSQLConnector.getInstance();
    	XmlUtill xml = XmlUtill.getInstance();
    	
    	xml.init(config, rootName);
    	
    	String serverip = xml.getAttributeValue("ip");
    	String port = xml.getAttributeValue("port");
    	String dbName = xml.getAttributeValue("databaseName");
    	String id = xml.getAttributeValue("id");
    	String password = xml.getAttributeValue("password");
    	
    	if(!connector.isConnected()){
			try {
				connector.connection(serverip, port, dbName, id, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	
    	}
		
		new AcceptThread(10000, "AcceptThread").start();
	}
}
