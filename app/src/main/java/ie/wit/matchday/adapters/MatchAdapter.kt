package ie.wit.matchday.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.matchday.databinding.CardMatchBinding
import ie.wit.matchday.models.MatchModel

interface MatchClickListener {
    fun onMatchClick(match: MatchModel)
}

class MatchAdapter constructor(private var matches: ArrayList<MatchModel>,
                                  private val listener: MatchClickListener)
    : RecyclerView.Adapter<MatchAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMatchBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val match = matches[holder.adapterPosition]
        holder.bind(match, listener)
    }

    fun removeAt(position: Int) {
        matches.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = matches.size

    inner class MainHolder(val binding : CardMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: MatchModel, listener: MatchClickListener) {
            binding.match = match
            binding.root.setOnClickListener { listener.onMatchClick(match) }
            binding.executePendingBindings()
        }
    }
}