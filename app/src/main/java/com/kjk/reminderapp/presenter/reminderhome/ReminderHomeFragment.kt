package com.kjk.reminderapp.presenter.reminderhome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjk.reminderapp.R
import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.databinding.FragmentReminderHomeBinding
import com.kjk.reminderapp.presenter.adapter.OnItemClickListener
import com.kjk.reminderapp.presenter.adapter.RemindersAdapter

/**
 *  reminder list를 보여주는
 *  reminder 홈 화면
 */
class ReminderHomeFragment : Fragment() {

    /**
     * data Binding
     */
    private lateinit var binding: FragmentReminderHomeBinding


    /**
     * viewModel
     */
    private val viewModel: ReminderHomeViewModel by lazy {
        ViewModelProvider(this).get(ReminderHomeViewModel::class.java)
    }


    /**
     *  adapter 정의
     */
    private val reminderAdapter: RemindersAdapter by lazy {
        RemindersAdapter(OnItemClickListener { reminder ->
            Log.d(TAG, "onClick :: ${reminder.title}")
            viewModel.setClickedReminder(reminder)
        })
    }


    /**
     *
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reminder_home,
            container,
            false
        )

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()

        // observe
        observe()
    }


    /**
     *  layout 초기화
     */
    private fun initLayout() {
        binding.reminderRecyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = reminderAdapter
        }
    }



    /**
     * viewModel의 liveData observing
     */
    private fun observe() {
        viewModel.toAddNewReminder.observe(viewLifecycleOwner, Observer { toMove ->
            Log.d(TAG, "observe: ${toMove}")
            if (toMove) {
                moveToDetailFragment()
                viewModel.navigateToDetailDone()
            }
        })


        viewModel.reminder.observe(viewLifecycleOwner, Observer { reminder ->
            reminder?.let {
                moveToDetailFragment(reminder.reminderId)
                viewModel.navigateToDetailDone()
            }
        })
    }



    /**
     *  ReminderDetailFragment destination으로 navigate
     */
    private fun moveToDetailFragment(reminderId: Long = 0L) {
        this.findNavController()
            .navigate(ReminderHomeFragmentDirections
                .actionReminderHomeFragmentToReminderDetailFragment().apply {
                    // default value가 아닌 경우,
                    setReminderId(reminderId)
                })
    }


    companion object {
        private const val TAG = "HomeFragment"
    }
}