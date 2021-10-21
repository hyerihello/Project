package application;

import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class WindowController {
// 왼쪽 사이드 바 
@FXML
public Label w_profile;

@FXML
public Label w_savechat;


@FXML
public Label w_name_m;

@FXML
public ImageView w_pic_m;

@FXML
public Button logout_w;

@FXML    
public void logoutw(ActionEvent Action) {//버튼의 액션을 적는다.
	try{
    	Parent Login = FXMLLoader.load(getClass().getResource("Sample.fxml"));
	    Scene scene = new Scene(Login);
	    Stage primaryStage = (Stage)logout_w.getScene().getWindow(); 
	    primaryStage.setScene(scene);
	     }catch(Exception e){
	       e.printStackTrace();
	       }
	 }

/// 채팅창
@FXML
public Label chat_member;



//입력창
@FXML
public Button add_pic;

@FXML
public TextArea chat_area;

@FXML
public TextField input_txt;

	
@FXML
public Button send_mssg; 
public String Message;

@FXML
public void sendMessage(ActionEvent Action) {
	Message = input_txt.getText();
	input_txt.setText("");
	chat_area.appendText(" :     " + Message +"        "+ LocalDateTime.now()+"\n");
	
	
}
}
