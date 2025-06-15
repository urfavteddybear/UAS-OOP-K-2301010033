/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package uasoop;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main Application Class - Sistem Manajemen Penyewaan Kamera
 * @author Wahyu
 */
public class UASOOP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Nimbus look and feel
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Could not set Nimbus look and feel: " + e.getMessage());
            }
            
            // Start application with login form
            new frmLogin().setVisible(true);
        });
    }
}
