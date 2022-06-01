package org.example.Server;

import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.testng.AssertJUnit.assertEquals;

public class ServerTest {
    Server server = new Server();
    private static final Logger log = Logger.getInstance();
    String path;
    String host;
    int port;

    @Test
    void main() {
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
        assertEquals(23334, port);
        assertEquals("127.0.0.1", host);
        assertEquals("serverLog.txt",path);

    }


}