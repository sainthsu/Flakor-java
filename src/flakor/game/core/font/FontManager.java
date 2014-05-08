package flakor.game.core.font;

import flakor.game.system.graphics.opengl.GLState;

import java.util.ArrayList;

/**
 * Created by Saint Hsu on 13-7-11.
 */
public class FontManager
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final ArrayList<Font> mFontsManaged = new ArrayList<Font>();
    private static FontManager singleton = new FontManager();
    // ===========================================================
    // Constructors
    // ===========================================================
    private FontManager() {}
    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public static FontManager onCreate()
    {
        return singleton;
    }

    public synchronized void onDestroy()
    {
        final ArrayList<Font> managedFonts = this.mFontsManaged;
        for(int i = managedFonts.size() - 1; i >= 0; i--) {
            managedFonts.get(i).invalidateLetters();
        }

        this.mFontsManaged.clear();
    }

    public synchronized void loadFont(final Font pFont)
    {
        if(pFont == null)
        {
            throw new IllegalArgumentException("pFont must not be null!");
        }
        this.mFontsManaged.add(pFont);
    }

    public synchronized void loadFonts(final Font ... pFonts)
    {
        for(int i = 0; i < pFonts.length; i++)
        {
            this.loadFont(pFonts[i]);
        }
    }

    public synchronized void unloadFont(final Font pFont)
    {
        if(pFont == null) {
            throw new IllegalArgumentException("pFont must not be null!");
        }
        this.mFontsManaged.remove(pFont);
    }

    public synchronized void unloadFonts(final Font ...pFonts)
    {
        for(int i = 0; i < pFonts.length; i++)
        {
            this.unloadFont(pFonts[i]);
        }
    }

    public synchronized void updateFonts(final GLState pGLState)
    {
        final ArrayList<Font> fonts = this.mFontsManaged;
        final int fontCount = fonts.size();
        if(fontCount > 0){
            for(int i = fontCount - 1; i >= 0; i--)
            {
                fonts.get(i).update(pGLState);
            }
        }
    }

    public synchronized void onReload()
    {
        final ArrayList<Font> managedFonts = this.mFontsManaged;
        for(int i = managedFonts.size() - 1; i >= 0; i--)
        {
            managedFonts.get(i).invalidateLetters();
        }
    }

}
