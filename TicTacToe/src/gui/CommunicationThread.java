/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jelena
 */
public class CommunicationThread extends Thread {

    Socket soketZaKomunikaciju;
    PrintStream izlazniTok;
    BufferedReader ulazniTok;
    Main glavna;
    Game f;
    String username;

    public CommunicationThread(Main glavna, String username) {
        this.glavna = glavna;
        this.username = username;
    }

    @Override
    public void run() {
        try {
            soketZaKomunikaciju = new Socket("localhost", 8888);
            izlazniTok = new PrintStream(soketZaKomunikaciju
                    .getOutputStream());
            ulazniTok = new BufferedReader(new InputStreamReader(
                    soketZaKomunikaciju.getInputStream()));
            izlazniTok.println("play" + username);

            while (true) {
                String odg = ulazniTok.readLine();
                if (odg.equals("Ok")) {
                    f = new Game(izlazniTok, ulazniTok, "o", username);
                    glavna.setVisible(false);
                    f.setVisible(true);
                } else {
                    if (odg.equals("prvi")) {
                        f = new Game(izlazniTok, ulazniTok, "x", username);
                        glavna.setVisible(false);
                        f.setVisible(true);
                    } else {
                        if (odg.startsWith("end")) {
                            if (odg.endsWith("loose")) {
                                f.setResult("You lost");
                                JOptionPane.showMessageDialog(null, "You lost");
                                f.dispose();
                                glavna.setVisible(true);
                                glavna.poslaoZahtev = false;
                            } else {
                                if (odg.endsWith("win")) {
                                    f.setResult("You won");
                                    JOptionPane.showMessageDialog(null, "You won");
                                    f.dispose();
                                    glavna.setVisible(true);
                                    glavna.poslaoZahtev = false;
                                } else {
                                    f.setResult("Tie");
                                    JOptionPane.showMessageDialog(null, "Tie");
                                    f.dispose();
                                    glavna.setVisible(true);
                                    glavna.poslaoZahtev = false;
                                }

                            }

                        } else {
                            f.nacrtaj(odg.split("/")[0], Integer.parseInt(odg.split("/")[1]));
                        }

                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
