package com.example.cuifei.testopengl.GLSurface;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.cuifei.testopengl.Shape.Lines;
import com.example.cuifei.testopengl.Shape.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {
    private Triangle triangle;
    private final float[] mMvpMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float angle;
    private Context mContext;
    private float x, y;
    private float cx, cy;

    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
        converGL();
    }

    public MyRenderer(Context context) {
        this.mContext = context;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    Lines lines;
    int w, h;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.f, 0.f, 0.f, 0.f);
        //        triangle = new Triangle(mContext);
        lines = new Lines();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.w = width;
        this.h = height;
        //        float ratio = width / (float) height;
        //        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public void converGL() {
        cx = 2 * x / w - 1;
        cy = 1 - (2 * y / h);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        //        long time = SystemClock.uptimeMillis() % 4000l;
        //        float angle = 0.09f * ((int)time);
        //        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //        Matrix.setLookAtM(mViewMatrix, 0, 0, 0,
        //                -3, 0, 0, 0, 0, 1, 0);
        //        Matrix.multiplyMM(mMvpMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //        Matrix.multiplyMM(scratch, 0, mMvpMatrix, 0, mRotationMatrix, 0);
        //        triangle.draw(scratch);

        lines.draw(cx, cy);

    }

    public static int loadShare(int type, String sharecode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, sharecode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
