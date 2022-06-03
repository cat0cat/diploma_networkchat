package org.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Server {

    public static ArrayList<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        ServerWork.ConnectWithServer.Connect();
    }

}

class ServerWork implements Runnable {
    private static int port;
    private static String host;
    private static String path;
    private static final Logger log = Logger.getInstance();
    Socket client;

    public ServerWork(Socket socket) {

        this.client = socket;
    }

    @Override
    public void run() {
        //запускаем сервер
        String userName = "";

        log.log("Сервер запущен. Port: " + port + " Host: " + host, path);
        System.out.println("Сервер запущен. Port: " + port + " Host: " + host);

        while (true) {
            try {
                PrintWriter out = new PrintWriter(this.client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));



                String line;
                while ((line = in.readLine()) != null) {
                    if (line.lastIndexOf("/name") == 0) {
                        userName = line;
                        userName = userName.replace("/name", "").trim();
                        send("Привет, " + userName + "!");

                        log.log(userName + " присоединился к чату", path);
                        continue;
                    }
                    // Пишем ответ

                        if (!userName.isEmpty())
                            send('[' + userName + "] " + line);
                    }
                    // Выход если от клиента получили /exit
                    if (line.equals("/exit")) {
                        log.log(userName + " вышел из чата", path);
                        Server.clients.remove(client);
                        break;
                    }

            } catch (IOException ex) {
                log.log(ex.getMessage(), path);
                ex.printStackTrace(System.out);
            }
        }

    }
    private void send(String message) throws IOException {
        for (Socket client : Server.clients) {
            if (client.isClosed()) continue;
            PrintWriter sender = new PrintWriter(client.getOutputStream());
            sender.println(message);
            sender.flush();
        }
        log.log(message,path);
    }

    class ConnectWithServer {
        public static void Connect() {
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
            try (ServerSocket server = new ServerSocket(port)) {
                while (true) {
                    Socket client = server.accept();
                    Server.clients.add(client);
                    new Thread(new ServerWork(client)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.log(e.getMessage(), path);
            }

        }
    }
}
