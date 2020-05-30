package com.example.cuifei.testopengl.Shape;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.cuifei.testopengl.GLSurface.MyRenderer;
import com.example.cuifei.testopengl.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class Triangle {
    //    private final String vertexShaderCode =
    //            "uniform mat4 uMVPMatrix;" +
    //            "attribute vec4 vPosition;" +
    //                    "void main() {" +
    //                    "  gl_Position = uMVPMatrix * vPosition;" +
    //                    "}";
    private final String vertexShaderCode =
            "layout (location = 0) attribute vec4 vPosition;" +
                    "layout (location = 1) attribute vec2 aTexCoord;" +
                    "varying vec2 TexCoord;" +
                    "void main() {" +
                    " gl_Position = vPosition;" +
                    " TexCoord = aTexCoord;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "varying vec2 TexCoord;" +
                    "uniform sampler2D ourTexture;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(ourTexture,TexCoord);" +
                    "}";


    final static int COORDS_PER_VERTEX = 4;
    //    static float[] triangleCoords = {0.0f, 0.622008459f, 0.0f, // top
    //            -0.5f, -0.311004243f, 0.0f, // bottom left
    //            0.5f, -0.311004243f, 0.0f};
    static float[] triangleCoords = {1.0f, 1.0f, 3.0f, 3.0f,  // 右上角
            1.0f, -1.0f, 3.0f, 0.0f, // 右下角
            -1.0f, -1.0f, 0.0f, 0.0f, // 左下角
            -1.0f, 1.0f, 0.0f, 3.0f  // 左上角
    };

    private short drawOrder[] = {0, 1, 2, 0, 2, 3};
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private FloatBuffer vertexBuffer;

    private int program;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    private int positionHandler;
    private int colorHandler;
    private int mMVPMatrixHandle;
    IntBuffer VBO = IntBuffer.allocate(triangleCoords.length * 4);
    //    IntBuffer textue = IntBuffer.allocate(4 * 4);
    int[] textue = new int[1];
    private ShortBuffer drawListBuffer;
    private int glHTexture;

    public Triangle(Context context) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        GLES20.glGenBuffers(1, VBO);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO.get(0));
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, 4 * triangleCoords.length, vertexBuffer, GLES20.GL_STATIC_DRAW);

        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
        try {
            InputStream is = context.getAssets().open("ic_launcher.png");

            Bitmap bitmap = BitmapFactory.decodeStream(is);

            GLES20.glGenTextures(1, textue, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textue[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
            //            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            //            byte[] data = stream.toByteArray();
            //            ByteBuffer byteBufferData = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight() * 4);
            //            GLES20.glReadPixels(0, 0, bitmap.getWidth(), bitmap.getHeight(), GLES20.GL_RGBA,
            //                    GLES20.GL_UNSIGNED_BYTE, byteBuffer);
            //            byteBufferData.order(ByteOrder.nativeOrder());
            //            byteBufferData.put(data);
            //            byteBufferData.position(0);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            //            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBufferData);
            GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int vertexShader = MyRenderer.loadShare(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyRenderer.loadShare(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(program);
        //        positionHandler = GLES20.glGetAttribLocation(program,"vPosition");
        //        GLES20.glEnableVertexAttribArray(positionHandler);
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glEnableVertexAttribArray(1);
        //        GLES20.glVertexAttribPointer(positionHandler, COORDS_PER_VERTEX,
        //                GLES20.GL_FLOAT, false,
        //                vertexStride, vertexBuffer);
        GLES20.glVertexAttribPointer(0, 2,
                GLES20.GL_FLOAT, false,
                vertexStride, 0);
        GLES20.glVertexAttribPointer(1, 2,
                GLES20.GL_FLOAT, false,
                vertexStride, 2 * 4);
        glHTexture = GLES20.glGetUniformLocation(program, "vTexture");
        GLES20.glUniform1i(glHTexture, textue[0]);
        colorHandler = GLES20.glGetUniformLocation(program, "vColor");
        //        mMVPMatrixHandle = GLES20.glGetUniformLocation(program,"uMVPMatrix");
        GLES20.glUniform4fv(colorHandler, 1, color, 0);
        //        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mvpMatrix,0);
        //        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textue.get(0));
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        //        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        GLES20.glDisableVertexAttribArray(0);
    }

}




















