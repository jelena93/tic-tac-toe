/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroler;

import domain.Message;
import domain.Player;
import gui.FrmGame;
import gui.FrmMain;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
    private List<Player> players;
    private IWindowShowMessages window;
    
    private Kontroler() {
        ipAddress = "localhost";
        players = new ArrayList<>();
        player = new Player();
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
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
    }
    
    public void sendRequest(IWindowShowMessages window, String player) throws Exception {
        this.window = window;
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(new Message(Util.PLAY_REQUEST, player));
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
    
    public void showMainWindow(List<Player> players) {
        this.players = players;
        ((JFrame) window).dispose();
        FrmMain frmMain = new FrmMain();
        window = frmMain;
        frmMain.setVisible(true);
    }
    
    public void sendResponse(int response) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        if (response == 0) {
            out.writeObject(new Message(Util.ACCEPT, null));
        } else {
            out.writeObject(new Message(Util.REJECT, null));
        }
    }
    
    public void startGame(Player message) {
        player.setPlayingWith(player);
        ((JFrame) window).dispose();
        window = new FrmGame();
        ((JFrame) window).setVisible(true);
    }
    
}
