package Chat01;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
public class servermain extends Application {
	
	
	public static ExecutorService threadPooL; //������Ǯ�� �̿��Ͽ� ���� Ŭ���̾�Ʈ�� �����Ͽ��� �� 
	//���� ��������� ȿ�������� �����ϵ��� �� ���ڱ� Ŭ���̾�Ʈ�� ���� �����Ͽ��� �̸� ������
	
	
	public static Vector<Client> clients = new Vector<Client>(); //Ŭ���̾�Ʈ���� ��� ����
	ServerSocket serverSocket;
	
	
	//������ �������Ѽ� Ŭ���̾�Ʈ�� ������ ��ٸ��� �޼ҵ�
	public void startServer(String IP, int port) {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
		} catch (Exception e) {
			e.printStackTrace();
			if(!serverSocket.isClosed()) { //���� ������ �����ִ� ���°� �ƴ϶��
				stopServer();
			}
			return;
		}
		//Ŭ���̾�Ʈ�� ������ ������ ��ٸ��� ������
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Socket socket = serverSocket.accept(); //Ŭ���̾�Ʈ�� ������ �ߴٸ�
						clients.add(new Client(socket));
						System.out.println("[Ŭ���̾�Ʈ ����]"
								+ socket.getRemoteSocketAddress()
								+ ": " + Thread.currentThread().getName());
					} catch (Exception e) {
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}
				}
			}
		};
		threadPooL = Executors.newCachedThreadPool(); //������ Ǯ �ʱ�ȭ
		threadPooL.submit(thread);
	}
	// ������ �۵��� ������Ű�� �޼ҵ�
	public void stopServer() {
		try {
			Iterator<Client> iterator = clients.iterator(); //�ݺ��� ����
			while(iterator.hasNext()) { //���ͷ����ͷ� Ŭ���̾�Ʈ �ϳ��� ����
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			// ���� ���� ��ü �ݱ�
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			// ������ Ǯ �����ϱ�
			if(threadPooL != null && !threadPooL.isShutdown()) {
				threadPooL.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// UI�� �����ϰ�, ������ ���α׷��� �۵���Ű�� �޼ҵ�
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(5));
		TextArea textArea = new TextArea();
		textArea.setEditable(false); //������ ��¸� �ϰ� ���� �Ұ�
		textArea.setFont(new Font("�������", 15));
		root.setCenter(textArea); //�߰��� textArea�� ����
		Button toggleButton = new Button("�����ϱ�");
		toggleButton.setMaxWidth(Double.MAX_VALUE);
		BorderPane.setMargin(toggleButton, new Insets(1,0,0,0));
		root.setBottom(toggleButton); //�Ʒ���ġ�� ��ư�� ����
		String IP = "127.0.0.1"; //�ڽ��� ���� �ּ�, �׽�Ʈ
		int port = 9876;
		toggleButton.setOnAction(event -> { //����ڰ� ��ư�� ������ �� �߻��ϴ� �̺�Ʈ ó��
			if(toggleButton.getText().equals("�����ϱ�")) {
				startServer(IP, port); //���� ����
				Platform.runLater(() -> { //�ʼ�! - ui��Ҹ� ���
					String message = String.format("[���� ����]\n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("�����ϱ�");
				});
			} 
			else { //�����ϱ� ��ư�� ���� ���
				stopServer();
				Platform.runLater(() -> { //�ʼ�! - ui��Ҹ� ���
					String message = String.format("[���� ����]\n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("�����ϱ�");
				});
			}
		});
		Scene scene = new Scene(root,400,400);
		primaryStage.setTitle("[ä�� ����]");
		primaryStage.setOnCloseRequest(event -> stopServer()); //���α׷��� ������ �����ߴٸ� stopServer�� ȣ���� �ڿ� ����
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	public static void main(String[] args) {
		launch(args);
	}
}
