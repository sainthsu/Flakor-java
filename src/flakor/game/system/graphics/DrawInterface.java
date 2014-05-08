package flakor.game.system.graphics;


import flakor.game.core.camera.Camera;
import flakor.game.system.graphics.opengl.GLState;

public interface DrawInterface
{
	public void onDraw(final GLState glState,final Camera camera);
}
