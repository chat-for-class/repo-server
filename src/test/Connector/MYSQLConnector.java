package test.Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MYSQLConnector {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private static MYSQLConnector instance = null;
	private boolean connectedFlag = false;
	
	private MYSQLConnector() {}
	
	public static MYSQLConnector getInstance(){
		if(instance == null){
			instance = new MYSQLConnector();
		}
		
		return instance;
	}
	
	public void connection(String serverip, String port, String databaseName, String user, String password) throws ClassNotFoundException, SQLException{

		if(!connectedFlag){
			Class.forName("org.gjt.mm.mysql.Driver");
			String url = "jdbc:mysql://"+serverip+":"+port+"/"+databaseName;
		
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			connectedFlag = true;
		}
	}
	
	public boolean loginCheck(String id, String pw) throws SQLException{
		boolean flag = false;
		
		pstmt = null;
		String sql = "select id from Clients where id = ?AND pw=?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setString(2, pw);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()==false){// 존재하지 않으면 
			flag = false;
		}else{
			flag = true;
		}
		
		/*
		try {
			String sql = "select pw.id, pw.password from Clients pw, (select * from Clients where id = ?) id where pw.password = ? and id.id = pw.id";
			pstmt = conn.prepareStatement(sql);
		
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()){
				String t_id = rs.getString(1);
				String t_pw = rs.getString(2);
				
				if(t_id.equals(id) && t_pw.equals(pw)){
					flag = true;
				}
				
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}finally{
			if(pstmt != null) pstmt.close();
			pstmt = null;
		}
		*/
		return flag;
	}
	
	public boolean insertJoin(String id, String pw, String mail) throws SQLException{
		boolean flag = false;
		
		try {
			String sql = "insert into Clients values (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, mail);
			
			flag = pstmt.executeUpdate() == 1 ? true : false;
			conn.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		}finally {
			if(pstmt != null) pstmt.close();
			pstmt = null;
		}
		
		return flag;
	}
	
	public boolean CheckIdduplication(String id) throws SQLException{
		boolean flag = false;
		
		try {
			String sql = "select * from Clients where id=?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			String t_id = null;
			
			while(rs.next()){
				t_id = rs.getString(1);
			}
			
			flag = t_id == null ? true : false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
		}finally {
			if(pstmt != null) pstmt.close();
			pstmt = null;
		}
		
		return flag;
	}
	
	public boolean isConnected(){
		return connectedFlag;
	}
	
	public void close() throws SQLException{
		if(conn != null) conn.close();
	}
	
}
