import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Arkadiy on 05.10.2018.
 */
public class Server extends Thread {
    private static final int port = 6666; // открываемый порт сервера
    private String TEMPL_MSG = "The client '%d' sent me message : \n\t";
    private String TEMPL_CONN = "The client '%d' closed the connection";

    private Socket socket;
    private int num;

    private void setSocket(int num, Socket socket) {
        this.num = num;
        this.socket = socket;
        setDaemon(true);
        setPriority(NORM_PRIORITY);
        start();
    }

    @Override
    public void run() {
        try {
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream dis = new DataInputStream(sin);
            DataOutputStream dos = new DataOutputStream(sout);

            String line = null;
            while (true) {
                line = dis.readUTF();
                System.out.println(String.format(TEMPL_MSG, num) + line);
                System.out.println("I'm sending it back...");
                dos.writeUTF("Server receive text : " + line);
                dos.flush();
                System.out.println();
                if (line.equalsIgnoreCase("quit")) {
                    socket.close();
                    System.out.println(String.format(TEMPL_CONN, num));
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

    public static void main(String[] ar) throws IOException {
        int i = 0; // Счётчик подключений
        InetAddress ia = InetAddress.getByName("localhost");
        ServerSocket srvSocket = new ServerSocket(port, 0, ia);
        System.out.println("Server started\n\n");
        while (true) {
            Socket socket = srvSocket.accept();
            System.err.println("Client accepted");
            new Server().setSocket(i++, socket);
        }
    }
}
