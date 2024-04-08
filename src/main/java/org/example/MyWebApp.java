package org.example;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;




public class MyWebApp {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        String ctxPath = "/chatrooms";
        String webappDir = new File("src/main/webapp").getAbsolutePath();

        tomcat.addWebapp(ctxPath, webappDir);

        tomcat.start();
        tomcat.getServer().await();
    }
}