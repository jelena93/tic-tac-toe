/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import domain.Message;
import domain.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private final Socket socket;
    private Player player;

    public ClientThread(Socket soket) {
        this.socket = soket;
        player = new Player();
    }

    public Socket getSocket() {
        return socket;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        boolean stopThread = false;
        while (!stopThread) {
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
                        ClientThread ct = Kontroler.getInstance().findClientThread(player.getPlayingWith());
                        double num = Math.random();
                        if (num <= 0.5) {
                            GameThread game = new GameThread(this, ct);
                            game.start();
                        } else {
                            GameThread game = new GameThread(ct, this);
                            game.start();
                        }
                        stopThread = true;
                        break;
                    }
                    case Util.REJECT: {
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        msg = new Message(Util.REJECT, "Player: " + player.getPlayingWith().getUsername() + " doesn't want to play with you");
                        out.writeObject(msg);
                        player.setPlayingWith(null);
                        break;
                    }
                    case Util.QUIT: {
                        stopThread = true;
                        Kontroler.getInstance().remove(this);
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public boolean equals(Object obj
    ) {
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
        if (!Objects.equals(this.player, other.player)) {
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
                if (clientThread.getPlayer().getPlayingWith() == null && !clientThread.getPlayer().equals(player)) {
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
    }

}
