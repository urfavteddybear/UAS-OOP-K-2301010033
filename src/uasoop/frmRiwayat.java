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
    }    void loadData() {
        model.setRowCount(0);
        try {            String sql = "SELECT t.id_transaksi, p.nama_pelanggan, t.tanggal_sewa, t.tanggal_kembali, " +
                        "t.durasi, t.total_harga, u.nama " +
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
    }    void cariData() {
        String tanggalDari = txTanggalDari.getText();
        String tanggalSampai = txTanggalSampai.getText();
        
        if (tanggalDari.isEmpty() || tanggalSampai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal pencarian!");
            return;
        }
        
        model.setRowCount(0);
        try {
            String sql = "SELECT t.id_transaksi, p.nama_pelanggan, t.tanggal_sewa, t.tanggal_kembali, " +
                        "t.durasi, t.total_harga, u.nama " +
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
                    rs.getString("nama")
                });
                
                totalTransaksi++;
                totalPendapatan += rs.getDouble("total_harga");
            }
            
            lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
            lblTotalPendapatan.setText(currencyFormat.format(totalPendapatan));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching data: " + e.getMessage());
        }
    }

    void lihatDetail() {
        int row = tblRiwayat.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu!");
            return;
        }
        
        int idTransaksi = (Integer) model.getValueAt(row, 0);        try {
            String sql = "SELECT b.nama_barang, dt.jumlah, dt.harga_satuan, dt.subtotal " +
                        "FROM detail_transaksi dt " +
                        "JOIN barang b ON dt.id_barang = b.id_barang " +
                        "WHERE dt.id_transaksi = ?";
            
            PreparedStatement ps = koneksi.prepareStatement(sql);
            ps.setInt(1, idTransaksi);
            ResultSet rs = ps.executeQuery();
            
            StringBuilder detail = new StringBuilder();
            detail.append("Detail Transaksi #").append(idTransaksi).append("\n");
            detail.append("Pelanggan: ").append(model.getValueAt(row, 1)).append("\n");
            detail.append("Tanggal Sewa: ").append(model.getValueAt(row, 2)).append("\n");
            detail.append("Tanggal Kembali: ").append(model.getValueAt(row, 3)).append("\n");
            detail.append("Durasi: ").append(model.getValueAt(row, 4)).append("\n\n");
            detail.append("Detail Barang:\n");
            detail.append("=====================================\n");
            
            while (rs.next()) {
                detail.append("• ").append(rs.getString("nama_barang")).append("\n");
                detail.append("  Jumlah: ").append(rs.getInt("jumlah")).append("\n");
                detail.append("  Harga: ").append(currencyFormat.format(rs.getDouble("harga_satuan"))).append("/hari\n");
                detail.append("  Subtotal: ").append(currencyFormat.format(rs.getDouble("subtotal"))).append("\n\n");
            }
            
            detail.append("=====================================\n");
            detail.append("Total: ").append(model.getValueAt(row, 5));
            
            JOptionPane.showMessageDialog(this, detail.toString(), "Detail Transaksi", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
                "ID", "Pelanggan", "Tanggal Sewa", "Tanggal Kembali", "Durasi", "Total", "User"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        jLabel5.setText("Riwayat Transaksi");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txTanggalDari, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txTanggalSampai, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCari)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefresh))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(30, 30, 30)
                        .addComponent(lblTotalTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel4)
                        .addGap(30, 30, 30)
                        .addComponent(lblTotalPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDetail)
                        .addGap(18, 18, 18)
                        .addComponent(btnTutup)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel5)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txTanggalDari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txTanggalSampai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCari)
                    .addComponent(btnRefresh))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblTotalTransaksi)
                    .addComponent(jLabel4)
                    .addComponent(lblTotalPendapatan))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDetail)
                    .addComponent(btnTutup))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
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
