package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtilHw09 {
	
	private static JDBCUtilHw09 instance = null;

	private JDBCUtilHw09() {} 
	
	public static JDBCUtilHw09 getInstance() {
		if(instance == null) 
			instance = new JDBCUtilHw09();
		return instance;
	}
	

	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String user = "pc11_5";
	private String password = "java";
	
	private Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	public void close(Connection conn, Statement stmt, PreparedStatement pstmt, ResultSet rs) {
		
		// 자원 반납
		if (rs != null) try {rs.close();} catch (SQLException e) {}
		if (pstmt != null) try {pstmt.close();} catch (SQLException e) {}
		if (stmt != null) try {stmt.close();} catch (SQLException e) {}
		if (conn != null) try {conn.close();} catch (SQLException e) {}		
	}

	
	// executeUpdate() => update, delete, insert
	public void update(String sql, List<Object> param) {
		 
		try {
			conn = DriverManager.getConnection(url, user, password);
			pstmt = conn.prepareStatement(sql);
			
			for (int i=0; i<param.size(); i++) {
				pstmt.setObject(i+1, param.get(i));
			} 
			
			int cnt = pstmt.executeUpdate();
			printResult(cnt);
			
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println();
			System.out.println("DB연결이 실패하거나, SQL문이 틀렸습니다.");
			System.out.print("사유 : " + e.getMessage());
		} finally {
			close(conn, stmt, pstmt, rs);
		}
		
	}


	// executeQuery() => select
	public List<Map<String, Object>> selectList(String sql, List<Object> param) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		try {
			conn = DriverManager.getConnection(url, user, password);
			pstmt = conn.prepareStatement(sql);

			for (int i=0; i<param.size(); i++) {
				pstmt.setObject(i+1, param.get(i));
			} 
			
			rs = pstmt.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			while (rs.next()) {
				
				Map<String, Object> row = new HashMap<String, Object>();
				for (int i=1; i<=columnCount; i++) {
					String key = rsmd.getColumnName(i);
					Object value = "";
					
					if (rsmd.getColumnTypeName(i).equals("CLOB")) {
						value = rs.getNString(i);
					} else {
						value = rs.getObject(i);
					}
					
					row.put(key, value);
				}
				list.add(row);
			}
			
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println();
			System.out.println("DB연결이 실패하거나, SQL문이 틀렸습니다.");
			System.out.print("사유 : " + e.getMessage());
		} finally {
			close(conn, stmt, pstmt, rs);
		}
		
		return list;
	}
	
	
	public void printResult (int cnt) {
		System.out.println();
		if (cnt > 0) {
			System.out.println("(*°▽°*) 성공! (*°▽°*)");
		} else {
			System.out.println("(°ロ°) 실패! (°ロ°)");
		}
	}

	
}
