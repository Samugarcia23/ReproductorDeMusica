package com.example.sam.reproductordemusica.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.sam.reproductordemusica.MainActivity
import com.example.sam.reproductordemusica.adapters.SongListAdapter

/**
 * Created by Sam on 14/02/2018.
 */
class PlayMusicService:Service() {

    var currentPos:Int = 0
    var musicDataList:ArrayList<String> = ArrayList()
    var mp:MediaPlayer?=null
    var s:String?=null
    var b=false

    override fun onBind(p0: Intent?): IBinder ?{
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent!!.getStringExtra("parar")!=null){
            s = intent!!.getStringExtra("parar")
            b=false
        }else{
            currentPos = intent!!.getIntExtra(SongListAdapter.MUSICITEMPOS,0)
            musicDataList = intent!!.getStringArrayListExtra(SongListAdapter.MUSICLIST)
            b=true
        }

        if(mp!=null){
            if(s.equals("parar")){
                mp!!.stop()
            }else{
                mp!!.stop()
                mp!!.release()
                mp = null
            }
        }

        if(b) {
            mp = MediaPlayer()
            mp!!.setDataSource(musicDataList[currentPos])
            mp!!.prepare()
            mp!!.setOnPreparedListener {
                mp!!.start()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}