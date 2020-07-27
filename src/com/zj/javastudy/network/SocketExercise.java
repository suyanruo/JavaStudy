package com.zj.javastudy.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class SocketExercise {

    public static final String DIC_SERVER = "dict.org";
    public static final int PORT = 2628;
    public static final int TIMEOUT = 15000;

    public static void printTime() {
        Socket socket;
        try {
            socket = new Socket("time.nist.gov", 13);
            socket.setSoTimeout(15000);
            StringBuffer time = new StringBuffer();
            InputStreamReader in = new InputStreamReader(socket.getInputStream(), "ASCII");
            for (int c = in.read(); c != -1; c = in.read()) {
                time.append((char) c);
            }
            System.out.println(time);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printDateFromNetwork() {
        // 时间协议起点为1900年（格林尼治标准时间），Java Date类起始时间为1970年，需要做个转换
        long differenceBetweenEpochs = 2208988800L;
        Socket socket = null;
        try {
            socket = new Socket("time.nist.gov", 37);
            socket.setSoTimeout(15000);
            InputStream raw = socket.getInputStream();
            long secondsSince1900 = 0;
            for (int i = 0; i < 4; i++) {
                secondsSince1900 = (secondsSince1900 << 8) | raw.read();
            }
            long secondsSince1970 = secondsSince1900 - differenceBetweenEpochs;
            Date time = new Date(secondsSince1970 * 1000);
            System.out.println(time);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void printDictionary() {
        String[] args = new String[] {"gold", "uranium", "silver", "copper", "lead"};
        Socket socket = null;
        try {
            socket = new Socket(DIC_SERVER, PORT);
            socket.setSoTimeout(TIMEOUT);
            Writer writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            for (String word : args) {
                define(writer, reader, word);
            }
            writer.write("quit\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void define(Writer writer, BufferedReader reader, String word) throws IOException {
        writer.write("DEFINE fd-eng-lat " + word + "\r\n");
        writer.flush();

        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("250")) {
                return;
            } else if (line.startsWith("552")) {
                return;
            }
            else if (line.matches("\\d\\d\\d .*")) continue;
            else if (line.trim().equals(".")) continue;
            else System.out.println(line);
        }
    }
}
