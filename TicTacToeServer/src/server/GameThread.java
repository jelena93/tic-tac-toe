/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jelena
 */
public class GameThread extends Thread {

    ClientThread igracJedan;
    ClientThread igracDva;
    BufferedReader ulazniTok;
    PrintStream izlazniTok;
    String matrica[][] = new String[3][3];
    String vr = "";
    boolean kraj = false;

    public GameThread(ClientThread igracJedan, ClientThread igracDva) {
        this.igracJedan = igracJedan;
        this.igracDva = igracDva;
    }

    @Override
    public void run() {
        for (int kolona = 0; kolona < 3; kolona++) {
            for (int red = 0; red < 3; red++) {
                matrica[kolona][red] = "n";
            }
        }
        try {
            while (true) {
                String odg1 = igracJedan.ulazniTok.readLine();
                igracDva.izlazniTok.println(odg1);
                int poz = Integer.parseInt(odg1.split("/")[1]);
                String potez = odg1.split("/")[0];
                postaviVrednost(poz, potez);
                provera();
                if (kraj) {
                    igracJedan.izlazniTok.println("end-win");
                    igracDva.izlazniTok.println("end-loose");
                    break;
                } else {
                    int br = 0;
                    for (int kolona = 0; kolona < 3; kolona++) {
                        for (int red = 0; red < 3; red++) {
                            if (matrica[kolona][red].equals("x") || matrica[kolona][red].equals("o")) {
                                br++;
                            }
                        }
                    }
                    if (br == 9) {
                        igracJedan.izlazniTok.println("end-nereseno");
                        igracDva.izlazniTok.println("end-nereseno");
                        break;
                    }
                }
                String odg2 = igracDva.ulazniTok.readLine();
                igracJedan.izlazniTok.println(odg2);
                poz = Integer.parseInt(odg2.split("/")[1]);
                potez = odg2.split("/")[0];
                postaviVrednost(poz, potez);
                provera();
                if (kraj) {
                    igracDva.izlazniTok.println("end-win");
                    igracJedan.izlazniTok.println("end-loose");
                    break;
                } else {
                    int br = 0;
                    for (int kolona = 0; kolona < 3; kolona++) {
                        for (int red = 0; red < 3; red++) {
                            if (matrica[kolona][red].equals("x") || matrica[kolona][red].equals("o")) {
                                br++;
                            }
                        }
                    }
                    if (br == 9) {
                        igracJedan.izlazniTok.println("end-nereseno");
                        igracDva.izlazniTok.println("end-nereseno");
                        break;
                    }
                }

            }
            igracJedan.setPoslaoZahtev(false);
            igracDva.setPoslaoZahtev(false);
        } catch (Exception e) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, e);

        }
    }

    public void postaviVrednost(int poz, String potez) {
        switch (poz) {
            case 1:
                matrica[0][0] = potez;
                break;
            case 2:
                matrica[0][1] = potez;
                break;
            case 3:
                matrica[0][2] = potez;
                break;
            case 4:
                matrica[1][0] = potez;
                break;
            case 5:
                matrica[1][1] = potez;
                break;
            case 6:
                matrica[1][2] = potez;
                break;
            case 7:
                matrica[2][0] = potez;
                break;
            case 8:
                matrica[2][1] = potez;
                break;
            case 9:
                matrica[2][2] = potez;
                break;
        }
    }

    public void provera() {
        vr = matrica[1][1];
        if (!vr.equals("n")) {
            if (vr.equals(matrica[0][0]) && vr.equals(matrica[2][2])) {
                kraj = true;
                return;
            }
            if (vr.equals(matrica[2][0]) && vr.equals(matrica[0][2])) {
                kraj = true;
                return;
            }
            if (vr.equals(matrica[0][1]) && vr.equals(matrica[2][1])) {
                kraj = true;
                return;
            }
            if (vr.equals(matrica[1][0]) && vr.equals(matrica[1][2])) {
                kraj = true;
                return;
            }
        }

        vr = matrica[1][0];
        if (!vr.equals("n")) {
            if (vr.equals(matrica[0][0]) && vr.equals(matrica[2][0])) {
                kraj = true;
                return;
            }
            if (vr.equals(matrica[1][1]) && vr.equals(matrica[1][2])) {
                kraj = true;
                return;
            }
        }

        vr = matrica[1][2];
        if (!vr.equals("n")) {
            if (vr.equals(matrica[0][2]) && vr.equals(matrica[2][2])) {
                kraj = true;
                return;
            }
            if (vr.equals(matrica[1][1]) && vr.equals(matrica[1][0])) {
                kraj = true;
            }
        }
    }

}
