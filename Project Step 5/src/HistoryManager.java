//Class untuk menyimpan dan memuat riwayat permainan dari/ke file teks.

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    private static final String HISTORY_FILE = "game_history.txt";

    //Method untuk menyimpan satu record ke akhir file
    public static void saveRecord(GameRecord record) {
        try (PrintWriter out = new PrintWriter(new FileWriter(HISTORY_FILE, true))) {
            out.println(record.toFileString());
        } catch (IOException e) {
            System.err.println("Gagal menyimpan riwayat: " + e.getMessage());
        }
    }

    //Method untuk membaca semua baris riwayat dari file
    public static List<String> loadHistory() {
        List<String> historyLines = new ArrayList<>();
        File file = new File(HISTORY_FILE);
        // Jika file belum ada, kembalikan list kosong
        if (!file.exists()) {
            return historyLines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                historyLines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat riwayat: " + e.getMessage());
        }
        return historyLines;
    }
}