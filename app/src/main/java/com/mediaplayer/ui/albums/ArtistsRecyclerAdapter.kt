package com.mediaplayer.ui.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mediaplayer.app.R
import com.mediaplayer.repository.Artist
import com.mediaplayer.ui.songs.ArtistItemViewHolder

class ArtistsRecyclerAdapter(private val artists: List<Artist>, private val callback: (Artist) -> Unit) : RecyclerView.Adapter<ArtistItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_artist_item, parent, false) as ViewGroup
        return ArtistItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    override fun onBindViewHolder(holder: ArtistItemViewHolder, position: Int) {
        artists[position].run {
            holder.setTitle(this.name)
            holder.itemView.setOnClickListener {
                this@ArtistsRecyclerAdapter.callback(this)
            }
        }


    }

}