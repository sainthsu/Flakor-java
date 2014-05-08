package flakor.game.core.scene;


import flakor.game.core.element.ColorfulInterface;
import flakor.game.core.modifier.ModifierInterface;
import flakor.game.system.graphics.DrawInterface;
import flakor.game.system.graphics.UpdatableInterface;

/**
 * Created by Saint Hsu on 13-7-8.
 */
public interface BackgroundInterface extends DrawInterface,UpdatableInterface,ColorfulInterface
{
    // ===========================================================
    // Methods
    // ===========================================================

    public void registerBackgroundModifier(final ModifierInterface<BackgroundInterface> backgroundModifier);
    public boolean unregisterBackgroundModifier(final ModifierInterface<BackgroundInterface> backgroundModifier);
    public void clearBackgroundModifiers();

    public boolean isColorEnabled();
    public void setColorEnabled(final boolean colorEnabled);

}
