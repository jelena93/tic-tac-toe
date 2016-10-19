/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jelena
 */
public class Server {

    public static List<ServerThread> igraci = new ArrayList<ServerThread>();

    private static int port = 8888;

    public static void main(String[] args) throws IOException {
        Socket soket;

        ServerSocket serverSoket = new ServerSocket(port);
        while (true) {
            soket = serverSoket.accept();
            ServerThread igrac = new ServerThread(soket, soket.getInetAddress().toString());
            igraci.add(igrac);
            igrac.start();
        }
    }
}
