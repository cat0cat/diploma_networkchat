package org.example.Client;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private int port;
    private String host;
    private String path;

    public Client() {
        // считываем настройки из файла
        Logger log = Logger.getInstance();
        try (FileReader reader = new FileReader("settings.txt")) {
            Properties props = new Properties();
            props.load(reader);
            port = Integer.parseInt(props.getProperty("SERVER_PORT"));
            host = props.getProperty("SERVER_HOST");
            path = props.getProperty("CLIENT_LOG");
        } catch (IOException ex) {
            log.log(ex.getMessage(), path);
            ex.printStackTrace(System.out);
        }

        try (Socket socket = new Socket(host, port)) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                 Scanner scanner = new Scanner(System.in)) {

                String msg;
                // Узнаем имя, приветствуем
                System.out.print("Введите Ваше имя: ");
                msg = scanner.nextLine();

                out.println("/name " + msg);
                String receivedMsg = "SERVER: " + in.readLine();
                System.out.println(receivedMsg);
                log.log(receivedMsg, path);

                while (true) {
                    System.out.print("> ");
                    msg = scanner.nextLine();
                    if (!msg.trim().isEmpty()) {
                        out.println(msg.trim());
                        receivedMsg = "SERVER: " + in.readLine();
                        System.out.println(receivedMsg);
                        log.log(receivedMsg, path);
                        if ("/exit".equals(msg)) break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
            log.log(e.getMessage(), path);
        }
    }

    public static void main(String[] args) {
        new Client();
    }

}
