/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelena
 */
public class ServerThread extends Thread {

    private Socket soket;
    private String ipAdress;
    private boolean igra = false;
    private boolean poslaoZahtev = false;
    PrintStream izlazniTok;
    BufferedReader ulazniTok;
    String username;

    public ServerThread(Socket soket, String ipAdress) {
        this.soket = soket;
        this.ipAdress = ipAdress;
    }

    @Override
    public void run() {
        try {
            izlazniTok = new PrintStream(soket.getOutputStream());
            ulazniTok = new BufferedReader(new InputStreamReader(
                    soket.getInputStream()));
            String odg = ulazniTok.readLine();
            if (odg.startsWith("play")) {
                poslaoZahtev = true;
                username = odg.substring(4);
                while (true) {
                    if (igra) {
                        break;
                    }
                    if (Server.igraci.size() % 2 == 0) {
                        for (ServerThread igrac : Server.igraci) {
                            if (igrac.isPoslaoZahtev()) {
                                izlazniTok.println("Ok");
                                igrac.izlazniTok.println("prvi");
                                new GameThread(igrac, this).start();
                                igra = true;
                                igrac.setIgra(true);
                                Server.igraci.remove(this);
                                Server.igraci.remove(igrac);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerThread) {
            ServerThread s = (ServerThread) obj;
            if (s.getIpAdress().equals(ipAdress) && s.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
