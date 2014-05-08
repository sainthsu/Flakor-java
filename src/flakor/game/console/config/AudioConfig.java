package flakor.game.console.config;

import flakor.game.system.audio.SoundManager;

/**
 * Created by Saint Hsu on 13-7-22.
 * 音频配置
 * musicOn true if music is on
 * soundOn true if sound is on
 */

public class AudioConfig
{
    private boolean musicOn = true;
    private boolean soundOn = true;

    /**
     * 最大同时播放的声效流
     */
    private int maxSimultaneousStreams = SoundManager.MAX_SIMULTANEOUS_STREAMS_DEFAULT;

    public boolean isMusicOn()
    {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn)
    {
        this.musicOn = musicOn;
    }

    public boolean isSoundOn()
    {
        return soundOn;
    }

    public void setSoundOn(boolean soundOn)
    {
        this.soundOn = soundOn;
    }

    public int getMaxSimultaneousStreams()
    {
        return maxSimultaneousStreams;
    }

    public void setMaxSimultaneousStreams(int maxSimultaneousStreams)
    {
        this.maxSimultaneousStreams = maxSimultaneousStreams;
    }
}
