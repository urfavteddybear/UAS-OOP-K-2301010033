/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uasoop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Wahyu
 */
public class frmRiwayat extends javax.swing.JFrame {

    Connection koneksi;
    DefaultTableModel model;
    NumberFormat currencyFormat;
    SimpleDateFormat dateFormat;

    /**
     * Creates new form frmRiwayat
     */
    public frmRiwayat() {
        initComponents();
        koneksi = dbkoneksi.koneksi();
        model = (DefaultTableModel) tblRiwayat.getModel();
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        // Set tanggal default (bulan ini)
        Date now = new Date();
        txTanggalSampai.setText(dateFormat.format(now));
        
        Date startOfMonth = new Date(now.getYear(), now.getMonth(), 1);
        txTanggalDari.setText(dateFormat.format(startOfMonth));
        
        loadData();
        
        // Set lebar kolom agar proporsional
        tblRiwayat.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tblRiwayat.getColumnModel().getColumn(1).setPreferredWidth(150); // Pelanggan
        tblRiwayat.getColumnModel().getColumn(2).setPreferredWidth(100); // Tanggal Sewa
        tblRiwayat.getColumnModel().getColumn(3).setPreferredWidth(100); // Tanggal Kembali
        tblRiwayat.getColumnModel().getColumn(4).setPreferredWidth(70);  // Durasi
        tblRiwayat.getColumnModel().getColumn(5).setPreferredWidth(120); // Total
        tblRiwayat.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        tblRiwayat.getColumnModel().getColumn(7).setPreferredWidth(100); // User
    }

    void loadData() {
        model.setRowCount(0);
        try {
            String sql = "SELECT t.id_transaksi, p.nama_pelanggan, t.tanggal_sewa, t.tanggal_kembali, " +
                        "t.durasi, t.total_harga, t.status, u.nama " +
                        "FROM transaksi t " +
                        "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                        "JOIN users u ON t.id_user = u.id_user " +
                        "ORDER BY t.id_transaksi DESC";
            
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            int totalTransaksi = 0;
            double totalPendapatan = 0;
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_transaksi"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("tanggal_sewa"),
                    rs.getString("tanggal_kembali"),
                    rs.getInt("durasi") + " hari",
                    currencyFormat.format(rs.getDouble("total_harga")),
                    rs.getString("status"),
                    rs.getString("nama")
                });
                
                totalTransaksi++;
                totalPendapatan += rs.getDouble("total_harga");
            }
            
            lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
            lblTotalPendapatan.setText(currencyFormat.format(totalPendapatan));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

    void cariData() {
        String tanggalDari = txTanggalDari.getText();
        String tanggalSampai = txTanggalSampai.getText();
        
        if (tanggalDari.isEmpty() || tanggalSampai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal pencarian!");
            return;
        }
        
        model.setRowCount(0);
        try {
            String sql = "SELECT t.id_transaksi, p.nama_pelanggan, t.tanggal_sewa, t.tanggal_kembali, " +
                        "t.durasi, t.total_harga, t.status, u.nama " +
                        "FROM transaksi t " +
                        "JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan " +
                        "JOIN users u ON t.id_user = u.id_user " +
                        "WHERE t.tanggal_sewa BETWEEN ? AND ? " +
                        "ORDER BY t.id_transaksi DESC";
            
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setString(1, tanggalDari);
            ps.setString(2, tanggalSampai);
            ResultSet rs = ps.executeQuery();
            
            int totalTransaksi = 0;
            double totalPendapatan = 0;
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_transaksi"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("tanggal_sewa"),
                    rs.getString("tanggal_kembali"),
                    rs.getInt("durasi") + " hari",
                    currencyFormat.format(rs.getDouble("total_harga")),
                    rs.getString("status"),
                    rs.getString("nama")
                });
                
                totalTransaksi++;
                totalPendapatan += rs.getDouble("total_harga");
            }
            
            lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
            lblTotalPendapatan.setText(currencyFormat.format(totalPendapatan));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    void lihatDetail() {
        int row = tblRiwayat.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi untuk melihat detail!");
            return;
        }
        
        int idTransaksi = (int) model.getValueAt(row, 0);
        
        try {
            String sql = "SELECT b.nama_barang, dt.jumlah, dt.harga_satuan, dt.subtotal " +
                        "FROM detail_transaksi dt " +
                        "JOIN barang b ON dt.id_barang = b.id_barang " +
                        "WHERE dt.id_transaksi = ?";
            
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();
            
            StringBuilder detail = new StringBuilder();
            detail.append("DETAIL TRANSAKSI #").append(idTransaksi).append("\n\n");
            
            while (rs.next()) {
                detail.append("Barang: ").append(rs.getString("nama_barang")).append("\n");
                detail.append("Jumlah: ").append(rs.getInt("jumlah")).append("\n");
                detail.append("Harga: ").append(currencyFormat.format(rs.getDouble("harga_satuan"))).append("\n");
                detail.append("Subtotal: ").append(currencyFormat.format(rs.getDouble("subtotal"))).append("\n\n");
            }
            
            JOptionPane.showMessageDialog(this, detail.toString(), "Detail Transaksi", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    void kembalikanBarang() {
        int row = tblRiwayat.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan ditandai sebagai selesai!");
            return;
        }
        
        int idTransaksi = (int) model.getValueAt(row, 0);
        String status = (String) model.getValueAt(row, 6); // Kolom status
        
        if ("Selesai".equals(status)) {
            JOptionPane.showMessageDialog(this, "Transaksi ini sudah selesai!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah yakin menandai transaksi ini sebagai SELESAI?\n" +
            "Stok barang akan dikembalikan ke inventory.", 
            "Konfirmasi Pengembalian", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Mulai transaction
                koneksi.setAutoCommit(false);
                
                // 1. Update status transaksi menjadi 'Selesai'
                String updateTransaksi = "UPDATE transaksi SET status = 'Selesai' WHERE id_transaksi = ?";
                PreparedStatement ps1 = koneksi.prepareStatement(updateTransaksi);
                ps1.setInt(1, idTransaksi);
                ps1.executeUpdate();
                
                // 2. Kembalikan stok barang
                String getDetails = "SELECT id_barang, jumlah FROM detail_transaksi WHERE id_transaksi = ?";
                PreparedStatement ps2 = koneksi.prepareStatement(getDetails);
                ps2.setInt(1, idTransaksi);
                ResultSet rs = ps2.executeQuery();
                
                while (rs.next()) {
                    int idBarang = rs.getInt("id_barang");
                    int jumlah = rs.getInt("jumlah");
                    
                    // Update stok barang (menambah kembali)
                    String updateStok = "UPDATE barang SET stok = stok + ? WHERE id_barang = ?";
                    PreparedStatement ps3 = koneksi.prepareStatement(updateStok);
                    ps3.setInt(1, jumlah);
                    ps3.setInt(2, idBarang);
                    ps3.executeUpdate();
                }
                
                // Commit transaction
                koneksi.commit();
                koneksi.setAutoCommit(true);
                
                JOptionPane.showMessageDialog(this, "Pengembalian berhasil! Stok barang telah dikembalikan.");
                loadData(); // Refresh data
                
            } catch (Exception e) {
                try {
                    koneksi.rollback();
                    koneksi.setAutoCommit(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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

        jLabel1 = new javax.swing.JLabel();
        txTanggalDari = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txTanggalSampai = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRiwayat = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        lblTotalTransaksi = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalPendapatan = new javax.swing.JLabel();
        btnDetail = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btnKembalikan = new javax.swing.JButton();        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Riwayat Transaksi");
        setResizable(false);

        jLabel1.setText("Dari Tanggal:");

        jLabel2.setText("Sampai:");

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        tblRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Pelanggan", "Tanggal Sewa", "Tanggal Kembali", "Durasi", "Total", "Status", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblRiwayat);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Total Transaksi:");

        lblTotalTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalTransaksi.setText("0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Total Pendapatan:");

        lblTotalPendapatan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTotalPendapatan.setText("Rp 0");

        btnDetail.setText("Lihat Detail");
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailActionPerformed(evt);
            }
        });

        btnTutup.setText("Tutup");
        btnTutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTutupActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("RIWAYAT TRANSAKSI");

        btnKembalikan.setText("Kembalikan Barang");
        btnKembalikan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembalikanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txTanggalDari, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txTanggalSampai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCari)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRefresh)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalTransaksi)
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalPendapatan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDetail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnKembalikan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTutup))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txTanggalDari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txTanggalSampai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(btnRefresh))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalTransaksi)
                    .addComponent(jLabel4)
                    .addComponent(lblTotalPendapatan)
                    .addComponent(btnDetail)
                    .addComponent(btnTutup)
                    .addComponent(btnKembalikan))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
        
        // Set minimum size untuk memastikan semua tombol terlihat
        setMinimumSize(new java.awt.Dimension(850, 500));
    }// </editor-fold>//GEN-END:initComponents

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        cariData();
    }//GEN-LAST:event_btnCariActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
        lihatDetail();
    }//GEN-LAST:event_btnDetailActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTutupActionPerformed
        dispose();
    }//GEN-LAST:event_btnTutupActionPerformed

    private void btnKembalikanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembalikanActionPerformed
        kembalikanBarang();
    }//GEN-LAST:event_btnKembalikanActionPerformed

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
            java.util.logging.Logger.getLogger(frmRiwayat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmRiwayat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmRiwayat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmRiwayat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRiwayat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnKembalikan;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTutup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalPendapatan;
    private javax.swing.JLabel lblTotalTransaksi;
    private javax.swing.JTable tblRiwayat;
    private javax.swing.JTextField txTanggalDari;
    private javax.swing.JTextField txTanggalSampai;
    // End of variables declaration//GEN-END:variables
}
