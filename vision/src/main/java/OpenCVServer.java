import com.esotericsoftware.kryonet.Server;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class OpenCVServer implements Runnable {

    private final Server server;
    private final String boundary = "stream";
    private BufferedImage img = null;
    private ServerSocket serverSocket;
    private Socket socket;
    private OutputStream outputStream;
    public Mat imag;
    private int port;

    public OpenCVServer(int port) {
        this.port = port;
        server = new Server();
        server.getKryo().register(HashMap.class);
        server.getKryo().register(ArrayList.class);
        server.getKryo().register(java.awt.Point.class);
        server.start();
        try {
            server.bind(port, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() throws IOException {
        serverSocket = new ServerSocket(8080);
        socket = serverSocket.accept();
        writeHeader(socket.getOutputStream(), boundary);
    }

    private void writeHeader(OutputStream stream, String boundary) throws IOException {
        stream.write(("HTTP/1.0 200 OK\r\n" +
                "Connection: close\r\n" +
                "Max-Age: 0\r\n" +
                "Expires: 0\r\n" +
                "Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0\r\n" +
                "Pragma: no-cache\r\n" +
                "Content-Type: multipart/x-mixed-replace; " +
                "boundary=" + boundary + "\r\n" +
                "\r\n" +
                "--" + boundary + "\r\n").getBytes());
    }

    public void pushImage(Mat frame) throws IOException {
        if (frame == null)
            return;
        try {
            outputStream = socket.getOutputStream();
            BufferedImage img = Mat2bufferedImage(frame);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();
            outputStream.write(("Content-type: image/jpeg\r\n" +
                    "Content-Length: " + imageBytes.length + "\r\n" +
                    "\r\n").getBytes());
            outputStream.write(imageBytes);
            outputStream.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (Exception ex) {
            socket = serverSocket.accept();
            writeHeader(socket.getOutputStream(), boundary);
        }
    }

    public static BufferedImage Mat2bufferedImage(Mat image) throws IOException {
        MatOfByte bytemat = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, bytemat);
        byte[] bytes = bytemat.toArray();
        InputStream in = new ByteArrayInputStream(bytes);
        BufferedImage img = null;
        img = ImageIO.read(in);
        return img;
    }

    public void sendData(HashMap<String, Object> data, Mat matrix) {
        server.sendToAllTCP(data);
        System.out.println("sending data to " + server.getConnections().length + " connections!");
        this.imag = matrix;
    }

    @Override
    public void run() {
        try {
            System.out.print("go to  http://localhost:8080 with browser");
            startServer();

            while (true) {
                pushImage(imag);
            }
        } catch (IOException e) {
            return;
        }
    }
}
