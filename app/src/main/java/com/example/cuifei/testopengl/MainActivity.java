package com.example.cuifei.testopengl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.cuifei.testopengl.GLSurface.MyGlSurfaceView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        setContentView(R.layout.activity_main);
        //        imageView = findViewById(R.id.iv);
        //        InputStream is = null;
        //        try {
        //            is = getAssets().open("ic_launcher.png");
        //            Bitmap bitmap = BitmapFactory.decodeStream(is);
        //            imageView.setImageBitmap(bitmap);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        //
        glSurfaceView = new MyGlSurfaceView(this);
        setContentView(glSurfaceView);
    }
}
