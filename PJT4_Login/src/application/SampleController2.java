package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SampleController2 {


@FXML
public TextField Main_id;

@FXML
public TextField main_pass;

@FXML
public Button MAIN_JOIN;

@FXML    
public void w_join(ActionEvent Action) {//버튼의 액션을 적는다.
	try{
    	Parent Login = FXMLLoader.load(getClass().getResource("Window.fxml"));
	    Scene scene = new Scene(Login);
	    Stage primaryStage = (Stage)MAIN_JOIN.getScene().getWindow(); 
	    primaryStage.setScene(scene);
	    
	     }catch(Exception e){
	       e.printStackTrace();
	       }
	 }

@FXML    
public void signIn(ActionEvent Action) {//버튼의 액션을 적는다.
	try{
    	Parent Login = FXMLLoader.load(getClass().getResource("sign_id.fxml"));
	    Scene scene = new Scene(Login);
	    Stage primaryStage = (Stage)MAIN_CREATE.getScene().getWindow(); 
	    primaryStage.setScene(scene);
	    primaryStage.show();
	     }catch(Exception e){
	       e.printStackTrace();
	       }
	 }

@FXML
public Button MAIN_CANCEL;

@FXML
public Button MAIN_CREATE;
}
