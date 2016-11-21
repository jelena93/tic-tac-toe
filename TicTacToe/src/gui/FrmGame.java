/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import kontroler.Kontroler;

/**
 *
 * @author Jelena
 */
public class FrmGame extends javax.swing.JFrame implements IWindowShowMessages {

    private Panel[] panels;

    public FrmGame() {
        initComponents();
        fillPanels();
        jlblUsername.setText(Kontroler.getInstance().getPlayer().getUsername() + " "
                + Kontroler.getInstance().getPlayer().getMark());
    }

    public void setResult(String res) {
        jlblResult.setText(res);
    }

    public void draw(String move, int position) {
        panels[position - 1].draw(move);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpGame = new javax.swing.JPanel();
        jlblUsername = new javax.swing.JLabel();
        jlblResult = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setResizable(false);

        jpGame.setLayout(new java.awt.GridLayout(3, 3));

        jlblUsername.setText("jLabel2");
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jpGame, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jlblUsername)
                                        .addComponent(jlblResult))
                                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(37, Short.MAX_VALUE)
                                .addComponent(jlblUsername)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jlblResult)
                                .addGap(27, 27, 27)
                                .addComponent(jpGame, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jlblResult;
    private javax.swing.JLabel jlblUsername;
    private javax.swing.JPanel jpGame;
    // End of variables declaration//GEN-END:variables

    @Override
    public void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showQuestionMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    private void fillPanels() {
        panels = new Panel[9];
        for (int i = 0; i < 9; i++) {
            Panel p = new Panel((i + 1));
            jpGame.add(p);
            panels[i] = p;
            p.repaint();
        }
    }

    private void formWindowClosing(WindowEvent evt) {
        try {
            Kontroler.getInstance().quit();
        } catch (IOException ex) {
            Logger.getLogger(FrmGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
