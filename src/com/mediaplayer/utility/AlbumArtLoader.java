package com.mediaplayer.utility;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.LruCache;
import android.widget.ImageView;

import com.mediaplayer.com.R;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by shrikanth on 10/2/15.
 */
public class AlbumArtLoader extends AsyncTask<String, Void, Bitmap> {

    public enum Mode{
        ALBUM, ARTIST, PLAYLIST
    }
    Mode current;
    private ImageView imageView;
    private String albumId;
    Activity activity;
    static LruCache<String, Bitmap> cache = new LruCache<>(4  *1024);
    public AlbumArtLoader(Activity activity, String albumId, ImageView imageView, Mode mode) {
        this.albumId = albumId;
        this.imageView = imageView;
        this.activity = activity;
        this.current = mode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        switch (current){
            case ALBUM:
                albumId =  albumId;
                break;
            case ARTIST:
                break;
            case PLAYLIST:
                break;
        }
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, Long.parseLong(albumId) );
        Bitmap bitmap;
        try {
            bitmap = cache.get(albumId);
            if(cache.get(albumId)==null){
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                cache.put(albumId,bitmap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.albums);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }


}