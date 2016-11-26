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
import java.util.List;
import java.util.Objects;
import java.util.Random;
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
        try {
            while (!stopThread) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message msg = (Message) in.readObject();
                switch (msg.getMessageType()) {
                    case Util.LOGIN: {
                        login(msg.getMessage().toString());
                        break;
                    }
                    case Util.PLAY: {
                        boolean stop = play();
                        if (stop) {
                            stopThread = true;
                            Kontroler.getInstance().getClientThreadsQueue().remove(this);
                        }
                        break;
                    }
                    case Util.QUIT: {
                        stopThread = true;
                        Kontroler.getInstance().remove(this);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Kontroler.getInstance().remove(this);
        }
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
            out = new ObjectOutputStream(socket.getOutputStream());
            msg = new Message(Util.LOGIN, null);
            out.writeObject(msg);

        }
    }

    private boolean play() throws IOException {
        ObjectOutputStream out;
        Message msg;
        Kontroler.getInstance().getClientThreadsQueue().add(this);
        System.out.println(Kontroler.getInstance().getClientThreadsQueue().size());
        if (Kontroler.getInstance().getClientThreadsQueue().size() < 2 && player.getPlayingWith() == null) {
            for (int i = 0; i < 10; i++) {
                if (Kontroler.getInstance().getClientThreadsQueue().size() < 2 && player.getPlayingWith() == null) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (player.getPlayingWith() != null) {
            return true;
        }
        ClientThread ct = Kontroler.getInstance().findAPlayer(this);

        if (ct == null && player.getPlayingWith() == null) {
            out = new ObjectOutputStream(socket.getOutputStream());
            msg = new Message(Util.ERROR, "There are now online players");
            out.writeObject(msg);
            return false;
        }
        if (player.getPlayingWith() != null) {
            return true;
        }
        int rand = new Random().nextInt(1);
        if (rand == 0) {
            GameThread game = new GameThread(this, ct);
            game.start();
        } else {
            GameThread game = new GameThread(ct, this);
            game.start();
        }
        return true;

    }

}
