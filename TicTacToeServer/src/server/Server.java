/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import kontroler.Kontroler;
import util.Util;

/**
 *
 * @author Jelena
 */
public class Server {

    private static Socket socket;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSoket = new ServerSocket(Util.PORT);

        while (true) {
            socket = serverSoket.accept();
            ClientThread player = new ClientThread(socket);
            Kontroler.getInstance().addClientThread(player);
            player.start();
        }
    }
}
