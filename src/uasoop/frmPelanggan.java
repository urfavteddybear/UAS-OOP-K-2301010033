/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uasoop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wahyu
 */
public class frmPelanggan extends javax.swing.JFrame {

    Connection koneksi;
    DefaultTableModel model;
    int selectedId = 0;

    /**
     * Creates new form frmPelanggan
     */    public frmPelanggan() {
        initComponents();
        koneksi = dbkoneksi.koneksi();
        model = (DefaultTableModel) tblPelanggan.getModel();
        loadData();
          // Set initial button and field states
        fieldisEnabled(false);
        tombolisEnabled(false);
        btnTambah.setEnabled(true);
        btnTutup.setEnabled(true);
    }void loadData() {
        model.setRowCount(0);
        try {
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pelanggan ORDER BY id_pelanggan");
            while (rs.next()) {                model.addRow(new Object[]{
                    rs.getInt("id_pelanggan"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("alamat"),
                    rs.getString("telepon"),
                    rs.getString("email") != null ? rs.getString("email") : ""
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }    void clearForm() {
        txNama.setText("");
        txAlamat.setText("");
        txTelp.setText("");
        txEmail.setText("");
        selectedId = 0;
        fieldisEnabled(false);
        tombolisEnabled(false);
        btnTambah.setEnabled(true);
        tblPelanggan.clearSelection();
    }
    
    private void clearFormFields() {
        txNama.setText("");
        txAlamat.setText("");
        txTelp.setText("");
        txEmail.setText("");
    }

    private void fieldisEnabled(boolean opsi) {
        txNama.setEditable(opsi);
        txAlamat.setEditable(opsi);
        txTelp.setEditable(opsi);
        txEmail.setEditable(opsi);
    }
      private void tombolisEnabled(boolean opsi) {
        btnTambah.setEnabled(opsi);
        btnUbah.setEnabled(opsi);
        btnHapus.setEnabled(opsi);
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
        tblPelanggan = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txNama = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txAlamat = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txTelp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txEmail = new javax.swing.JTextField();
        btnTambah = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Pelanggan");
        setResizable(false);

        tblPelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nama", "Alamat", "Telepon", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPelangganMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblPelanggan);

        jLabel1.setText("Nama:");

        jLabel2.setText("Alamat:");

        jLabel3.setText("Telepon:");

        jLabel4.setText("Email:");

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Data Pelanggan");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(txNama)
                            .addComponent(txAlamat)
                            .addComponent(txTelp)
                            .addComponent(txEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTambah)
                            .addComponent(btnUbah)
                            .addComponent(btnHapus)
                            .addComponent(btnTutup))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUbah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTutup))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        if (btnTambah.getText().equals("Tambah")) {
            // Enable input mode
            fieldisEnabled(true);
            btnTambah.setText("Simpan");
            btnUbah.setEnabled(false);
            btnHapus.setEnabled(false);
            clearFormFields();
        } else {
            // Save mode
            String nama = txNama.getText();
            String alamat = txAlamat.getText();
            String telp = txTelp.getText();
            String email = txEmail.getText();
            
            if (nama.isEmpty() || alamat.isEmpty() || telp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama, alamat, dan telepon harus diisi!");
                return;
            }
            
            try {
                PreparedStatement ps = koneksi.prepareStatement(
                        "INSERT INTO pelanggan (nama_pelanggan, alamat, telepon, email) VALUES (?, ?, ?, ?)");
                ps.setString(1, nama);
                ps.setString(2, alamat);
                ps.setString(3, telp);
                ps.setString(4, email);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
                clearForm();
                loadData();
                btnTambah.setText("Tambah");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnTambahActionPerformed
    
    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if (btnUbah.getText().equals("Ubah")) {
            // Check if data is selected
            if (selectedId == 0) {
                JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
                return;
            }
            // Enable edit mode
            fieldisEnabled(true);
            btnUbah.setText("Simpan");
            btnTambah.setEnabled(false);
            btnHapus.setEnabled(false);
        } else {
            // Save mode
            String nama = txNama.getText();
            String alamat = txAlamat.getText();
            String telp = txTelp.getText();
            String email = txEmail.getText();

            if (nama.isEmpty() || alamat.isEmpty() || telp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama, alamat, dan telepon harus diisi!");
                return;
            }
            
            try {
                PreparedStatement ps = koneksi.prepareStatement(
                        "UPDATE pelanggan SET nama_pelanggan=?, alamat=?, telepon=?, email=? WHERE id_pelanggan=?");
                ps.setString(1, nama);
                ps.setString(2, alamat);
                ps.setString(3, telp);
                ps.setString(4, email);
                ps.setInt(5, selectedId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
                clearForm();
                loadData();
                btnUbah.setText("Ubah");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        if (selectedId == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement ps = koneksi.prepareStatement("DELETE FROM pelanggan WHERE id_pelanggan=?");
                ps.setInt(1, selectedId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                clearForm();
                loadData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        dispose();
    }//GEN-LAST:event_btnTutupActionPerformed
    
    private void tblPelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPelangganMouseClicked
        int row = tblPelanggan.getSelectedRow();
        if (row >= 0) {
            selectedId = (Integer) model.getValueAt(row, 0);
            txNama.setText(model.getValueAt(row, 1).toString());
            txAlamat.setText(model.getValueAt(row, 2).toString());
            txTelp.setText(model.getValueAt(row, 3).toString());
            txEmail.setText(model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "");
            
            // Enable Ubah and Hapus buttons when data is selected
            btnUbah.setEnabled(true);
            btnHapus.setEnabled(true);
        }
    }//GEN-LAST:event_tblPelangganMouseClicked

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmPelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmPelanggan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton btnUbah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPelanggan;
    private javax.swing.JTextField txAlamat;
    private javax.swing.JTextField txEmail;
    private javax.swing.JTextField txNama;
    private javax.swing.JTextField txTelp;
    // End of variables declaration//GEN-END:variables
}
