//Class untuk menyimpan data dari setiap permainan yang telah selesai.

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameRecord {
    String player1Name;
    String player2Name;
    int score1;
    int score2;
    GameMode mode;
    Date timestamp;

    public GameRecord(String p1Name, String p2Name, int s1, int s2, GameMode mode, Date time) {
        this.player1Name = p1Name;
        this.player2Name = p2Name;
        this.score1 = s1;
        this.score2 = s2;
        this.mode = mode;
        this.timestamp = time;
    }

    // Untuk mengubah dta record menjadi satu baris teks untuk disimpn ke file
    public String toFileString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // Format: Tanggal,Mode,NamaP1,SkorP1,NamaP2,SkorP2
        return String.format("%s,%s,%s,%d,%s,%d",
                sdf.format(timestamp),
                mode,
                player1Name,
                score1,
                player2Name,
                score2
        );
    }
}