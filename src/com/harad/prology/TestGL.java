package com.harad.prology;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class TestGL extends Activity
{
	GLSurfaceView mGLSurfaceView;

   	final Handler h = new Handler()
   	{
   		@Override
   		public void handleMessage(Message msg)
   		{
   			if (msg.what == 0) { close( (String)msg.obj ); }
   		}
   	};

	GLSurfaceView.Renderer renderer = new GLSurfaceView.Renderer()
	{
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glClearColor(8, 8, 8, 0);
			String   glInfo = "GPU Model: " + gl.glGetString(GL10.GL_RENDERER);
			glInfo = glInfo + "\n" + "GPU Vendor: " + gl.glGetString(GL10.GL_VENDOR);
			glInfo = glInfo + "\n" + "GPU Version: " + gl.glGetString(GL10.GL_VERSION);
			Message.obtain(h, 0, glInfo).sendToTarget();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) { }

		@Override
		public void onDrawFrame(GL10 gl) { }
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new GLSurfaceView(this);
		mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		mGLSurfaceView.setRenderer(renderer);
		setContentView(mGLSurfaceView);
	}

	public void close(String glInfo)
	{
		Intent intent= new Intent();
		intent.putExtra("GLInfo", glInfo);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
