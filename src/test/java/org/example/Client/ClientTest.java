package org.example.Client;

import org.testng.annotations.Test;

import java.io.File;

import static org.testng.AssertJUnit.assertTrue;

public class ClientTest {
    private static final Logger log = Logger.getInstance();
    String path = "clientLog.txt";
    @Test
    public void testMain() {
        String msg = "Test message";
        File file = new File(path);
        long beforeLength = file.length();
        log.log(msg,path);
        long afterLength = file.length();
        boolean afterLengthOverBefore = afterLength > beforeLength;
        assertTrue(afterLengthOverBefore);
    }
}