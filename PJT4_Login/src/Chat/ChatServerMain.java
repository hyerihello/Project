package Chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChatServerMain extends Application {
	
	@FXML
	public TextField Main_id;

	@FXML
	public TextField main_pass;

	@FXML
	public Button MAIN_JOIN;
	
	@FXML
	public TextArea chatBox;
	
	@FXML    
	public void w_join(ActionEvent Action) {//버튼의 액션을 적는다.
    	Parent Login = null;
		try {
			Login = FXMLLoader.load(getClass().getResource("Window.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Scene scene = new Scene(Login);
	    Stage primaryStage = (Stage)MAIN_JOIN.getScene().getWindow(); 
	    primaryStage.setScene(scene);
	    
	    String IP = "127.0.0.1";
		int port = 9876;
		
		startServer(IP, port);
		Platform.runLater(() -> {
			String message = String.format("[서버 시작]\n", IP, port);
			chatBox.appendText(message);
		});
		//===========================================================================
			// logout 버튼 눌렀을 때
			stopServer();
			Platform.runLater(() -> {
				String message = String.format("[서버 종료]\n", IP, port);
				chatBox.appendText(message);
				});
		
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
	
	public static ExecutorService threadPool;
	public static Vector<ChatServerClient> clients = new Vector<ChatServerClient>();
	ServerSocket serverSocket;
	
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
		} catch (Exception e) {
			e.printStackTrace();
			if(!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						Socket socket = serverSocket.accept();
						clients.add(new ChatServerClient(socket));
						System.out.println("[클라이언트 접속]"
								+ socket.getRemoteSocketAddress()
								+ " : " + Thread.currentThread().getName());
					} catch (Exception e) {
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
				
			}
			
		};
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	
	public void stopServer() {
		try {
			Iterator<ChatServerClient> iterator = clients.iterator();
			while(iterator.hasNext()) {
				ChatServerClient client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
