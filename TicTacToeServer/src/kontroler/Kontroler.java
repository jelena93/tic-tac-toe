/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroler;

import domain.Player;
import java.util.ArrayList;
import java.util.List;
import server.ClientThread;
import server.GameThread;

/**
 *
 * @author Jelena
 */
public class Kontroler {

    private static Kontroler instance;
    private final List<ClientThread> clientThreads;
    private final List<ClientThread> clientThreadsQueue;
    private final List<GameThread> games;

    private Kontroler() {
        clientThreads = new ArrayList<>();
        clientThreadsQueue = new ArrayList<>();
        games = new ArrayList<>();
    }

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public List<ClientThread> getClientThreadsQueue() {
        return clientThreadsQueue;
    }

    public static Kontroler getInstance() {
        if (instance == null) {
            instance = new Kontroler();
        }
        return instance;
    }

    public void addClientThread(ClientThread clientThread) {
        clientThreads.add(clientThread);
    }

    public void addClientThreadToQueue(ClientThread clientThread) {
        clientThreadsQueue.add(clientThread);
    }

    public ClientThread findClientThread(Player player) {
        for (ClientThread clientThread : clientThreads) {
            if (clientThread.getPlayer().equals(player)) {
                return clientThread;
            }
        }
        return null;
    }

    public void remove(ClientThread playerThread) {
        clientThreads.remove(playerThread);
    }

    public synchronized ClientThread findAPlayer(ClientThread ct) {
        System.out.println("***" + ct.getPlayer().getUsername());
        for (int i = 0; i < clientThreadsQueue.size(); i++) {
            ClientThread clientThread = clientThreadsQueue.get(i);
            if (!clientThread.equals(ct)) {
                clientThreadsQueue.remove(clientThread);
                return clientThread;
            }
        }
        return null;
    }

    public void recreateClientThread(ClientThread ct) {
        clientThreads.remove(ct);
        ClientThread newCT = new ClientThread(ct.getSocket());
        newCT.getPlayer().setUsername(ct.getPlayer().getUsername());
        clientThreads.add(newCT);
        newCT.start();
    }

}
