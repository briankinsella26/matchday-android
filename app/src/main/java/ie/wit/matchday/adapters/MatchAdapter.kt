package ie.wit.matchday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.matchday.databinding.CardMatchBinding
import ie.wit.matchday.models.MatchModel

class MatchAdapter constructor(private var matches: List<MatchModel>) :
    RecyclerView.Adapter<MatchAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMatchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val match = matches[holder.adapterPosition]
        holder.bind(match)
    }

    override fun getItemCount(): Int = matches.size

    class MainHolder(private val binding : CardMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: MatchModel) {
            binding.matchOpponent.text = match.opponent
            binding.result.text = match.result
            binding.homeAway.text = match.homeOrAway
        }
    }
}