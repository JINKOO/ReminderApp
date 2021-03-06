package com.kjk.reminderapp.presenter.reminderdetail

import android.media.RingtoneManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kjk.reminderapp.R
import com.kjk.reminderapp.data.local.ReminderDatabase
import com.kjk.reminderapp.databinding.FragmentReminderDetailBinding
import com.kjk.reminderapp.presenter.reminderalarm.AlarmFunctions
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
     *  alarm function
     */
    private val alarmFunction by lazy {
        AlarmFunctions(requireActivity())
    }


    /**
     * 시스템 ringtone 선택 화면에서
     * 사용자가 선택한 ringtone result처리
     */
    private val launcher: ActivityResultLauncher<Int> =
        registerForActivityResult(SelectRingtoneContract()) { result ->
            result?.let {
                //Log.d(TAG, "${result}, ${RingtoneManager.getRingtone(requireActivity(), result).getTitle(requireActivity())}")
                val ringtone = RingtoneManager.getRingtone(requireActivity(), result)
                viewModel.setRingtone(result.toString(), ringtone.getTitle(requireActivity()))
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

        // safe-args
        val arguments = ReminderDetailFragmentArgs.fromBundle(requireArguments())
        Log.d(TAG, "onCreateView: arguments : ${arguments.reminderId}")

        // init viewModel
        viewModelFactory = ReminderViewModelFactory(arguments.reminderId, alarmFunction)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReminderDetailViewModel::class.java)

        // init databinding
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayout()
        observe()
    }


    /**
     *  layout 초기화
     */
    private fun initLayout() {

        val supportActionBar = requireActivity().actionBar
        supportActionBar?.setTitle(R.string.toolbar_title_detail)
        if (viewModel.reminderSettingTaskType == ReminderSettingTaskType.CREATE) {
            setDefaultRingTone()
        }
        setReminderTitle()
        setTimePicker()
    }

    /**
     *  ViewModel의 LiveData Observe
     */
    private fun observe() {
        viewModel.reminder.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observe: ${it}")
        })

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

        viewModel.showInputCheckMessage.observe(viewLifecycleOwner, Observer { toShowMessage ->
            if (toShowMessage) {
                showToastMessage()
                viewModel.showInputCheckMessageDone()

            }
        })
    }


    /**
     *  home fragment로 이동
     */
    private fun moveToHomeFragment() {
        this.findNavController()
            .navigate(
                ReminderDetailFragmentDirections
                    .actionReminderDetailFragmentToReminderHomeFragment()
            )
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
     *  reminder Title
     */
    private fun setReminderTitle() {
        binding.reminderTitleEditText.apply {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setReminderTitle(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }


    /**
     *  setTimePickerDialog
     */
    private fun setTimePicker() {
        binding.timePicker.apply {
            setOnTimeChangedListener { view, hourOfDay, minute ->
                //Log.d(TAG, "setTimePicker: ${hourOfDay}, ${minute}")
                // database에 저장하기위해, 지정한 알림 시간을 milliseconds로 변환.
                viewModel.setReminderTime(hourOfDay, minute)
            }
        }
    }


    /**
     * default ringtone을 set한다.
     */
    private fun setDefaultRingTone() {
        val defaultRingtoneUri = Settings.System.DEFAULT_RINGTONE_URI
        val ringtone = RingtoneManager.getRingtone(requireActivity(), defaultRingtoneUri)
        Log.d(TAG, "initLayout: CREATE ${ringtone.getTitle(requireActivity())}")
        viewModel.setRingtone(defaultRingtoneUri.toString(), ringtone.getTitle(requireActivity()))
    }


    /**
     *  Toast message를 보여주는 함수.
     */
    private fun showToastMessage() {
        Toast.makeText(requireActivity(), getString(R.string.all_input_check_message), Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val TAG = "DetailFragment"
    }
}
