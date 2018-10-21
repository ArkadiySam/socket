import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Arkadiy on 05.10.2018.
 */
public class Client {
    private static final int serverPort = 6666;
    private static final String localhost = "127.0.0.1";

    public static void main(String[] ar) throws IOException {
        Socket socket = null;
        System.out.println("Welcome to Client side\n" +
                "Connecting to the server\n\t" +
                "(IP address " + localhost +
                ", port " + serverPort + ")");
        InetAddress ipAddress = InetAddress.getByName(localhost);
        socket = new Socket(ipAddress, serverPort);
        System.out.println("The connection is established.");

        System.out.println(
                "\tLocalPort = " +
                        socket.getLocalPort() +
                        "\n\tInetAddress.HostAddress = " +
                        socket.getInetAddress().getHostAddress() +
                        "\n\tReceiveBufferSize (SO_RCVBUF) = " +
                        socket.getReceiveBufferSize());

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        // Создаем поток для чтения с клавиатуры.
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader keyboard = new BufferedReader(isr);
        String line = null;
        System.out.println("Type in something and press enter");
        System.out.println();
        while (true) {
            line = keyboard.readLine();
            out.writeUTF(line);     // Отсылаем строку серверу
            out.flush();            // Завершаем поток
            line = in.readUTF();    // Ждем ответа от сервера
            if (line.endsWith("quit"))
                break;
            else {
                System.out.println(
                        "The server sent me this line :\n\t" + line);
            }
        }
    }
}


