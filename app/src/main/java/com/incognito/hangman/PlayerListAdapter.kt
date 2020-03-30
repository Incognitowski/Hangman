package com.incognito.hangman

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.player_card.view.*

class PlayerListAdapter(private val playerList: List<PlayerEntity>, private val context: Context) : RecyclerView.Adapter<PlayerListAdapter.PlayerListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerListViewHolder {

        val playerCard = LayoutInflater.from(parent.context).inflate(R.layout.player_card, parent, false);

        return PlayerListViewHolder(playerCard)

    }

    override fun onBindViewHolder(holder: PlayerListViewHolder, position: Int) {

        val currentPlayer : PlayerEntity = playerList[position];

        holder.playerNameTextView.text = currentPlayer.userName;
        holder.scoreTextView.text = "Pontuação: " + currentPlayer.score.toString();
        holder.deleteBtn.setTag(R.string.btn_delete_player_tag, currentPlayer.uid);

    }

    override fun getItemCount() = playerList.size;

    class PlayerListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val scoreTextView : TextView = itemView.playerScoreTextView;
        val playerNameTextView : TextView = itemView.playerNameTextView;
        val deleteBtn : ImageButton = itemView.deletePlayerBtn;

    }

}