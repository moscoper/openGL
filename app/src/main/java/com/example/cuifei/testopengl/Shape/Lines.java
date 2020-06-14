package com.example.cuifei.testopengl.Shape;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.example.cuifei.testopengl.GLSurface.MyRenderer;
import com.example.cuifei.testopengl.bean.LinePath;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Lines {
    private final String vertexShaderCode =
            "layout (location = 0) attribute vec4 vPosition;" +
                    "layout (location = 1) attribute vec4 color;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    " gl_Position = vPosition;" +
                    " vColor = color;" +
                    "}";

    //                    "uniform vec4 vColor;" +
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    //    static float[] linePoints = {0.5f, 0.5f, 0.0f, 1.0f, // top
    //            0.5f, -0.5f, 0.0f, 1.0f,// bottom left
    //            -0.5f, -0.5f, 0.0f, 1.0f};
    static float[] linePoints;

    private FloatBuffer vertexBuffer;
    ArrayList<Float> arrayList = new ArrayList<>();
    public ArrayList<LinePath> pathArrayList = new ArrayList<>();
    IntBuffer VBO;
    IntBuffer VAO;
    private int program;
    private int colorHandler;


    public Lines() {

        int vertexShader = MyRenderer.loadShare(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyRenderer.loadShare(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        VAO = IntBuffer.allocate(200);
        GLES30.glGenVertexArrays(200, VAO);
        VBO = IntBuffer.allocate(200);
        GLES20.glGenBuffers(200, VBO);
    }


    public void converFloat(LinePath linePath) {
        linePoints = new float[linePath.points.size()];
        for (int i = 0; i < linePath.points.size(); i++) {
            if (i < linePath.points.size() && i < linePoints.length) {
                linePoints[i] = linePath.points.get(i);
            }

        }
    }

    public void freshData(LinePath linePath, int i) {
        //        arrayList.add(x);
        //        arrayList.add(y);
        //        arrayList.add(0.0f);
        converFloat(linePath);


        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(linePoints.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(linePoints);
        vertexBuffer.position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO.get(i));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, linePoints.length * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glUseProgram(program);
        GLES20.glLineWidth(20.f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glEnableVertexAttribArray(1);
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 7 * 4, 0);
        GLES20.glVertexAttribPointer(1, 4, GLES20.GL_FLOAT, false, 7 * 4, 3 * 4);
    }


    public void draw(float x, float y) {

        Log.d("draw", "==" + pathArrayList.size());

        if (pathArrayList.size() == 1) {
            GLES30.glBindVertexArray(VAO.get(0));
            freshData(pathArrayList.get(pathArrayList.size() - 1), 0);
//            colorHandler = GLES20.glGetUniformLocation(program, "vColor");
//            GLES20.glUniform4fv(colorHandler, 1, color, 0);
            GLES30.glBindVertexArray(VAO.get(0));
            GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, pathArrayList.get(0).points.size() / 7);
//            GLES20.glDisableVertexAttribArray(0);
//            GLES20.glDisableVertexAttribArray(1);
        } else if (pathArrayList.size() > 1) {
            for (int i = 0; i < pathArrayList.size(); i++) {
                if (i == pathArrayList.size() - 1) {
//                    GLES30.glBindVertexArray(VAO.get(i));
                    GLES30.glBindVertexArray(VAO.get(i));
                    freshData(pathArrayList.get(pathArrayList.size() - 1), i);
                    GLES30.glBindVertexArray(VAO.get(i));
                    GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, pathArrayList.get(i).points.size() / 7);
//                    GLES20.glDisableVertexAttribArray(i);
//                    GLES20.glDisableVertexAttribArray(1);
                } else {
                    Log.d("draw", "==VAO");
                    GLES30.glBindVertexArray(VAO.get(i));
                    GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, pathArrayList.get(i).points.size() / 7);
//                    GLES20.glDisableVertexAttribArray(i);
//                    GLES20.glDisableVertexAttribArray(1);
                }
            }
        }

//        for (int i = 0; i < pathArrayList.size(); i++) {
//            if (i > 1) {
//                GLES30.glBindVertexArray(VAO.get(0));
//                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, linePoints.length / 7);
//                freshData(pathArrayList.get(pathArrayList.size() - 1));
//
//
////            colorHandler = GLES20.glGetUniformLocation(program, "vColor");
////            GLES20.glUniform4fv(colorHandler, 1, color, 0);
//                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, linePoints.length / 7);
//                GLES20.glDisableVertexAttribArray(0);
//            } else if (i == 0) {
//
//
////            colorHandler = GLES20.glGetUniformLocation(program, "vColor");
////            GLES20.glUniform4fv(colorHandler, 1, color, 0);
//                GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, linePoints.length / 7);
//                GLES20.glDisableVertexAttribArray(0);
//            }
//
//        }

    }
}




























