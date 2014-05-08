/*
 * RenderTextureInitializationException.java
 * Created on 8/27/13 7:04 PM
 *
 * ver0.0.1beta 8/27/13 saint
 * Copyright (c) 2013 Saint Hsu All Rights Reserved.
 */

package flakor.game.system.graphics.opengl.Texture;

import flakor.game.tool.FlakorRuntimeException;

/**
 * Created by saint on 8/27/13.
 */
public class RenderTextureInitializationException extends FlakorRuntimeException
{
    // Constants
    // ===========================================================

    private static final long serialVersionUID = -7219303294648252076L;

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
    // ===========================================================

    public RenderTextureInitializationException() {
        super();
    }

    public RenderTextureInitializationException(final String pMessage) {
        super(pMessage);
    }

    public RenderTextureInitializationException(final Throwable pThrowable) {
        super(pThrowable);
    }

    public RenderTextureInitializationException(final String pMessage, final Throwable pThrowable) {
        super(pMessage, pThrowable);
    }
}
