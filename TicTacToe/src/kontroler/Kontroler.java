/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroler;

import communication.CommunicationThread;
import domain.Message;
import domain.Player;
import gui.FrmGame;
import gui.FrmPlay;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Util;
import gui.IWindowShowMessages;
import javax.swing.JFrame;

/**
 *
 * @author Jelena
 */
public class Kontroler {

    private static Kontroler instance;
    private Socket socket;
    private String ipAddress;
    private Player player;
    private IWindowShowMessages window;

    private Kontroler() {
        ipAddress = "localhost";
        player = new Player();
    }

    public Player getPlayer() {
        return player;
    }

    public Socket getSocket() {
        return socket;
    }

    public static Kontroler getInstance() {
        if (instance == null) {
            instance = new Kontroler();
        }
        return instance;
    }

    public void connect() throws Exception {
        try {
            socket = new Socket(ipAddress, Util.PORT);
        } catch (IOException ex) {
            throw new Exception("Can't connect to server with ip address: " + ipAddress + " and port: " + Util.PORT);
        }
    }

    public void login(IWindowShowMessages window, String username) throws IOException {
        this.window = window;
        player.setUsername(username);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(new Message(Util.LOGIN, username));
        new CommunicationThread().start();
    }

    public void showMessage(String message) {
        window.showMessage("", message);
    }

    public void showErrorMessage(String error) {
        window.showErrorMessage("Error", error);
    }

    public void showQuestionMessage(String question) {
        window.showQuestionMessage("", question);
    }

    public void showMainWindow() {
        ((JFrame) window).dispose();
        FrmPlay frmPlay = new FrmPlay();
        window = frmPlay;
        frmPlay.setVisible(true);
    }

    public void end(String msg) {
        ((FrmGame) window).setResult(msg);
        window.showMessage("End", msg);
        ((JFrame) window).dispose();
        FrmPlay frmPlay = new FrmPlay();
        window = frmPlay;
        frmPlay.setVisible(true);
    }

    public void startGame(Player player) {
        this.player = player;
        ((JFrame) window).dispose();
        window = new FrmGame();
        ((JFrame) window).setVisible(true);
    }

    public void showMove(Player player) {
        this.player = player;
        ((FrmGame) window).draw(player.getPlayingWith().getMark(), player.getPlayingWith().getPosition());
        ((FrmGame) window).setResult("Your move");
        this.player.setPlayersTurn(true);
    }

    public void sendMove(int position) throws IOException {
        player.setPlayersTurn(false);
        ((FrmGame) window).setResult("");
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        Message msg = new Message(Util.MOVE, position);
        out.writeObject(msg);
    }

    public void play() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        Message msg = new Message(Util.PLAY);
        out.writeObject(msg);
    }

    public void quit() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        Message msg = new Message(Util.QUIT);
        out.writeObject(msg);
        ((FrmGame)window).dispose();
        FrmPlay frmPlay = new FrmPlay();
        window = frmPlay;
        frmPlay.setVisible(true);
    }
}
