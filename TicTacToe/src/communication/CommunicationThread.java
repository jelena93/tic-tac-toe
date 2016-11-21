/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communication;

import domain.Message;
import domain.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import kontroler.Kontroler;
import util.Util;

/**
 *
 * @author Jelena
 */
public class CommunicationThread extends Thread {

    @Override
    public void run() {
        boolean end = false;
        while (!end) {
            try {
                ObjectInputStream in = new ObjectInputStream(Kontroler.getInstance().getSocket().getInputStream());
                Message msg = (Message) in.readObject();

                switch (msg.getMessageType()) {
                    case Util.ERROR: {
                        Kontroler.getInstance().showErrorMessage(msg.getMessage().toString());
                        break;
                    }
                    case Util.LOGIN: {
                        Kontroler.getInstance().showMainWindow();
                        break;
                    }
                    case Util.START: {
                        Kontroler.getInstance().startGame((Player) msg.getMessage());
                        break;
                    }
                    case Util.MOVE: {
                        Player p = (Player) msg.getMessage();
                        System.out.println(p);
                        Kontroler.getInstance().showMove(p);
                        break;
                    }
                    case Util.END: {
                        Kontroler.getInstance().end((String) msg.getMessage());
                        end = true;
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
