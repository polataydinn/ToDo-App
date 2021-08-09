package com.example.to_do.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.R
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoData
import com.example.to_do.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ToDoData) {
            binding.titleTxt.text = item.title
            binding.descriptionTxt.text = item.description

            when (item.priority) {
                Priority.HIGH -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.priorityIndicator.context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.priorityIndicator.context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> binding.priorityIndicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        binding.priorityIndicator.context,
                        R.color.green
                    )
                )
            }

            binding.rowBackground.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
                binding.rowBackground.findNavController().navigate(action)
            }
        }
    }

    fun setData(toDoData: List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}