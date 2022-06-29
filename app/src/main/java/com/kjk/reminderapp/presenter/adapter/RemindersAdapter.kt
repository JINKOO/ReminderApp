package com.kjk.reminderapp.presenter.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kjk.reminderapp.R

import com.kjk.reminderapp.databinding.ItemListReminderBinding
import com.kjk.reminderapp.domain.vo.ReminderVO

/**
 *  ReminderHomeFragment에 적용할 Adapter
 */
class RemindersAdapter(
    private val itemCallBack: OnItemClickListener,
    private val checkCallBack: OnItemCheckBoxListener
) : RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder>() {


    private var reminders: List<ReminderVO> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemindersViewHolder {
        return RemindersViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: RemindersViewHolder, position: Int) {
        holder.bind(reminders[position], itemCallBack, checkCallBack)
    }


    override fun getItemCount() = reminders.size


    fun updateAll(reminders: List<ReminderVO>) {
        this.reminders = reminders
        notifyDataSetChanged()
    }


    /**
     *  ViewHolderClass
     */
    class RemindersViewHolder(
        private val binding: ItemListReminderBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(reminder: ReminderVO, callBack: OnItemClickListener, checkCallBack: OnItemCheckBoxListener) {
            binding.reminder = reminder
            binding.callBack = callBack
            binding.checkCallBack = checkCallBack
            binding.executePendingBindings()

            Log.d(TAG, "bind: ${reminder.isActivate}")

            // fragment에서 click event를 handle한다.
            binding.reminderCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                binding.isChecked = isChecked
            }
        }


        companion object {
            private const val TAG = "ReminderViewHolder"
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


/**
 *  adatper item click listener
 */
class OnItemClickListener(
    private val clickListener: ((reminder: ReminderVO) -> Unit)
) {
    fun onItemClick(reminder: ReminderVO) = clickListener(reminder)
}


/**
 *  checkBox click Listener
 */
class OnItemCheckBoxListener(
    private val clickListener: ((isChecked: Boolean, reminder: ReminderVO) -> Unit))
{
    fun onItemCheckBoxClicked(isChecked: Boolean, reminder: ReminderVO) = clickListener(isChecked, reminder)
}
