package com.example.cuifei.testopengl.GLSurface;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGlSurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    MyRenderer renderer;

    float perX, perY;

    public MyGlSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new MyRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);


    }

    public MyGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                renderer.setXY(event);
                requestRender();
                break;
            case MotionEvent.ACTION_MOVE:
                //                float x = event.getX();
                //                float y = event.getY();
                //                renderer.setXY(x, y);
                renderer.setXY(event);
                requestRender();
                //                float dx = x - perX;
                //                float dy = y - perY;
                //                if (y > getHeight() / 2) {
                //                    dx = dx * -1;
                //                }
                //
                //                // reverse direction of rotation to left of the mid-line
                //                if (x < getWidth() / 2) {
                //                    dy = dy * -1;
                //                }
                //                renderer.setAngle(
                //                        renderer.getAngle() -
                //                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                //
                //
                //                perX = x;
                //                perY = y;

                break;
            case MotionEvent.ACTION_UP:
                renderer.setXY(event);
                requestRender();
                break;
            default:
        }
        return true;
    }
}
