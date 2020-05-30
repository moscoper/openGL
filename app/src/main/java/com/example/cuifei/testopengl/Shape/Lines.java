package com.example.cuifei.testopengl.Shape;

import android.opengl.GLES20;

import com.example.cuifei.testopengl.GLSurface.MyRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Lines {
    private final String vertexShaderCode =
            "layout (location = 0) attribute vec4 vPosition;" +
                    "void main() {" +
                    " gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
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
    IntBuffer VBO;
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
    }


    public void converFloat() {
        linePoints = new float[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            linePoints[i] = arrayList.get(i);
        }
    }

    public void freshData(float x, float y) {
        arrayList.add(x);
        arrayList.add(y);
        arrayList.add(0.0f);
        converFloat();
        VBO = IntBuffer.allocate(linePoints.length * 4);
        VBO.clear();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(linePoints.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.clear();
        vertexBuffer.put(linePoints);
        vertexBuffer.position(0);

        GLES20.glGenBuffers(1, VBO);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO.get(0));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, linePoints.length * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);

    }


    public void draw(float x, float y) {
        freshData(x, y);

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 3 * 4, 0);
        colorHandler = GLES20.glGetUniformLocation(program, "vColor");
        GLES20.glUniform4fv(colorHandler, 1, color, 0);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, linePoints.length / 3);
        GLES20.glDeleteBuffers(1,VBO);
        GLES20.glDisableVertexAttribArray(0);
    }
}




























