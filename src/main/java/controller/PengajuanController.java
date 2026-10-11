package controller;

import DAO.PengajuanDAO;
import DAO.PengajuanDAOImpl;
import model.PengajuanDesain;
import model.PengajuanDigital;
import model.PengajuanCetak;
import model.Pemohon;

import java.util.ArrayList;

public class PengajuanController {
    private final PengajuanDAO dao;

    public PengajuanController() {
        this.dao = new PengajuanDAOImpl();
    }

    public boolean idSudahAda(int id) {
        return dao.getById(id) != null;
    }

    public boolean tambahPengajuanDigital(int id, String nama, String dept, String info, String deadline, String platform, String format) {
        if (idSudahAda(id)) {
            return false;
        }
        Pemohon pemohon = new Pemohon(nama, dept);
        PengajuanDigital pd = new PengajuanDigital(id, pemohon, info, deadline, platform, format);
        dao.tambahDigital(pd);
        return true;
    }

    public boolean tambahPengajuanCetak(int id, String nama, String dept, String info, String deadline, String ukuran, int jumlah) {
        if (idSudahAda(id)) {
            return false;
        }
        Pemohon pemohon = new Pemohon(nama, dept);
        PengajuanCetak pc = new PengajuanCetak(id, pemohon, info, deadline, ukuran, jumlah);
        dao.tambahCetak(pc);
        return true;
    }

    public ArrayList<PengajuanDesain> getSemuaPengajuan() {
        return dao.getSemua();
    }

    public PengajuanDesain cariBerdasarkanId(int id) {
        return dao.getById(id);
    }

    public boolean updateRevisi(int id, String catatanBaru) {
        if (idSudahAda(id)) {
            return dao.updateRevisi(id, catatanBaru);
        }
        return false;
    }

    public boolean updateRevisiDanStatus(int id, String catatanBaru, String statusBaru) {
        if (idSudahAda(id)) {
            return dao.updateRevisiDanStatus(id, catatanBaru, statusBaru);
        }
        return false;
    }

    public boolean hapusPengajuan(int id) {
        if (idSudahAda(id)) {
            return dao.hapus(id);
        }
        return false;
    }
}