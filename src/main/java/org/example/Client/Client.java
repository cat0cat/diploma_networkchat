package org.example.Client;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private int port;
    private String host;
    private final Logger log = Logger.getInstance();
    private BufferedReader in;
    private PrintWriter out;
    private String path;
    private Socket socket;
    private Scanner scanner;

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

        try {
            this.socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            log.log(e.getMessage(), path);
        }

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            scanner = new Scanner(System.in);

            String msg;
            // Узнаем имя, приветствуем
            System.out.print("Введите Ваше имя: ");
            msg = scanner.nextLine();

            out.println("/name " + msg);
            String receivedMsg = "SERVER: " + in.readLine();
            System.out.println(receivedMsg);
            log.log(receivedMsg, path);

            new WriteChat().start();
            new ReadChat().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Client();
    }

    //читаем сообщения из сокета
    private class ReadChat extends Thread {
        @Override
        public void run() {
            String str;
            try  {
                while (true) {
                    System.out.print("> ");
                    str = in.readLine();
                    if (str.equals("/exit")) {
                        log.log(str, path);
                        break;
                    }
                    System.out.println(str);
                    log.log(str, path);
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.log(e.getMessage(), path);
            }
        }
    }

    //пишем сообщения в сокет
    public class WriteChat extends Thread {
        @Override
        public void run() {
            while (true) {
                String userMsg;
                userMsg = scanner.nextLine();
                if (userMsg.equals("/exit"))
                    break;
                else {
                    out.println(userMsg);
                }
                out.flush();
            }
        }
    }
}
