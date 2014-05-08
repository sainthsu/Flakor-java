package flakor.game.system.graphics.opengl.Shader;

import flakor.game.tool.Debug;

import java.util.ArrayList;

/**
 * Created by longjiyang on 13-7-11.
 */
public class ShaderProgramManager
{
    // ===========================================================
    // Fields
    // ===========================================================

    private final ArrayList<ShaderProgram> mShaderProgramsManaged = new ArrayList<ShaderProgram>();

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    public synchronized void onCreate() {
        this.loadShaderProgram(PositionColorTextureCoordinatesShaderProgram.getInstance());
        this.loadShaderProgram(PositionTextureCoordinatesShaderProgram.getInstance());
        this.loadShaderProgram(PositionTextureCoordinatesUniformColorShaderProgram.getInstance());
        this.loadShaderProgram(PositionColorShaderProgram.getInstance());
        this.loadShaderProgram(PositionTextureCoordinatesTextureSelectShaderProgram.getInstance());
        this.loadShaderProgram(PositionTextureCoordinatesPositionInterpolationTextureSelectShaderProgram.getInstance());
    }

    public synchronized void onDestroy() {
        final ArrayList<ShaderProgram> managedShaderPrograms = this.mShaderProgramsManaged;
        for(int i = managedShaderPrograms.size() - 1; i >= 0; i--) {
            managedShaderPrograms.get(i).setCompiled(false);
        }

        this.mShaderProgramsManaged.clear();
    }

    public synchronized void loadShaderProgram(final ShaderProgram pShaderProgram) {
        if(pShaderProgram == null) {
            throw new IllegalArgumentException("pShaderProgram must not be null!");
        }

        if(pShaderProgram.isCompiled()) {
            Debug.warn("Loading an already compiled " + ShaderProgram.class.getSimpleName() + ": '" + pShaderProgram.getClass().getSimpleName() + "'. '" + pShaderProgram.getClass().getSimpleName() + "' will be recompiled.");

            pShaderProgram.setCompiled(false);
        }

        if(this.mShaderProgramsManaged.contains(pShaderProgram)) {
            Debug.warn("Loading an already loaded " + ShaderProgram.class.getSimpleName() + ": '" + pShaderProgram.getClass().getSimpleName() + "'.");
        } else {
            this.mShaderProgramsManaged.add(pShaderProgram);
        }
    }

    public void loadShaderPrograms(final ShaderProgram ... pShaderPrograms) {
        for(int i = pShaderPrograms.length - 1; i >= 0; i--) {
            this.loadShaderProgram(pShaderPrograms[i]);
        }
    }

    public synchronized void onReload() {
        final ArrayList<ShaderProgram> managedShaderPrograms = this.mShaderProgramsManaged;
        for(int i = managedShaderPrograms.size() - 1; i >= 0; i--) {
            managedShaderPrograms.get(i).setCompiled(false);
        }
    }

}
