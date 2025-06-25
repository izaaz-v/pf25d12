//Class untuk mengelola audio dalam game

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {

    private Clip currentMusicClip;
    private List<String> musicFilePaths; //Path ke file musik
    private List<String> musicTitles;    //Daftar judul lagu untuk ditampilkan di layar
    private int currentTrackIndex = -1;

    public MusicPlayer() {
        musicFilePaths = new ArrayList<>();
        musicTitles = new ArrayList<>();

        addSong("Mondstadt", "audio/background_music.wav");
        addSong("Flickering Candlelight", "audio/song2.wav");
        addSong("the Other Side", "audio/song3.wav");
    }

    //Menambahkan lagu ke daftar putar
    public void addSong(String title, String path) {
        musicTitles.add(title);
        musicFilePaths.add(path);
    }

    //Memutar musik latar berdasarkan posisinya (index) di dalam daftar.

    public void playMusicByIndex(int index) {
        if (index < 0 || index >= musicFilePaths.size() || index == currentTrackIndex) {
            return; //Ketika indev unvalid atau lagu yang sama tetap diputar
        }

        stopMusic();

        currentTrackIndex = index;
        String path = musicFilePaths.get(currentTrackIndex);

        try {
            URL url = getClass().getClassLoader().getResource(path);
            if (url == null) {
                throw new IOException("File musik tidak ditemukan di: " + path);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            currentMusicClip = AudioSystem.getClip();
            currentMusicClip.open(audioStream);
            currentMusicClip.loop(Clip.LOOP_CONTINUOUSLY); //Musik diulang-ulang
            currentMusicClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error saat memutar musik latar: " + path);
            e.printStackTrace();
        }
    }

     //Ketika musik backsound yang diputar harus dihentikan

    public void stopMusic() {
        if (currentMusicClip != null) {
            currentMusicClip.stop();
            currentMusicClip.close();
            currentTrackIndex = -1;
        }
    }

    //Menampilkan judul lagu di UI.
    public String[] getMusicTitles() {
        return musicTitles.toArray(new String[0]);
    }
}