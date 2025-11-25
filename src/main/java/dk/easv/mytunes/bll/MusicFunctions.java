package dk.easv.mytunes.bll;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;



public class MusicFunctions {
    private String bip = "music.mp3";
    private Media hit = new Media(new File(bip).toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(hit);

    public void playMusic(){

        mediaPlayer.play();

    }

    public void stopMusic(){
        mediaPlayer.stop();
    }
    public String getStatus(){
        /** StatUS can be ready, playing and stopped
         * so with this we can use the same button for
         * palying and stopping
         */
        return mediaPlayer.getStatus().toString();
    }
}
