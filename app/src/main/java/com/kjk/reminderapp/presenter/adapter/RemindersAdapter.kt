package com.kjk.reminderapp.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kjk.reminderapp.R
import com.kjk.reminderapp.data.local.ReminderEntity

import com.kjk.reminderapp.databinding.ItemListReminderBinding

/**
 *  ReminderHomeFragment에 적용할 Adapter
 */
class RemindersAdapter: RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>(){


    private var reminders: List<ReminderEntity> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        return RemindersViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        holder.bind(reminders[position])
    }


    override fun getItemCount() = reminders.size


    fun updateAll(reminders: List<ReminderEntity>) {
        this.reminders = reminders
        notifyDataSetChanged()
    }


    /**
     *  ViewHolderClass
     */
    class RemindersViewHolder(
        private val binding: ItemListReminderBinding
    ): RecyclerView.ViewHolder(binding.root) {


        fun bind(reminder: ReminderEntity) {
            binding.reminder = reminder
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): RemindersViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ItemListReminderBinding>(
                    layoutInflater,
                    R.layout.item_list_reminder,
                    parent,
                    false
                )
                return RemindersViewHolder(binding)
            }
        }
    }
}