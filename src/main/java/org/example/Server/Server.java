package org.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server extends Thread {

    private static int port;
    private static String host;
    private static String path;
    private static final Logger log = Logger.getInstance();
    private static BufferedReader in;
    private static PrintWriter out;

    public Server() {
        start();
    }

    public static void main(String[] args) {
        // считываем настройки из файла
        try (FileReader reader = new FileReader("settings.txt")) {
            Properties props = new Properties();
            props.load(reader);
            port = Integer.parseInt(props.getProperty("SERVER_PORT"));
            host = props.getProperty("SERVER_HOST");
            path = props.getProperty("SERVER_LOG");
        } catch (IOException ex) {
            log.log(ex.getMessage(), path);
            System.out.println(ex.getMessage());
        }
        //запускаем сервер
        String userName = "";

        while (true) {
            try (ServerSocket servSocket = new ServerSocket(port);
                 Socket socket = servSocket.accept()) {

                log.log("Сервер запущен. Port: " + port + " Host: " + host, path);
                System.out.println("Сервер запущен. Port: " + port + " Host: " + host);

                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    if (line.lastIndexOf("/name") == 0) {
                        userName = line;
                        userName = userName.replace("/name", "").trim();
                        out.println("Привет, " + userName + "!");
                        continue;
                    }
                    // Пишем ответ
                    if (!userName.isEmpty())
                        out.print('[' + userName + "] ");
                    out.println(line);
                    // Выход если от клиента получили /exit
                    if (line.equals("/exit")) {
                        break;
                    }
                    //out.flush();
                }

            } catch (IOException ex) {
                log.log(ex.getMessage(), path);
                ex.printStackTrace(System.out);
            }
        }

    }

    @Override
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            out.println(msg);
            out.flush();
            while (true) {
                msg = in.readLine();
                try {
                    if (msg.equals("/exit")) {
                        log.log(msg, path);
                        break;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    log.log(e.getMessage(), path);
                }
                log.log(msg, path);
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.log(e.getMessage(), path);
        }
    }

}
