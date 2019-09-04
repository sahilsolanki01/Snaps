package com.solanki.sahil.snaps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ViewSnapActivity extends AppCompatActivity {

    Intent intent;


    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public class ImageDownloader extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snap);

        intent = getIntent();

        TextView textView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.imageView);

        textView.setText(intent.getStringExtra("message"));

        ImageDownloader imageDownloader = new ImageDownloader();
        try {
            Bitmap celebImage = imageDownloader.execute(intent.getStringExtra("imageUrl")).get();
            imageView.setImageBitmap(celebImage);
        }catch (Exception e){
            e.getMessage();
        }






    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.getDatabase().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").child(intent.getStringExtra("snapKey")).removeValue();
        FirebaseStorage.getInstance().getReference().child("images").child(intent.getStringExtra("imageName")).delete();

    }



}
