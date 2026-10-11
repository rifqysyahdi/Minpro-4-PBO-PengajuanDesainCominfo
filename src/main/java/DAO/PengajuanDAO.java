package DAO;

import model.PengajuanDesain;
import model.PengajuanDigital;
import model.PengajuanCetak;
import java.util.ArrayList;

public interface PengajuanDAO {
    void tambahDigital(PengajuanDigital pd);
    void tambahCetak(PengajuanCetak pc);
    ArrayList<PengajuanDesain> getSemua();
    PengajuanDesain getById(int id);
    boolean updateRevisi(int id, String catatanBaru);
    boolean updateRevisiDanStatus(int id, String catatanBaru, String statusBaru);
    boolean hapus(int id);
}