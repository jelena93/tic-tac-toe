/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import domain.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

/**
 *
 * @author Jelena
 */
public class GameThread extends Thread {

    private final ClientThread playerOneThread;
    private final ClientThread playerTwoThread;
    private final String[][] matrix;
    private String value = "";
    private boolean gameEnd;

    public GameThread(ClientThread playerOneThread, ClientThread playerTwoThread) {
        this.playerOneThread = playerOneThread;
        this.playerTwoThread = playerTwoThread;
        this.matrix = new String[3][3];
    }

    @Override
    public void run() {
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                matrix[col][row] = "n";
            }
        }
        playerOneThread.getPlayer().setMark("x");
        playerTwoThread.getPlayer().setMark("o");
        playerOneThread.getPlayer().setPlayersTurn(true);
        playerOneThread.getPlayer().setPlayingWith(playerTwoThread.getPlayer());
        playerTwoThread.getPlayer().setPlayingWith(playerOneThread.getPlayer());

        try {
            send(new Message(Util.START, playerOneThread.getPlayer()), playerOneThread.getSocket());
            send(new Message(Util.START, playerTwoThread.getPlayer()), playerTwoThread.getSocket());
        } catch (IOException ex) {
            Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        Message msg;
        while (true) {
            try {
                ObjectInputStream in = new ObjectInputStream(playerOneThread.getSocket().getInputStream());
                msg = (Message) in.readObject();
                if (msg.getMessageType() == Util.QUIT) {

                }
                playerOneThread.getPlayer().setPosition((int) msg.getMessage());
                send(new Message(Util.MOVE, playerTwoThread.getPlayer()), playerTwoThread.getSocket());
                setValue((int) msg.getMessage(), playerOneThread.getPlayer().getMark());
                check();
                if (gameEnd) {
                    send(new Message(Util.END, Util.WIN_MSG), playerOneThread.getSocket());
                    send(new Message(Util.END, Util.LOOSE_MSG), playerTwoThread.getSocket());
                } else {
                    boolean isTied = checkIfTied();
                    if (isTied) {
                        send(new Message(Util.END, Util.TIED_MSG), playerOneThread.getSocket());
                        send(new Message(Util.END, Util.TIED_MSG), playerTwoThread.getSocket());
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ObjectInputStream in = new ObjectInputStream(playerTwoThread.getSocket().getInputStream());
                msg = (Message) in.readObject();
                playerTwoThread.getPlayer().setPosition((int) msg.getMessage());
                send(new Message(Util.MOVE, playerOneThread), playerOneThread.getSocket());
                setValue((int) msg.getMessage(), playerTwoThread.getPlayer().getMark());
                check();
                if (gameEnd) {
                    send(new Message(Util.END, Util.WIN_MSG), playerTwoThread.getSocket());
                    send(new Message(Util.END, Util.LOOSE_MSG), playerOneThread.getSocket());
                } else {
                    boolean isTied = checkIfTied();
                    if (isTied) {
                        send(new Message(Util.END, Util.TIED_MSG), playerOneThread.getSocket());
                        send(new Message(Util.END, Util.TIED_MSG), playerTwoThread.getSocket());
                    }
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setValue(int position, String mark) {
        switch (position) {
            case 1:
                matrix[0][0] = mark;
                break;
            case 2:
                matrix[0][1] = mark;
                break;
            case 3:
                matrix[0][2] = mark;
                break;
            case 4:
                matrix[1][0] = mark;
                break;
            case 5:
                matrix[1][1] = mark;
                break;
            case 6:
                matrix[1][2] = mark;
                break;
            case 7:
                matrix[2][0] = mark;
                break;
            case 8:
                matrix[2][1] = mark;
                break;
            case 9:
                matrix[2][2] = mark;
                break;
        }
    }

    public void check() {
        value = matrix[1][1];
        if (!value.equals("n")) {
            if (value.equals(matrix[0][0]) && value.equals(matrix[2][2])) {
                gameEnd = true;
                return;
            }
            if (value.equals(matrix[2][0]) && value.equals(matrix[0][2])) {
                gameEnd = true;
                return;
            }
            if (value.equals(matrix[0][1]) && value.equals(matrix[2][1])) {
                gameEnd = true;
                return;
            }
            if (value.equals(matrix[1][0]) && value.equals(matrix[1][2])) {
                gameEnd = true;
                return;
            }
        }

        value = matrix[1][0];
        if (!value.equals("n")) {
            if (value.equals(matrix[0][0]) && value.equals(matrix[2][0])) {
                gameEnd = true;
                return;
            }
            if (value.equals(matrix[1][1]) && value.equals(matrix[1][2])) {
                gameEnd = true;
                return;
            }
        }

        value = matrix[1][2];
        if (!value.equals("n")) {
            if (value.equals(matrix[0][2]) && value.equals(matrix[2][2])) {
                gameEnd = true;
                return;
            }
            if (value.equals(matrix[1][1]) && value.equals(matrix[1][0])) {
                gameEnd = true;
            }
        }
    }

    private void send(Message msg, Socket socket) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(msg);
    }

    private boolean checkIfTied() {
        int br = 0;
        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (matrix[col][row].equals("x") || matrix[col][row].equals("o")) {
                    br++;
                }
            }
        }
        return br == 9;
    }

}
