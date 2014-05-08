package flakor.game.console.config;

/**
 * Created by Saint Hsu on 13-7-23.
 */
public class RenderConfig
{
    // ===========================================================
    // Fields
    // ===========================================================

    private boolean multiSampling = false;
    private boolean dithering = false;

    public boolean isDithering()
    {
        return dithering;
    }

    public void setDithering(boolean dithering)
    {
        this.dithering = dithering;
    }

    public boolean isMultiSampling()
    {
        return multiSampling;
    }

    public void setMultiSampling(boolean multiSampling)
    {
        this.multiSampling = multiSampling;
    }
}
