package org.example.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger logger = null;

    public void log(String msg, String path) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(path, true));
            writer.write("[" + LocalDateTime.now().format(DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm:ss")) + " " + "] " + msg + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Logger() {}

    public static Logger getInstance() {
        if(logger == null) logger = new Logger();
        return logger;
    }

}
