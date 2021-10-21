package Chat01;


	
import java.io.IOException;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

	public class Client { //������ �ϳ��� Ŭ���̾�Ʈ�� ����ϱ� ���� ����� ����
		Socket socket;
		public Client(Socket socket) {
			this.socket = socket;
			receive();
		}
		//Ŭ���̾�Ʈ�κ��� �޼����� ���� �޴� �޼ҵ�
		public void receive() {
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							InputStream in = socket.getInputStream(); //���޹޴� ����
							byte[] buffer = new byte[512]; //�ѹ��� 512byte��ŭ�� ���޹��� �� �ְ� �� ��
							int length = in.read(buffer); //��ǲ��Ʈ���� �̿��Ͽ� ���� ���� ������ ����Ʈ ���ۿ� ��� ����Ʈ ���� ��ȯ�Ѵ�.
							//length�� ������ ũ�Ⱑ ��
							if(length == -1) throw new IOException();
							System.out.println("[�޼��� ���� ����]"
									+ socket.getRemoteSocketAddress()
									+ ": " + Thread.currentThread().getName());
							String message = new String(buffer, 0, length, "UTF-8"); //���޹��� ������ ���ڵ� �Ͽ� ���ڿ� ������ ����
							for(Client client : servermain.clients) { //���޹��� �޼����� �ٸ� Ŭ���̾�Ʈ�鿡�Ե� ��� �����ϰ� ��
								client.send(message);
							}
						}
					} catch(Exception e) {
						try {
							System.out.println("[�޼��� ���� ����]"
									+ socket.getRemoteSocketAddress()
									+ ": " + Thread.currentThread().getName());
							servermain.clients.remove(Client.this);
							socket.close();
						} catch(Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			};
			servermain.threadPooL.submit(thread); //MainŬ������ ������Ǯ ������ 
			//�����Ǵ� �����带 ���������� �����ϱ� ����  �����带 ���
		}
		// Ŭ���̾�Ʈ���� �޼����� �����ϴ� �޼ҵ�
		public void send(String message) {
			Runnable thread = new Runnable() {
				@Override
				public void run() {
					try {
						OutputStream out = socket.getOutputStream();
						byte[] buffer = message.getBytes("UTF-8"); //�޼��� ������ ���ڵ��Ͽ� ���� �迭�� ��´�.
						out.write(buffer); //������ ������ ���
						out.flush(); //�ʼ�
					} catch (IOException e) {
						try {
							System.out.println("[�޼��� �۽� ����]"
									+ socket.getRemoteSocketAddress()
									+ ": " + Thread.currentThread().getName());
							servermain.clients.remove(Client.this); //����Ŭ������ �߰��� ������ �� Ŭ���̾�Ʈ�� ����
							socket.close();
						} catch(Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			};
			servermain.threadPooL.submit(thread);
		}
	}

