package com.zj.javastudy.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class InternetExercise {
    public static void printInetAddress() {
        try {
            InetAddress address = InetAddress.getByName("www.baidu.com");
            System.out.println(address);
            InetAddress me = InetAddress.getLocalHost();
            System.out.println(me);
        } catch (UnknownHostException e) {
            System.out.println("Could not find the host");
        }
    }

    public static void printNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                System.out.println(ni);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void printNetAddress() {
        try {
            NetworkInterface en0 = NetworkInterface.getByName("en0");
            Enumeration addresses = en0.getInetAddresses();
            while (addresses.hasMoreElements()) {
                System.out.println(addresses.nextElement());
            }
        } catch (SocketException e) {

        }
    }
}
