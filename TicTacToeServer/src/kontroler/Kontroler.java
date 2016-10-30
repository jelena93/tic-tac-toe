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
    private List<ClientThread> clientThreads;
    private List<GameThread> games;

    private Kontroler() {
        clientThreads = new ArrayList<>();
        games = new ArrayList<>();
    }

    public List<ClientThread> getClientThreads() {
        return clientThreads;
    }

    public static Kontroler getInstance() {
        if (instance == null) {
            instance = new Kontroler();
        }
        return instance;
    }

    public void addPlayerThread(ClientThread clientThread) {
        clientThreads.add(clientThread);
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

}
