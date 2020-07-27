package com.zj.javastudy.network;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaytimeServer {
    public static final int PORT = 13;

    public static void main(String[] args) {
//        responseClient();
        responseClientPooled();
    }

    public static void responseClient() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            // 外部try捕获在daytime端口上构造ServerSocket对象时可能产生对任何Exception
            while (true) {
                try (Socket connect = server.accept()) {
                    // 内部try监视接受和处理连接时可能抛出的异常
                    Writer writer = new OutputStreamWriter(connect.getOutputStream());
                    Date now = new Date();
                    writer.write(now.toString() + "\r\n");
                    writer.flush();
                } catch (IOException e) {}
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void responseClientPooled() {
        ExecutorService pool = Executors.newFixedThreadPool(50);
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Callable<Void> daytimeTask = new DaytimeTask(server.accept());
                    pool.submit(daytimeTask);
                } catch (IOException e) {}
            }
        } catch (IOException e) {
            System.err.println("Could not start server");
        }
    }

    private static class DaytimeTask implements Callable<Void> {
        private Socket connect;

        public DaytimeTask(Socket connect) {
            this.connect = connect;
        }

        @Override
        public Void call() throws Exception {
            try {
                Writer writer = new OutputStreamWriter(connect.getOutputStream());
                Date now = new Date();
                writer.write(now.toString() + "\r\n");
                writer.flush();
            } catch (IOException e) {

            } finally {
                try {
                    if (connect != null) {
                        connect.close();
                    }
                } catch (IOException e) {}
            }
            return null;
        }
    }
}
