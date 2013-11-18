package edu.mbl.jif.utils;

import java.io.*;
import javax.sound.sampled.*;

public class SoundClip {
    
    private boolean error = false; //ERROR get set true if there is an error.
    private AudioInputStream audioSource;
    private DataLine.Info info;
    private Clip clip;
    private File file;
    
//  public SoundClip(String dir) {
//    try {
//      file = new File(dir);
//      audioSource = AudioSystem.getAudioInputStream(file);
//      info = new DataLine.Info(Clip.class, audioSource.getFormat());
//      clip = (Clip) AudioSystem.getLine(info);
//      clip.open(audioSource);
//    } catch (Exception e) {
//      e.printStackTrace();
//      error = true;
//    }
//  }
    
    
    public SoundClip(String soundFile) {
        
        try {
            AudioInputStream stream =
                    AudioSystem.getAudioInputStream(
                    this.getClass().getResource("./sounds/" +  soundFile));
            // ALAW & ULAW encodings must be converted to PCM_SIGNED before played
            AudioFormat format = stream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        format.getSampleSizeInBits() * 2,
                        format.getChannels(),
                        format.getFrameSize() * 2,
                        format.getFrameRate(),
                        true); // big endian
                stream = AudioSystem.getAudioInputStream(format, stream);
            }
            /** @todo Does this deal with .wav files?? */
            // Create the clip
            info = new DataLine.Info(
                    Clip.class, stream.getFormat(),
                    ( (int) stream.getFrameLength() * format.getFrameSize()));
            clip = (Clip) AudioSystem.getLine(info);
            // This method does not return until the audio file is completely loaded
            clip.open(stream);
            // Start playing
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        
    }
    
    
    public SoundClip(File dir) {
        try {
            file = dir;
            audioSource = AudioSystem.getAudioInputStream(file);
            info = new DataLine.Info(Clip.class, audioSource.getFormat());
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioSource);
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }
    }
    
    public void play() {
        clip.start();
    }
    
//  public void play() {
//    if (!error) {
//      (new SwingWorker() {
//        public Object construct() {
//          clip.start();
//          return null;
//        }
//        public void finished() {
//        }
//      }).start();
//    }
//  }
    
    public void stop() {
        if (!error) {
            clip.stop();
        }
    }
    
    public void setMicrosecondPosition(int pos) {
        if (!error) {
            clip.setMicrosecondPosition(pos);
        }
    }
    
}
