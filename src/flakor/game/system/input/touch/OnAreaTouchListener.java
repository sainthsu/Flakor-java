package flakor.game.system.input.touch;

/**
 * Created by Saint Hsu on 13-7-8.
 */
public interface OnAreaTouchListener
{
    public boolean onAreaTouched(final TouchEvent event,final TouchAreaInterface area,final float touchAreaLocalX, final float touchAreaLocalY);
}
