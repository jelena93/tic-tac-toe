/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import kontroler.Kontroler;

/**
 *
 * @author Jelena
 */
public class Panel extends javax.swing.JPanel implements MouseListener {

    private boolean played = false;
    private final int position;
    private String playerMove;

    public Panel(int position) {
        this.position = position;
        prepareForm();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        int r = 80;
        g2d.setStroke(new BasicStroke(3));

        g.setColor(Color.white);
        if (position == 1 || position == 2 || position == 4 || position == 5) {
            g.drawLine(getWidth(), 0, getWidth(), getHeight());
            g.drawLine(0, getHeight(), getWidth(), getHeight());
        } else {
            if (position == 3 || position == 6) {
                g.drawLine(0, getHeight(), getWidth(), getHeight());
            } else {
                if (position == 7 || position == 8) {
                    g.drawLine(getWidth(), 0, getWidth(), getHeight());
                }
            }
        }
        if (played) {
            if (Kontroler.getInstance().getPlayer().getMark().equals("x")) {
                g2d.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
                g2d.drawLine(10, getHeight() - 10, getWidth() - 10, 10);
            } else {
                g2d.drawOval((getWidth() - r) / 2, (getHeight() - r) / 2, r, r);
            }
        }
        if (playerMove != null) {
            if (playerMove.equals("x")) {
                g2d.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
                g2d.drawLine(10, getHeight() - 10, getWidth() - 10, 10);
            } else {
                g2d.drawOval((getWidth() - r) / 2, (getHeight() - r) / 2, r, r);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void mouseClicked(MouseEvent e) {
        if (Kontroler.getInstance().getPlayer().isPlayersTurn() && !played && playerMove == null) {
            try {
                Kontroler.getInstance().sendMove(position);
                played = true;
                repaint();
            } catch (IOException ex) {
                Kontroler.getInstance().showErrorMessage(ex.getMessage());
            }
        }
    }

    public void draw(String potez) {
        playerMove = potez;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void prepareForm() {
        setPreferredSize(new Dimension(30, 30));
        addMouseListener(this);
    }
}
