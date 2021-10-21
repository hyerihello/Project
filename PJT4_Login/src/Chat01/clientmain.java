package Chat01;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
public class clientmain extends Application {
	Socket socket;
	TextArea textArea;
	//Ŭ���̾�Ʈ ���α׷� ���� �޼ҵ�
	public void startClient(String IP, int port) {
		//�����ʹ� �ٸ��� ������Ǯ�� ����� �ʿ� x �׷��� Runnable�� �ƴ϶� �׳� Thread�� ���
		Thread thread = new Thread() {
			public void run() {
				try {
					socket = new Socket(IP, port);
					receive(); //�����κ��� �޽����� ���� ����
				} catch (Exception e) {
					if(!socket.isClosed()) {
						stopClient();
						System.out.println("[���� ���� ����]");
						Platform.exit();
					}
				}
			}
		};
		thread.start();
	}
	//Ŭ���̾�Ʈ ���α׷� ���� �޼ҵ�
	public void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	// �����κ��� �޼����� ���޹޴� �޼ҵ�
	public void receive() {
		while(true) {
			try { //������ ���� ��ǲ��Ʈ���� ������ �̿��� ��� �Ȱ���
				InputStream in = socket.getInputStream(); //���޹޴� ����
				byte[] buffer = new byte[512]; //�ѹ��� 512byte��ŭ�� ���޹��� �� �ְ� �� ��
				int length = in.read(buffer); //��ǲ��Ʈ���� �̿��Ͽ� ���� ���� ������ ����Ʈ ���ۿ� ��� ����Ʈ ���� ��ȯ�Ѵ�.
				//length�� ������ ũ�Ⱑ ��
				if(length == -1) throw new IOException();
				String message = new String(buffer, 0, length, "UTF-8");
				Platform.runLater(()->{
					textArea.appendText(message);
				});
			} catch(Exception e) {
				stopClient();
				break;
			}
		}
	}
	// ������ �޼����� �����ϴ� �޼ҵ�
	public void send(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer); //������ �ƿ�ǲ��Ʈ���� ���(����) �׷� ������ ��ǲ��Ʈ������ ������ ��� ����Ѵ�.
					out.flush();
				} catch(Exception e) {
					stopClient();
				}
			}
		};
		thread.start();
	}
	//������ ���α׷��� ���۽�Ű�� �޼ҵ�
	@Override
	public void start(Stage primaryStage) {
		
		
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		TextField userName = new TextField(); //������� �̸��� �� ����
		userName.setPrefWidth(150);
		userName.setPromptText("�г����� �Է��ϼ���.");
		HBox.setHgrow(userName, Priority.ALWAYS);
		TextField IPText = new TextField("127.0.0.1"); //�⺻������ 127.0.0.1�� ����
		TextField portText = new TextField("9876");
		portText.setPrefWidth(80);
		hbox.getChildren().addAll(userName, IPText, portText); //hbox�� ���� ��ҵ� �߰�
		root.setTop(hbox);
		textArea = new TextArea();
		textArea.setEditable(false); //������ ��¸� �ϰ� ���� �Ұ�
		root.setCenter(textArea); //�߰��� textArea�� ����
		TextField input = new TextField();
		input.setPrefWidth(Double.MAX_VALUE);
		input.setDisable(true); //�����ϱ� ��ư�� ������ �������� �޼��� ���� �ؽ�Ʈ �Է� �Ұ�
		input.setOnAction(event-> {
			send(userName.getText() + ": " + input.getText() + "\n"); //����� �̸��� �Է��� ������ ������ ������
			input.setText(""); //������ �޽��� ������ �ʱ�ȭ
			input.requestFocus(); //�ٽ� �޼����� ���� �� �ְ� ��Ŀ�� ����
		});
		Button sendButton = new Button("������");
		sendButton.setDisable(true); //�����ϱ� �������� send��ư ��Ȱ��ȭ
		sendButton.setOnAction(event-> {
			send(userName.getText() + ": " + input.getText() + "\n"); //����� �̸��� �Է��� ������ ������ ������
			input.setText(""); //������ �޽��� ������ �ʱ�ȭ
			input.requestFocus(); //�ٽ� �޼����� ���� �� �ְ� ��Ŀ�� ����
		});
		Button connectionButton = new Button("�����ϱ�");
		connectionButton.setOnAction(event -> {
			if(connectionButton.getText().equals("�����ϱ�")) {
				int port = 9876;
				try {
					port = Integer.parseInt(portText.getText());
				} catch(Exception e) {
					e.printStackTrace();
				}
				startClient(IPText.getText(), port); //����
				Platform.runLater(() ->{
					textArea.appendText("[ ä�ù� ���� ]\n");
				});
				connectionButton.setText("�����ϱ�");
				input.setDisable(false); //���� �� �ٽ� �Է°����ϰ� �����
				sendButton.setDisable(false);
				input.requestFocus(); //�ٷ� �ٸ� �޽��� �Է� �����ϰ� ��Ŀ�� �ֱ�
			}
			else {
				stopClient();
				Platform.runLater(() -> {
					textArea.appendText("[ ä�ù� ���� ]\n");
				});
				connectionButton.setText("�����ϱ�");
				input.setDisable(true);
				sendButton.setDisable(true);
			}
		});
		BorderPane pane = new BorderPane();
		pane.setLeft(connectionButton);
		pane.setCenter(input);
		pane.setRight(sendButton);
		root.setBottom(pane);
		Scene scene = new Scene(root,400,400);
		primaryStage.setTitle("[ä�� Ŭ���̾�Ʈ]");
		primaryStage.setOnCloseRequest(event -> stopClient()); //���α׷��� ������ �����ߴٸ� stopClient�� ȣ���� �ڿ� ����
		primaryStage.setScene(scene);
		primaryStage.show();
		connectionButton.requestFocus(); //���α׷��� ����Ǹ� �����ϱ� ��ư�� �⺻������ ��Ŀ�� ��
		/*
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		*/
	}
	public static void main(String[] args) {
		launch(args);
	}
}
