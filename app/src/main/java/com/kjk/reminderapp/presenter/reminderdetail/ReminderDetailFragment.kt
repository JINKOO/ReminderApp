package com.kjk.reminderapp.presenter.reminderdetail

import android.app.TimePickerDialog
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
import com.kjk.reminderapp.R
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.databinding.FragmentReminderDetailBinding

/**
 *  알림 설정하는 Fragment
 */
class ReminderDetailFragment : Fragment() {

    /**
     *  data binding
     */
    private lateinit var binding: FragmentReminderDetailBinding


    /**
     * viewModelFactory
     */
    private lateinit var viewModelFactory: ReminderViewModelFactory


    /**
     *  viewModel
     */
    private lateinit var viewModel: ReminderDetailViewModel


    /**
     *  onCreateView
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reminder_detail,
            container,
            false
        )

        // init viewModel
        val application = requireNotNull(activity).application
        val dataSource = ReminderDatabase.getInstance(application).reminderDatabaseDao

        //safe-args
        val arguments = ReminderDetailFragmentArgs.fromBundle(requireArguments())
        Log.d(TAG, "onCreateView: ${arguments.reminder.title}")

        // init viewModel
        viewModelFactory = ReminderViewModelFactory(dataSource, arguments.reminder)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReminderDetailViewModel::class.java)

        // init databinding
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        //
        initLayout()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
    }

    /**
     *  layout 초기화
     */
    private fun initLayout() {
        setTimePicker()
    }


    /**
     *  ViewModel의 LiveData Observe
     */
    private fun observe() {
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer { toMove ->
            if (toMove) {
                // HomeFragment로 이동
                moveToHomeFragment()
                viewModel.onSaveClickEventDone()
            }
        })
    }


    /**
     *  home fragment로 이동
     */
    private fun moveToHomeFragment() {
        this.findNavController()
            .navigate(ReminderDetailFragmentDirections
                .actionReminderDetailFragmentToReminderHomeFragment())
    }


    /**
     *  setTimePickerDialog
     */
    private fun setTimePicker() {
        binding.timePicker.apply { 
            setOnTimeChangedListener { view, hourOfDay, minute ->
                Log.d(TAG, "setTimePicker: ${hourOfDay}, ${minute}")
                // database에 저장하기위해, 지정한 알림 시간을 milliseconds로 변환.
                viewModel.setRemindTime(hourOfDay, minute)
            }
        }
    }

    companion object {
        private const val TAG = "DetailFragment"
    }
}