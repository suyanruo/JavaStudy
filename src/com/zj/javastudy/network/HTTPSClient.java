package com.zj.javastudy.network;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.UnknownHostException;

public class HTTPSClient {

    public static void sendSslRequest(String host, int port) {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = null;
        try {
            socket = (SSLSocket) factory.createSocket(host, port);

            // 启用所有密码组
            String[] supported = socket.getSupportedCipherSuites();
            socket.setEnabledCipherSuites(supported);

            Writer out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            out.write("GET http://" + host + "/ HTTP/1.1\r\n");
            out.write("Host: " + host + "\r\n");
            out.write("\r\n");
            out.flush();

            // 读取响应
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 读取首部
            String s;
            while (!(s = in.readLine()).equals("")) {
                System.out.println(s);
            }
            System.out.println();

            // 读取长度
            String contentLength = in.readLine();
            int length = Integer.MAX_VALUE;
            try {
                length = Integer.parseInt(contentLength, 16);
            } catch (NumberFormatException e) {
                // 服务器响应的第一行不是content-length
            }
            System.out.println(contentLength);

            int c;
            int i = 0;
            while ((c = in.read()) != -1 && i++ < length) {
                System.out.write(c);
            }
            System.out.println();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
