package com.zj.javastudy.network;

public class Main {
    public static void main(String[] args) {
//        SocketExercise.printDictionary();
        HTTPSClient.sendSslRequest("www.usps.com", 443);
    }
}
