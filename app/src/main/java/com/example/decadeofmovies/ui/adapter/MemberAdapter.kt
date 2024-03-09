package com.example.decadeofmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.decadeofmovies.databinding.ItemMemberBinding

class MemberAdapter(
    private val membersList: MutableList<String>
): RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(
            ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(membersList[position])
    }

    override fun getItemCount(): Int {
        return membersList.size
    }

    class MemberViewHolder(private val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(member: String) {
            binding.apply {
                tvMember.text = member
            }
        }
    }

    fun updateList(list: List<String>) {
        membersList.clear()
        membersList.addAll(list)
        this.notifyDataSetChanged()
    }
}