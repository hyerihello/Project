package application;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Entity.EmpEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;
import myjdbc.JDBCTemplate;
import biz.*;
import biz.EmpBiz;

public class sign_idController {
private Stage Stage;	
public static JDBCTemplate db;

@FXML
public Button back_create;


@FXML    
public void BackLogIn(ActionEvent Action) {//버튼의 액션을 적는다.
	try{
    	Parent Login = FXMLLoader.load(getClass().getResource("Sample.fxml"));
	    Scene scene = new Scene(Login);
	    Stage primaryStage = (Stage)back_create.getScene().getWindow(); 
	    primaryStage.setScene(scene);
	     }catch(Exception e){
	       e.printStackTrace();
	       }
	 }

@FXML
public TextField sign_id;


@FXML
public TextField sign_pass;

@FXML
public TextField sign_email;

@FXML
public TextField sign_phone;


@FXML
public Button sign_create;

@FXML
public void id_create(ActionEvent Action) {
	//System.out.println(sign_id.getText()+ "이랑"+ sign_pass.getText()+"이랑" + sign_email.getText() +"이랑"+ sign_phone.getText());	
	//String s = sign_id.getText()+ "이랑"+ sign_pass.getText()+"이랑" + sign_email.getText() +"이랑"+ sign_phone.getText();	
	//System.out.println(s);
	
	
	 EmpBiz biz = new EmpBiz(); //biz를 위로올리면 insert, select 모두 사용할 수 있다.
	
	 //insert
	 EmpEntity insert = new EmpEntity(sign_id.getText(),sign_pass.getText(),sign_email.getText(),sign_phone.getText());
	 int res = 0;
	 res = biz.getEmpInsert(insert);
	 System.out.println(res);
	 
	 
	
	 // select
	 List<EmpEntity> entity;// = new ArrayList<EmpEntity>(); 	
	 entity = biz.getSelectAll();	 
	 System.out.println(entity);	 
	 for (EmpEntity r : entity)	 {
		 System.out.println(1);
		 System.out.println(r.getId());
		 System.out.println(r.getPw());
		 System.out.println(r.getEml());
		 System.out.println(r.getTel());
	 }

	 
	 
	
	Stage = (Stage)sign_create.getScene().getWindow(); //sign_create의 버튼에서 창을 가져오는거니깐 sign_create
	Popup pop = new Popup();
	
	try{
		Parent root = FXMLLoader.load(getClass().getResource("CreatePopup.fxml"));
		
		pop.getContent().add(root);
		
		pop.setAutoHide(true);
    	
		pop.show(Stage);
    	
    	
    	Parent Login = FXMLLoader.load(getClass().getResource("Sample.fxml"));
	    Scene scene = new Scene(Login);
	    Stage primaryStage = (Stage)back_create.getScene().getWindow(); 
	    primaryStage.setScene(scene);
	     }catch(Exception e){
	       e.printStackTrace();
	       }
	
}
}
