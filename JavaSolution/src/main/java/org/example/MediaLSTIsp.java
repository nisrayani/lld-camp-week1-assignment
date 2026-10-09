package org.example;// MediaLSPIsp.java
// Messy starter: Fat interface + LSP surprises (violates ISP + LSP)

interface Player {
    void play(String source);

    void pause();
}

interface Recorder {
    void record(String destination);   // many can't
}

interface LiveStreamer {
    void streamLive(String url);       // many can't
}

interface Downloader {
    void download(String sourceUrl);   // many can't
}


class AudioPlayer implements Player, Downloader {
    private boolean playing = false;

    @Override
    public void play(String source) {
        playing = true;
    }

    @Override
    public void pause() {
        playing = false;
    }

    @Override
    public void download(String sourceUrl) {
        // pretend
    }

    public boolean isPlaying() {
        return playing;
    }
}

class CameraStreamPlayer implements Player, LiveStreamer {
    private boolean liveStarted = false;
    private boolean playing = false;

    @Override
    public void play(String source) {
        // Surprise: needs streamLive first for “real” play
        if (!liveStarted) {
            System.out.println("[WARN] playing without live stream started.");
        }
        playing = true;
    }

    @Override
    public void pause() {
        playing = false;
    }

    @Override
    public void streamLive(String url) {
        liveStarted = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isLive() {
        return liveStarted;
    }
}

public class MediaLSTIsp {
    public static void main(String[] args) {
        AudioPlayer ap = new AudioPlayer();
        ap.play("song.mp3");
        System.out.println("Audio playing: " + ap.isPlaying());
        ap.pause();

        CameraStreamPlayer cam = new CameraStreamPlayer();
        cam.play("rtsp://camera");       // warning surprise
        cam.streamLive("rtsp://camera"); // required order
        cam.play("rtsp://camera");
    }
}
