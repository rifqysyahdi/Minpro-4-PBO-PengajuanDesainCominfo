package DAO;

import model.Pemohon;
import model.PengajuanCetak;
import model.PengajuanDesain;
import model.PengajuanDigital;
import util.Database;

import java.sql.*;
import java.util.ArrayList;

public class PengajuanDAOImpl implements PengajuanDAO {

    @Override
    public void tambahDigital(PengajuanDigital pd) {
        String query = "INSERT INTO pengajuan (id_pengajuan, nama_pemohon, departemen, kategori, catatan, deadline, status, target_platform, format_file) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, pd.getIdPengajuan());
            stmt.setString(2, pd.getPemohon().getNamaLengkap());
            stmt.setString(3, pd.getPemohon().getDepartemenBiro());
            stmt.setString(4, "Digital");
            stmt.setString(5, pd.getCatatanRevisi());
            stmt.setString(6, pd.getDeadline());
            stmt.setString(7, pd.getStatus());
            stmt.setString(8, pd.getTargetPlatform());
            stmt.setString(9, pd.getFormatFile());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public void tambahCetak(PengajuanCetak pc) {
        String query = "INSERT INTO pengajuan (id_pengajuan, nama_pemohon, departemen, kategori, catatan, deadline, status, ukuran_media, jumlah_cetak) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, pc.getIdPengajuan());
            stmt.setString(2, pc.getPemohon().getNamaLengkap());
            stmt.setString(3, pc.getPemohon().getDepartemenBiro());
            stmt.setString(4, "Cetak");
            stmt.setString(5, pc.getCatatanRevisi());
            stmt.setString(6, pc.getDeadline());
            stmt.setString(7, pc.getStatus());
            stmt.setString(8, pc.getUkuranMedia());
            stmt.setInt(9, pc.getJumlahCetak());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<PengajuanDesain> getSemua() {
        ArrayList<PengajuanDesain> list = new ArrayList<>();
        String query = "SELECT * FROM pengajuan";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Pemohon pemohon = new Pemohon(rs.getString("nama_pemohon"), rs.getString("departemen"));
                String kategori = rs.getString("kategori");
                
                if (kategori.equals("Digital")) {
                    PengajuanDigital pd = new PengajuanDigital(
                            rs.getInt("id_pengajuan"), pemohon, rs.getString("catatan"),
                            rs.getString("deadline"), rs.getString("target_platform"), rs.getString("format_file")
                    );
                    pd.updateStatus(rs.getString("status"));
                    list.add(pd);
                } else if (kategori.equals("Cetak")) {
                    PengajuanCetak pc = new PengajuanCetak(
                            rs.getInt("id_pengajuan"), pemohon, rs.getString("catatan"),
                            rs.getString("deadline"), rs.getString("ukuran_media"), rs.getInt("jumlah_cetak")
                    );
                    pc.updateStatus(rs.getString("status"));
                    list.add(pc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public PengajuanDesain getById(int id) {
        String query = "SELECT * FROM pengajuan WHERE id_pengajuan = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Pemohon pemohon = new Pemohon(rs.getString("nama_pemohon"), rs.getString("departemen"));
                String kategori = rs.getString("kategori");
                
                if (kategori.equals("Digital")) {
                    PengajuanDigital pd = new PengajuanDigital(
                            rs.getInt("id_pengajuan"), pemohon, rs.getString("catatan"),
                            rs.getString("deadline"), rs.getString("target_platform"), rs.getString("format_file")
                    );
                    pd.updateStatus(rs.getString("status"));
                    return pd;
                } else {
                    PengajuanCetak pc = new PengajuanCetak(
                            rs.getInt("id_pengajuan"), pemohon, rs.getString("catatan"),
                            rs.getString("deadline"), rs.getString("ukuran_media"), rs.getInt("jumlah_cetak")
                    );
                    pc.updateStatus(rs.getString("status"));
                    return pc;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean updateRevisi(int id, String catatanBaru) {
        String query = "UPDATE pengajuan SET catatan = ? WHERE id_pengajuan = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, catatanBaru);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateRevisiDanStatus(int id, String catatanBaru, String statusBaru) {
        String query = "UPDATE pengajuan SET catatan = ?, status = ? WHERE id_pengajuan = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, catatanBaru);
            stmt.setString(2, statusBaru);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean hapus(int id) {
        String query = "DELETE FROM pengajuan WHERE id_pengajuan = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            return false;
        }
    }
}