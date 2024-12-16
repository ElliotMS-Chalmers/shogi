package controller;

import model.settings.SoundSet;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayer {

    public static void playMoveSound(SoundSet set) {
        playSound(set.getMoveSoundPath());
    }

    public static void playCaptureSound(SoundSet set) {
        playSound(set.getCaptureSoundPath());
    }

    public static void playSound(String path) {
        Media moveSoundMedia = new Media(path);
        MediaPlayer mediaPlayer = new MediaPlayer(moveSoundMedia);
        mediaPlayer.play();
    }
}
