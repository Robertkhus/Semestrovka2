//package org.example.semestrovka2.server;
//
//import java.net.ServerSocket;
//
//public class Server {
//    private static final int PORT = 5000;
//
//    public static void main(String[] args) {
//        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
//            System.out.println("Server started...");
//            while (true) {
//                new Handler(serverSocket.accept()).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
