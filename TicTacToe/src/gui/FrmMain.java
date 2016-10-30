/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Player;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import kontroler.Kontroler;

/**
 *
 * @author Jelena
 */
public class FrmMain extends javax.swing.JFrame implements IWindowShowMessages {

    /**
     * Creates new form FrmMain
     */
    public FrmMain() {
        initComponents();
        fillJlist();
        jlOnlinePlayers.addListSelectionListener((ListSelectionEvent e) -> {
            String player = jlOnlinePlayers.getSelectedValue();
            JOptionPane.showMessageDialog(this, player);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jlOnlinePlayers = new javax.swing.JList<>();
        jbtnPlay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jlOnlinePlayers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jlOnlinePlayers);

        jbtnPlay.setText("Play");
        jbtnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPlayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(189, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnPlay)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                .addComponent(jbtnPlay)
                                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPlayActionPerformed
        String player = jlOnlinePlayers.getSelectedValue();
        JOptionPane.showMessageDialog(this, player);
        try {
            Kontroler.getInstance().sendRequest(this, player);
        } catch (Exception ex) {
            showErrorMessage("Error", ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnPlay;
    private javax.swing.JList<String> jlOnlinePlayers;
    // End of variables declaration//GEN-END:variables

    private void fillJlist() {
        List<Player> players = Kontroler.getInstance().getPlayers();
        DefaultListModel<String> model = new DefaultListModel<>();
        for (Player player : players) {
            model.addElement(player.getUsername());
        }
        jlOnlinePlayers.setModel(model);
    }

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
        int response = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
        try {
            Kontroler.getInstance().sendResponse(response);
        } catch (IOException ex) {
            showErrorMessage("Error", ex.getMessage());
        }
    }
}
