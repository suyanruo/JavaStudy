package com.zj.javastudy.network;

import java.net.URI;
import java.net.URISyntaxException;

public class URIExercise {
    public static void printAbsolutePath() {
        try {
            URI absolute = new URI("http://www.wanwan.com/");
            URI relative = new URI("images/logo.png");
            URI resolved = absolute.resolve(relative);
            System.out.println(resolved.toString());

            URI absolute2 = new URI("http://www.wanwan.com/images/logo.png");
            URI top = new URI("http://www.wanwan.com");
            URI relative2 = top.relativize(absolute2);
            System.out.println(relative2.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
