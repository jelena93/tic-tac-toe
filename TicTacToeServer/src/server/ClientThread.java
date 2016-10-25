/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import domain.Message;
import domain.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import kontroler.Kontroler;
import util.Util;

/**
 *
 * @author Jelena
 */
public class ClientThread extends Thread {

    private Socket socket;
    private String ipAdress;
    private boolean igra = false;
    private boolean poslaoZahtev = false;
    PrintStream izlazniTok;
    BufferedReader ulazniTok;
    String username;
    private Player player;

    public ClientThread(Socket soket, String ipAdress) {
        this.socket = soket;
        this.ipAdress = ipAdress;
        player = new Player();
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isIgra() {
        return igra;
    }

    public void setIgra(boolean igra) {
        this.igra = igra;
    }

    public boolean isPoslaoZahtev() {
        return poslaoZahtev;
    }

    public void setPoslaoZahtev(boolean poslaoZahtev) {
        this.poslaoZahtev = poslaoZahtev;
    }

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message) in.readObject();
                switch (msg.getMessageType()) {
                    case Util.LOGIN: {
                        login(msg.getMessage().toString());
                        break;
                    }
                    case Util.PLAY_REQUEST: {
                        sendRequest(msg.getMessage().toString());
                        break;
                    }
                    case Util.ACCEPT: {
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        player.setMove("x");
                        player.getPlayingWith().setMove("o");
                        msg = new Message(Util.ACCEPT, player);
                        out.writeObject(msg);
                        break;
                    }
                    case Util.REJECT: {
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        msg = new Message(Util.REJECT, "Player: " + player.getPlayingWith().getUsername() + " doesn't want to play with you");
                        out.writeObject(msg);
                        player.setPlayingWith(null);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


//            if (odg.startsWith("play")) {
//                poslaoZahtev = true;
//                username = odg.substring(4);
//                while (true) {
//                    if (igra) {
//                        break;
//                    }
//                    if (Server.players.size() % 2 == 0) {
//                        for (ClientThread igrac : Server.players) {
//                            if (igrac.isPoslaoZahtev()) {
//                                izlazniTok.println("Ok");
//                                igrac.izlazniTok.println("prvi");
//                                new GameThread(igrac, this).start();
//                                igra = true;
//                                igrac.setIgra(true);
//                                Server.players.remove(this);
//                                Server.players.remove(igrac);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientThread other = (ClientThread) obj;
        if (!Objects.equals(this.ipAdress, other.ipAdress)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    private void login(String username) throws IOException {
        ObjectOutputStream out;
        Message msg;
        boolean exists = false;
        List<ClientThread> clientThreads = Kontroler.getInstance().getClientThreads();
        for (ClientThread clientThread : clientThreads) {
            if (username.equals(clientThread.getPlayer().getUsername())) {
                exists = true;
                msg = new Message(Util.ERROR, "Player with username: " + username + " already exists");
                msg.setMessageType(Util.ERROR);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(msg);
                break;
            }
        }
        if (!exists) {
            player.setUsername(username);
            List<Player> playersCanPlay = new ArrayList<>();
            for (ClientThread clientThread : clientThreads) {
                if (clientThread.getPlayer().isCanPlay() && !clientThread.getPlayer().equals(player)) {
                    playersCanPlay.add(clientThread.getPlayer());
                }
            }
            out = new ObjectOutputStream(socket.getOutputStream());
            msg = new Message(Util.LOGIN, playersCanPlay);
            out.writeObject(msg);

        }
    }

    private void sendRequest(String username) throws IOException {
        ObjectOutputStream out;
        Message msg;
        List<ClientThread> clientThreads = Kontroler.getInstance().getClientThreads();
        for (ClientThread clientThread : clientThreads) {
            if (clientThread.getPlayer().getUsername().equals(username)) {
                Socket s = clientThread.getSocket();
                out = new ObjectOutputStream(s.getOutputStream());
                msg = new Message(Util.PLAY_REQUEST, "Player: " + player.getUsername() + " wants to play with you");
                out.writeObject(msg);
                player.setPlayingWith(clientThread.getPlayer());
                break;
            }
        }
//            msg = new Message(Util.ERROR, "Player with username: " + username + " already exists");
//            msg.setMessageType(Util.ERROR);
//            out = new ObjectOutputStream(socket.getOutputStream());
//            out.writeObject(msg);

    }

   
}
