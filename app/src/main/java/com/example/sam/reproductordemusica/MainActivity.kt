package com.example.sam.reproductordemusica

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.sam.reproductordemusica.adapters.SongListAdapter
import com.example.sam.reproductordemusica.model.SongModel
import kotlinx.android.synthetic.main.activity_main.*
import android.support.design.widget.Snackbar
import android.support.design.widget.FloatingActionButton
import com.example.sam.reproductordemusica.service.PlayMusicService


class MainActivity : AppCompatActivity() {

    var songModelData: ArrayList<SongModel> = ArrayList()
    var songListAdapter: SongListAdapter?=null
    var para:Boolean = false
    companion object {
        val PERMISSION_REQUEST_CODE = 12
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            var musicDataIntent = Intent(applicationContext, PlayMusicService::class.java)
            musicDataIntent.putExtra("parar", "parar")
            applicationContext.startService(musicDataIntent)
        }

        if (ContextCompat.checkSelfPermission(applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }else{
            cargarDatos()
        }
    }

    fun cargarDatos(){
        var songCursor:Cursor? = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null)
        while (songCursor!=null && songCursor.moveToNext()){
            var songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            var songDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            var songPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            songModelData.add(SongModel(songName, songDuration, songPath))
        }
        songListAdapter = SongListAdapter(songModelData, applicationContext)
        var layoutManger = LinearLayoutManager(applicationContext)
        recycler_view.layoutManager = layoutManger
        recycler_view.adapter = songListAdapter
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode ==  PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT).show()
                cargarDatos()
            }
        }
    }
}
