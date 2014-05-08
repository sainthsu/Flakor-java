package flakor.game.core;

import flakor.game.console.config.EngineConfig;
import flakor.game.core.scene.Scene;

/**
 * Created by Saint Hsu on 13-7-14.
 */
public interface GameInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public EngineConfig onCreateEngineOptions();
    public Engine onCreateEngine(final EngineConfig pEngineOptions);

    public void onCreateResources(final OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception;
    public void onCreateScene(final OnCreateSceneCallback pOnCreateSceneCallback) throws Exception;
    public void onPopulateScene(final Scene pScene, final OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception;

    public void onReloadResources() throws Exception;
    public void onDestroyResources() throws Exception;

    /*Game procedure
    /*游戏流程
     */

    public void onGameCreated();
    public void onGameResume();
    public void onGamePause();
    //public void onGameRender();
    public void onGameResize(int width,int height);
    public void onGameDestroyed();

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static interface OnCreateResourcesCallback
    {

        // ===========================================================
        // Methods
        // ===========================================================

        public void onCreateResourcesFinished();
    }

    public static interface OnCreateSceneCallback
    {

        // ===========================================================
        // Methods
        // ===========================================================

        public void onCreateSceneFinished(final Scene pScene);
    }

    public static interface OnPopulateSceneCallback
    {

        // ===========================================================
        // Methods
        // ===========================================================

        public void onPopulateSceneFinished();
    }
}
