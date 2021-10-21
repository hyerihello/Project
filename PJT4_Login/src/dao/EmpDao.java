package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Entity.EmpEntity;
import static myjdbc.JDBCTemplate.*;

//CRUD만 하겠다. = commit/rollback 
public class EmpDao implements EmpDaoIm{
	Connection con;
	public EmpDao(Connection con) {
		this.con = con;
	}




	//1. select
	public List<EmpEntity> getselectAll(){
		String all = "select * from join";
		//레코드 한줄씩 EmpEntity 로 담아서 list객체에 추가한 다음 리턴
		List<EmpEntity> Listall = new ArrayList<>();
		EmpEntity entity = null;
		Statement stmt = null;
		ResultSet rs = null; //db정보 받을수있는 메서드? result -> entity에 넣어서 리턴할것이다
		
		try {
			 stmt = con.createStatement();
			 rs = stmt.executeQuery(select_all);
			 while(rs.next()) {
				 entity = new EmpEntity(rs.getString(1), 
						  rs.getString(2),
						  rs.getString(3),
						  rs.getString(4));
				 
				 Listall.add(entity); //list 추가한다 한행씩!
			 }//while add
			 
		}catch(Exception e) {
				
		
	} finally {
		Close(rs);
		Close(stmt);
	}//try end
		return Listall; // return하면 jdbctemplate로간닫
	}

	//2. insert
	public int getEmpInsert(EmpEntity entity) {

		PreparedStatement pstm = null; // value 값을 값을 대입할때 (?,?,?,?)
		int res =0; //insert input 1, out0 그래서 int로 해야한다.
		try {
			pstm = con.prepareStatement(insert_emp);
			pstm.setString(1, entity.getId()); //첫번째물음표 1
			pstm.setString(2, entity.getPw());
			pstm.setString(3, entity.getEml());
			pstm.setString(4, entity.getTel());


			res = pstm.executeUpdate(); // insert 실행
			if (res >0) { // 들어갔으면 1, 없으면 0 이기 때문에 들어가면 commit!
				Commit(con);
			}
		}catch(Exception e) {
			//System.out.println(e.toString());
			Rollback(con);
		}finally {
			Close(pstm);
		}
		return res;
	}}
	
	
	
//	// update, delete 필요없음 	
//	//3. update
//	public int getEmpUpdate(EmpEntity entity) {
//
//		PreparedStatement pstm = null;
//		int res =0;
//		try {
//			pstm = con.prepareStatement(update_emp);
//			pstm.setDouble(1, entity.getSal());
//			pstm.setInt(2, entity.getEmpno());
//
//			
//			res = pstm.executeUpdate(); // insert 실행
//			if (res >0) {
//				Commit(con);
//			}
//		}catch(Exception e) {
//			//System.out.println(e.toString());
//			Rollback(con);
//		}finally {
//			Close(pstm);
//		}
//		return res;
//	}
//	
//	
//	
//	//4. delete
//	public int getEmpDelete(EmpEntity entity) {
//
//		PreparedStatement pstm = null;
//		int res =0;
//		try {
//			pstm = con.prepareStatement(delect_emp);
//			
//			pstm.setInt(1, entity.getEmpno());
//
//			
//			res = pstm.executeUpdate(); // insert 실행
//			if (res >0) {
//				Commit(con);
//			}
//		}catch(Exception e) {
//			
//			Rollback(con);
//		}finally {
//			Close(pstm);
//		}
//		return res;
//	}
//	}
