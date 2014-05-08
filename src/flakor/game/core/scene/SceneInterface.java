package flakor.game.core.scene;

/**
 * Created by saint on 9/11/13.
 * <p>场景工作流程(Scene visit line):
 * <p>onEnter->onEnterTransitionDidFinish->onDraw->onExitTransitionDidStart->onExit
 */
public interface SceneInterface
{

    public void onEnter();

    public void onEnterTransitionDidFinish();

    public void onVisit();

    public void onExitTransitionDidStart();

    public void onExit();
}
