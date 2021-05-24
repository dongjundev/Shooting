package shootingspaceship;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class SoundManager {

    public static Clip clip2; //보스방 배경음
    public static Clip clip3;//인트로배경음

    public static void Sound(String file, String file2, boolean Loop) {                   //*음악재생  Loop가 true 이면 무한재생
        Clip clip;//보스방 들어갈때 나오는 소리
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
            AudioInputStream ais2 = AudioSystem.getAudioInputStream(new File(file2));
            clip = AudioSystem.getClip();
            clip2 = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            if (Loop) {
                clip2.loop(1);
                clip2.open(ais2);
                clip2.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void Sound(String file, boolean select) {                   //true이면 인트로배경음//false이면 총효과음
        if (select == false) {
            Clip clip; //효과음 clip
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        } else {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
                clip3 = AudioSystem.getClip();

                clip3.open(ais);
                clip3.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void SoundStop(Clip clip) {
        clip.stop();
        return;
    }

    public static void SoundStart(Clip clip) {
        clip.start();
        return;
    }

    public static void SoundClose(Clip clip) {
        clip.stop();
        clip.close();
    }

}
