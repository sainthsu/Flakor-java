package flakor.game.system.input;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Saint Hsu on 13-7-10.
 */
public interface InputStreamOpenerInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public InputStream open() throws IOException;
}
