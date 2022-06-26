package com.kjk.reminderapp.presenter.reminderdetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kjk.reminderapp.R
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.databinding.FragmentReminderDetailBinding
import com.kjk.reminderapp.presenter.util.SelectRingtoneContract

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
     * 시스템 ringtone 선택 화면에서
     * 사용자가 선택한 ringtone result처리
     */
    private val launcher: ActivityResultLauncher<Int> = registerForActivityResult(SelectRingtoneContract()) { result ->
        result?.let {
            Log.d(TAG, "${result}, ${RingtoneManager.getRingtone(requireActivity(),result).getTitle(activity)} ")
            val ringtone = RingtoneManager.getRingtone(requireActivity(), result)
            viewModel.setRingtoneTitle(ringtone.getTitle(activity))

            // test ringtone 실행.
            // TODO 나중에 지워야 하는 코드
            ringtone.play()

        }
    }


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
        val defaultRingtoneUri = Settings.System.DEFAULT_RINGTONE_URI
        val ringtone = RingtoneManager.getRingtone(requireActivity(), defaultRingtoneUri)
        viewModel.setRingtoneTitle(ringtone.getTitle(requireActivity()))
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
        
        viewModel.navigateToSystemRingtone.observe(viewLifecycleOwner, Observer { toMove ->
            if (toMove) {
                moveToSystemRingtone()
                viewModel.navigateToSelectRingtoneDone()
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
     *  system ringtone 화면 이동
     */
    private fun moveToSystemRingtone() {
        Log.d(TAG, "moveToSystemRingtone: ")
        /**
         * TYPE_RINGTONE, TYPE_NOTIFICATION, TYPE_ALARM, TYPE_ALL 중에 선택 가능하다.
         * 이중의 하나를 intent로 전달할 때 사용한다.
         */
        launcher.launch(RingtoneManager.TYPE_ALL)

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