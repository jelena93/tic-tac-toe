/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import kontroler.Kontroler;

/**
 *
 * @author Jelena
 */
public class FrmLogin extends javax.swing.JFrame implements IWindowShowMessages {

    boolean poslaoZahtev = false;

    public FrmLogin() throws IOException {
        initComponents();
        connect();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbtnLogin = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jtxtUsername = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setResizable(false);

        jbtnLogin.setText("Login");
        jbtnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnLoginActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Username:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(jLabel2)
                                                .addGap(18, 18, 18)
                                                .addComponent(jtxtUsername))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap(304, Short.MAX_VALUE)
                                                .addComponent(jbtnLogin)))
                                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jtxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(61, 61, 61)
                                .addComponent(jbtnLogin)
                                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLoginActionPerformed
        String username = jtxtUsername.getText().trim();
        if (!username.isEmpty()) {
            try {
                Kontroler.getInstance().login(this, username);
            } catch (IOException ex) {
                showErrorMessage("Error", ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Enter your username");
        }

    }//GEN-LAST:event_jbtnLoginActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FrmLogin().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FrmLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton jbtnLogin;
    private javax.swing.JTextField jtxtUsername;
    // End of variables declaration//GEN-END:variables

    private void connect() {
        try {
            Kontroler.getInstance().connect();
        } catch (Exception ex) {
            showErrorMessage("Error", ex.getMessage());
            System.exit(0);
        }

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
    public int showQuestionMessage(String title, String message) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

}
