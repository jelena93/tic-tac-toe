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
import java.util.List;
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
        while (true) {
            try {
                ObjectInputStream in = new ObjectInputStream(Kontroler.getInstance().getSocket().getInputStream());
                Message msg = (Message) in.readObject();

                switch (msg.getMessageType()) {
                    case Util.ERROR: {
                        Kontroler.getInstance().showErrorMessage(msg.getMessage().toString());
                        break;
                    }
                    case Util.LOGIN: {
                        Kontroler.getInstance().showMainWindow((List<Player>) msg.getMessage());
                        break;
                    }
                    case Util.PLAY_REQUEST: {
                        Kontroler.getInstance().showQuestionMessage(msg.getMessage().toString());
                        break;
                    }
                    case Util.ACCEPT: {
                        Kontroler.getInstance().startGame((Player) msg.getMessage());
                        break;
                    }
                    case Util.REJECT: {
                        Kontroler.getInstance().showMessage(msg.getMessage().toString());
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(CommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
