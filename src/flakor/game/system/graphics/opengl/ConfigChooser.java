package flakor.game.system.graphics.opengl;

import android.opengl.GLSurfaceView.EGLConfigChooser;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class ConfigChooser implements EGLConfigChooser
{

	// ===========================================================
	// Constants
	// ===========================================================

		private static final int[] BUFFER = new int[1];

		private static final int RED_SIZE = 5;
		private static final int GREEN_SIZE = 6;
		private static final int BLUE_SIZE = 5;
		private static final int DEPTH_SIZE = 0;
		private static final int ALPHA_SIZE = 0;
		private static final int STENCIL_SIZE = 0;

		private static final int MULTISAMPLE_COUNT = 2; // TODO Could be made variable?

		private static final int EGL_GLES2_BIT = 4;

		private static final int[] EGLCONFIG_ATTRIBUTES_MULTISAMPLE =
        {
			EGL10.EGL_RED_SIZE, ConfigChooser.RED_SIZE,
			EGL10.EGL_GREEN_SIZE, ConfigChooser.GREEN_SIZE,
			EGL10.EGL_BLUE_SIZE, ConfigChooser.BLUE_SIZE,
			EGL10.EGL_ALPHA_SIZE, ConfigChooser.ALPHA_SIZE,
			EGL10.EGL_DEPTH_SIZE, ConfigChooser.DEPTH_SIZE,
			EGL10.EGL_STENCIL_SIZE, ConfigChooser.STENCIL_SIZE,
			EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
			EGL10.EGL_SAMPLE_BUFFERS, 1,
			EGL10.EGL_SAMPLES, ConfigChooser.MULTISAMPLE_COUNT,
			EGL10.EGL_NONE
		};

		private static final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
		private static final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;

		private static final int[] EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA = new int[]
        {
			EGL10.EGL_RED_SIZE, ConfigChooser.RED_SIZE,
			EGL10.EGL_GREEN_SIZE, ConfigChooser.GREEN_SIZE,
			EGL10.EGL_BLUE_SIZE, ConfigChooser.BLUE_SIZE,
			EGL10.EGL_ALPHA_SIZE, ConfigChooser.ALPHA_SIZE,
			EGL10.EGL_DEPTH_SIZE, ConfigChooser.DEPTH_SIZE,
			EGL10.EGL_STENCIL_SIZE, ConfigChooser.STENCIL_SIZE,
			EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
			ConfigChooser.EGL_COVERAGE_BUFFERS_NV, 1,
			ConfigChooser.EGL_COVERAGE_SAMPLES_NV, ConfigChooser.MULTISAMPLE_COUNT,  // always 5 in practice on tegra 2
			EGL10.EGL_NONE
		};

		private static final int[] EGLCONFIG_ATTRIBUTES_FALLBACK = new int[]
        {
			EGL10.EGL_RED_SIZE, ConfigChooser.RED_SIZE,
			EGL10.EGL_GREEN_SIZE, ConfigChooser.GREEN_SIZE,
			EGL10.EGL_BLUE_SIZE, ConfigChooser.BLUE_SIZE,
			EGL10.EGL_ALPHA_SIZE, ConfigChooser.ALPHA_SIZE,
			EGL10.EGL_DEPTH_SIZE, ConfigChooser.DEPTH_SIZE,
			EGL10.EGL_STENCIL_SIZE, ConfigChooser.STENCIL_SIZE,
			EGL10.EGL_RENDERABLE_TYPE, ConfigChooser.EGL_GLES2_BIT,
			EGL10.EGL_NONE
		};

		// ===========================================================
		// Fields
		// ===========================================================

		private final boolean multiSamplingRequested;

		private boolean multiSampling;
		private boolean coverageMultiSampling;

		private int redSize = -1;
		private int greenSize = -1;
		private int blueSize = -1;
		private int alphaSize = -1;
		private int depthSize = -1;
		private int stencilSize = -1;
		
		// ===========================================================
		// Constructors
		// ===========================================================

		public ConfigChooser(final boolean multiSamplingRequested)
		{
			this.multiSamplingRequested = multiSamplingRequested;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public boolean isMultiSampling() 
		{
			return this.multiSampling;
		}

		public boolean isCoverageMultiSampling()
		{
			return this.coverageMultiSampling;
		}

		public int getRedSize() {
			return this.redSize;
		}

		public int getGreenSize() {
			return this.greenSize;
		}

		public int getBlueSize() {
			return this.blueSize;
		}

		public int getAlphaSize() {
			return this.alphaSize;
		}

		public int getDepthSize() {
			return this.depthSize;
		}

		public int getStencilSize()
		{
			return this.stencilSize;
		}

	@Override
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
	{
		// TODO Auto-generated method stub
		try
        {
			return this.chooseConfig(egl, display, ConfigChooserMatcher.STRICT);
		}
        catch (final IllegalArgumentException e)
        {
            e.printStackTrace();
		}

		try
        {
			return this.chooseConfig(egl, display, ConfigChooserMatcher.LOOSE_STENCIL);
		}
        catch (final IllegalArgumentException e)
        {
            e.printStackTrace();
		}

		try
        {
			return this.chooseConfig(egl, display, ConfigChooserMatcher.LOOSE_DEPTH_AND_STENCIL);
		}
        catch (final IllegalArgumentException e)
        {
            e.printStackTrace();
		}

		return this.chooseConfig(egl, display, ConfigChooserMatcher.ANY);
	}

	private EGLConfig chooseConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final ConfigChooserMatcher pConfigChooserMatcher) throws IllegalArgumentException
	{
		ConfigChooser.BUFFER[0] = 0;

		int eglConfigCount;

		if(this.multiSamplingRequested) 
		{
			eglConfigCount = ConfigChooser.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_MULTISAMPLE);
			if(eglConfigCount > 0)
			{
				this.multiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_MULTISAMPLE, eglConfigCount, pConfigChooserMatcher);
			}

			eglConfigCount = ConfigChooser.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA);
			if(eglConfigCount > 0) 
			{
				this.coverageMultiSampling = true;
				return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_COVERAGEMULTISAMPLE_NVIDIA, eglConfigCount, pConfigChooserMatcher);
			}
		}

		eglConfigCount = ConfigChooser.getEGLConfigCount(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_FALLBACK);

		if(eglConfigCount > 0)
        {
			return this.findEGLConfig(pEGL, pEGLDisplay, ConfigChooser.EGLCONFIG_ATTRIBUTES_FALLBACK, eglConfigCount, pConfigChooserMatcher);
		}
        else
        {
			throw new IllegalArgumentException("No " + EGLConfig.class.getSimpleName() + " found!");
		}
	}


	// ===========================================================
	// Methods
	// ===========================================================

	private static int getEGLConfigCount(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes)
	{
		if(pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, null, 0, ConfigChooser.BUFFER) == false)
        {
			throw new IllegalArgumentException("EGLCONFIG_FALLBACK failed!");
	    }
		return ConfigChooser.BUFFER[0];
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final int[] pEGLConfigAttributes, final int pEGLConfigCount, final ConfigChooserMatcher pConfigChooserMatcher)
    {
		final EGLConfig[] eglConfigs = new EGLConfig[pEGLConfigCount];
		if(!pEGL.eglChooseConfig(pEGLDisplay, pEGLConfigAttributes, eglConfigs, pEGLConfigCount, ConfigChooser.BUFFER))
        {
			throw new IllegalArgumentException("findEGLConfig failed!");
		}

		return this.findEGLConfig(pEGL, pEGLDisplay, eglConfigs, pConfigChooserMatcher);
	}

	private EGLConfig findEGLConfig(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig[] pEGLConfigs, final ConfigChooserMatcher pConfigChooserMatcher)
    {
		for(int i = 0; i < pEGLConfigs.length; ++i)
        {
			final EGLConfig config = pEGLConfigs[i];
			if(config != null)
            {
				final int redSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_RED_SIZE, 0);
				final int greenSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_GREEN_SIZE, 0);
				final int blueSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_BLUE_SIZE, 0);
				final int alphaSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_ALPHA_SIZE, 0);
				final int depthSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_DEPTH_SIZE, 0);
				final int stencilSize = ConfigChooser.getConfigAttrib(pEGL, pEGLDisplay, config, EGL10.EGL_STENCIL_SIZE, 0);

				if(pConfigChooserMatcher.matches(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize))
                {
						this.redSize = redSize;
						this.greenSize = greenSize;
						this.blueSize = blueSize;
						this.alphaSize = alphaSize;
						this.depthSize = depthSize;
						this.stencilSize = stencilSize;
						return config;
				}
			}
		}
		throw new IllegalArgumentException("No EGLConfig found!");
	}

	private static int getConfigAttrib(final EGL10 pEGL, final EGLDisplay pEGLDisplay, final EGLConfig pEGLConfig, final int pAttribute, final int pDefaultValue)
    {
		if(pEGL.eglGetConfigAttrib(pEGLDisplay, pEGLConfig, pAttribute, ConfigChooser.BUFFER))
        {
			return ConfigChooser.BUFFER[0];
		}
        else
        {
			return pDefaultValue;
		}
	}

	public enum ConfigChooserMatcher
    {
		// ===========================================================
		// Elements
		// ===========================================================

		STRICT()
        {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				if(pDepthSize == ConfigChooser.DEPTH_SIZE && pStencilSize == ConfigChooser.STENCIL_SIZE) {
					if(pRedSize == ConfigChooser.RED_SIZE && pGreenSize == ConfigChooser.GREEN_SIZE && pBlueSize == ConfigChooser.BLUE_SIZE && pAlphaSize == ConfigChooser.ALPHA_SIZE) {
						return true;
					}
				}
				return false;
			}
		},
		LOOSE_STENCIL() {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				if(pDepthSize == ConfigChooser.DEPTH_SIZE && pStencilSize >= ConfigChooser.STENCIL_SIZE) {
					if(pRedSize == ConfigChooser.RED_SIZE && pGreenSize == ConfigChooser.GREEN_SIZE && pBlueSize == ConfigChooser.BLUE_SIZE && pAlphaSize == ConfigChooser.ALPHA_SIZE) {
						return true;
					}
				}
				return false;
			}
		},
		LOOSE_DEPTH_AND_STENCIL()
                {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				if(pDepthSize >= ConfigChooser.DEPTH_SIZE && pStencilSize >= ConfigChooser.STENCIL_SIZE) {
					if(pRedSize == ConfigChooser.RED_SIZE && pGreenSize == ConfigChooser.GREEN_SIZE && pBlueSize == ConfigChooser.BLUE_SIZE && pAlphaSize == ConfigChooser.ALPHA_SIZE) {
						return true;
					}
				}
				return false;
			}
		},
		ANY() {
			@Override
			public boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize) {
				return true;
			}
		};

	    public abstract boolean matches(final int pRedSize, final int pGreenSize, final int pBlueSize, final int pAlphaSize, final int pDepthSize, final int pStencilSize);

	}
}
