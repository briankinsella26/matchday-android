package ie.wit.matchday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.matchday.databinding.CardMatchBinding
import ie.wit.matchday.models.MatchModel

interface MatchListener {
    fun onMatchClick(match: MatchModel)
}

class MatchAdapter constructor(private var matches: List<MatchModel>, private val listener: MatchListener) :
    RecyclerView.Adapter<MatchAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMatchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val match = matches[holder.adapterPosition]
        holder.bind(match, listener)
    }

    override fun getItemCount(): Int = matches.size

    class MainHolder(private val binding : CardMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: MatchModel, listener: MatchListener) {
            binding.matchOpponent.text = match.opponent
            binding.result.text = match.result
            binding.homeAway.text = if(match.home) "home" else "away"
            binding.root.setOnClickListener { listener.onMatchClick(match)}
        }
    }
}