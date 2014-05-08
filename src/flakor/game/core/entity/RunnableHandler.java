package flakor.game.core.entity;

import flakor.game.system.graphics.UpdatableInterface;

import java.util.ArrayList;

/**
 * Created by Saint Hsu on 13-7-8.
 */
public class RunnableHandler implements UpdatableInterface
{
    private final ArrayList<Runnable> runnables = new ArrayList<Runnable>();
    @Override
    public void onUpdate(final float secondsElapsed)
    {
        final ArrayList<Runnable> runnables = this.runnables;
        final int count = runnables.size();
        for(int i=0;i<count;i++)
            runnables.remove(i).run();
    }

    @Override
    public void reset()
    {
        this.runnables.clear();
    }

    public synchronized void postRunnable(final Runnable runnable)
    {
        this.runnables.add(runnable);
    }

}
