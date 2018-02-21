package com.example.sam.reproductordemusica.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.sam.reproductordemusica.Interface.ItemClickListener
import com.example.sam.reproductordemusica.R
import com.example.sam.reproductordemusica.model.SongModel
import com.example.sam.reproductordemusica.service.PlayMusicService
import java.util.concurrent.TimeUnit

/**
 * Created by Sam on 13/02/2018.
 */
class SongListAdapter(SongModel:ArrayList<SongModel>, context:Context):RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {
    var mContext= context
    var mSongModel = SongModel
    var allMusicList:ArrayList<String> = ArrayList()

    companion object {
        val MUSICLIST = "musiclist"
        val MUSICITEMPOS = "pos"
    }

    override fun getItemCount(): Int {
        return mSongModel.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SongListViewHolder {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.song_list2, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder?, position: Int) {
        var model = mSongModel[position]
        var songName = model.mSongName
        var songDuration = toMandS(model.mSongDuration.toLong())

        holder!!.songTV.text = songName
        holder.durationTV.text = songDuration
        holder.setOnItemClickListener(object:ItemClickListener{
            override fun onItemClick(view: View, pos: Int) {
                for (i in 0 until mSongModel.size){
                    allMusicList.add(mSongModel[i].mSongPath)
                }
                Toast.makeText(mContext, "Titulo: "+ songName, Toast.LENGTH_SHORT).show()
                var musicDataIntent = Intent(mContext, PlayMusicService::class.java)
                musicDataIntent.putStringArrayListExtra(MUSICLIST, allMusicList)
                musicDataIntent.putExtra(MUSICITEMPOS, pos)
                mContext.startService(musicDataIntent)
            }
        })
    }

    fun toMandS(millis:Long):String{
      var duration = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
              TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MINUTES.toSeconds(
                      TimeUnit.MILLISECONDS.toMinutes(millis)
              ))
        return duration
    }

    class SongListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var songTV: TextView
        var durationTV: TextView
        var mItemClickListener:ItemClickListener?=null
        init{
            songTV = itemView.findViewById(R.id.song_name_tv)
            durationTV = itemView.findViewById(R.id.song_duration_tv)
            itemView.setOnClickListener(this)
        }
        fun setOnItemClickListener(itemClickListener: ItemClickListener){
            this.mItemClickListener=itemClickListener
        }
        override fun onClick(view: View?){
            this.mItemClickListener!!.onItemClick(view!!,adapterPosition)
        }
    }
}

