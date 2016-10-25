/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroler;

import java.util.ArrayList;
import java.util.List;
import server.ClientThread;

/**
 *
 * @author Jelena
 */
public class Kontroler {

    private static Kontroler instance;
    private List<ClientThread> clientThreads;

    private Kontroler() {
        clientThreads = new ArrayList<>();
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

}
