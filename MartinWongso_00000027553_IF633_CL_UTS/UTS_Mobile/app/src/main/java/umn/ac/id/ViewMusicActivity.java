package umn.ac.id;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewMusicActivity extends AppCompatActivity {
    ListView listView;
    String[] items;
    private int count;

    RecyclerView audioListView;
    DaftarLaguAdapter audioListAdapter;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTitle("List Lagu");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        listView = (ListView) findViewById(R.id.listView);

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewMusicActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Selamat Datang");
        builder.setMessage("Martin Wongso - 00000027553");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //display();
                        getSongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                startProfile();
                return true;
            case R.id.logout:
                if(MusicDetailActivity.mp != null){
                    if (MusicDetailActivity.mp.isPlaying())
                        MusicDetailActivity.mp.pause();
                }
                startLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startProfile(){
        Intent profileIntent = new Intent(ViewMusicActivity.this, ProfileAct2.class);
        startActivity(profileIntent);
    }

    private void startLogin(){
        Intent profileIntent = new Intent(ViewMusicActivity.this, LoginActivity.class);
        startActivity(profileIntent);
    }

    public void getSongs(){
        context = ViewMusicActivity.this;
        audioListView = findViewById(R.id.recyclerView);

        ArrayList<File> allAudioFiles = getAllAudioFromDevice(context);

        audioListAdapter = new DaftarLaguAdapter(context,allAudioFiles);
        audioListView.setLayoutManager(new LinearLayoutManager(context));
        audioListView.setAdapter(audioListAdapter);
    }

    public ArrayList<File> getAllAudioFromDevice(final Context context) {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        return mySongs;
    }

    public ArrayList<File> findSong(File root){
        ArrayList<File> at = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                at.addAll(findSong(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")){
                    at.add(singleFile);
                }
            }
        }
        return at;
    }

    void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[ mySongs.size() ];
        for(int i=0;i<mySongs.size();i++){
            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter<String> adp = new
                ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adp);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String songName = listView.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(),MusicDetailActivity.class)
                        .putExtra("pos",position).putExtra("songs",mySongs).putExtra("songname",songName));
            }
        });
    }
}
