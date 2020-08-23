package com.thomax.shadow.proxy.cipher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

/**
 * Helper class
 */
public class Util {
    public static String dumpBytes(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%x", b & 0xff));
        return sb.toString();
    }

    public static byte[] randomBytes(int size) {
        byte[] bytes = new byte[size];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    public static String getErrorMessage(Throwable e) {
        Writer writer = new StringWriter();
        PrintWriter pWriter = new PrintWriter(writer);
        e.printStackTrace(pWriter);
        return writer.toString();
    }



    public static String bytesToString(byte[] data, int start, int length) {
        String str = "";

        try {
            str = new String(data, start, length, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }


    public static boolean saveFile(String fn, String content) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(fn);
            writer.println(content);
            writer.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public static String getFileContent(String fn) {
        Path path = Paths.get(fn);
        String content = "";
        try {
            content = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            // do nothing
        }

        return content;
    }

    private static short byteToUnsignedByte(byte b) {
        return (short)(b & 0xff);
    }

    private static int getPort(byte b, byte b1) {
        return byteToUnsignedByte(b) << 8 | byteToUnsignedByte(b1);
    }
}
