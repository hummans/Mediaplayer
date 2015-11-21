package com.mediaplayer.com;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mediaplayer.adapter.PlaylistCreationAdapter;
import com.mediaplayer.db.SongInfoDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SongsShowActivity extends Activity {

    public static final String MODE_KEY = "mode_key";
    public static final String ID_KEY = "id";

    public enum SHOW_MODE{
        PLAY_LIST,
        PLAYLIST_CREATION,
        ALBUMS,
        ARTISTS,
        NOW_PLAYING
    }
    SHOW_MODE CURRENT_MODE = SHOW_MODE.PLAYLIST_CREATION;
    ListView lv;
    PlaylistCreationAdapter adapter;
    ArrayList<SongInfo> song_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_creation);
        this.getActionBar().setTitle("Create Playlist");
        lv = (ListView)findViewById(R.id.listview);
        Bundle b = getIntent().getExtras();
        CURRENT_MODE = (SHOW_MODE)b.getSerializable(MODE_KEY);
        setupAdapter();
    }

    private void setupAdapter() {
        Map<String, SongInfo> selected_song_array = new HashMap<>();
        song_array = new ArrayList<>();
        adapter = new PlaylistCreationAdapter(this,song_array,lv);
        lv.setFastScrollEnabled(true);
        lv.setAdapter(adapter);
        song_array.clear();
        switch (CURRENT_MODE){
            case PLAYLIST_CREATION:
                song_array.addAll(SongInfoDatabase.getInstance().getSongs(null) );
                break;
            case ALBUMS:
                break;
            case ARTISTS:
                break;
            case NOW_PLAYING:
                song_array.addAll(SongsManager.getInstance().getSongsList());
                for (int i=0;i<song_array.size();i++){
                    selected_song_array.put(song_array.get(i).getId(),song_array.get(i));
                }
                break;
            case PLAY_LIST:
                break;
        }
        if(selected_song_array.size() > 0){
            adapter.setSelected_song_array(selected_song_array);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.playlist_creation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.create_playlist:
                showDialog();
                break;
        }
        return true;
    }

    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View v = this.getLayoutInflater().inflate(R.layout.playlist_save_dialog,null);
        final EditText editText = (EditText)v.findViewById(R.id.playlist_name);
        dialogBuilder.setView(v);
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                long[] ids = getLongArray();
                if (name != null && name.length() > 0) {
                    SongInfoDatabase.getInstance().createNewPLaylist(ids,name);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a valid name", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog  = dialogBuilder.create();
        alertDialog.show();
    }

    private long[] getLongArray(){
        Map<String,SongInfo> selected_song_array = adapter.getSelected_song_array();
        Iterator it = selected_song_array.entrySet().iterator();
        long[] ids  = new long[selected_song_array.entrySet().size()];
        int counter = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ids[counter++] = Long.parseLong((String)pair.getKey());
            it.remove();
        }
        return  ids;
    }
}
