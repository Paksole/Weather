import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        // �������� ������
        Socket socket = new Socket("localhost", 8080);

        // �������� �������
        socket.getOutputStream().write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());

        // ��������� ������
        byte[] buffer = new byte[1024];
        int bytesRead = socket.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, bytesRead));

        // �������� ������
        socket.close();
    }
}