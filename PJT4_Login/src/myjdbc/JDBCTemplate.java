package myjdbc;

import Entity.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


 // 매번 db코드 열고닫기 힘드니까 하나 만들어 놓자.
 // 하나의 테이블당 하나씩 템플릿 (ex, 하나의 emp table당 하나의 템플릿)
 // Emp -> Entity / CRUD -> static
 //	Emp -> VO
 // 모든 db는 static -> 한번만 insert하게끔, 또한 static이어야만 동시제어가 된다. 각각의 소멸시점때문에 static으로 한다.
 // Emp -> db에서는 객체화 시켰을때 Entity 라고 부른다. (테이블이 객체화 되어있는걸 엔티티라고 부른다)
 // 또한 CRUD가 들어있어야야 한다. CRUD -> static


// 연결, 클로즈 = Connection 
// 롤백, 커밋만 하겠다.  = Statement


public class JDBCTemplate {
	
	public static Connection getConnection() {
		Connection con = null;
		
		//1. 연결	
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:XE","scott","TIGER");
		if(!con.isClosed()) {
			System.out.println("연결했어 ");
		}
		
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	public static void Close(Connection con) {
		if(con != null) {
			try {
				con.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void Close(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void Close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
	
	
	public static void Rollback(Connection con) {
		if(con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}
	public static void Commit(Connection con) {
		if(con != null) {
			try {
				con.commit();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}	
	public static boolean isConnection(Connection con) {
		try {
			if(con != null && !con.isClosed()) { 
				// &비트연산, (a>b)&&(a<b++) 논리연산 -> 앞의 결과가 false 면 뒤에 연산 수행안하고 결과를 false로 리턴
				// (a>b)||(a<b++)  -> 앞의 결과가 true 면 뒤에 연산 수행안하고 결과를 true로 리턴
				return true;
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
}
