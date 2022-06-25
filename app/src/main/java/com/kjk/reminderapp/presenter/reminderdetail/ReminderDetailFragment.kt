package com.kjk.reminderapp.presenter.reminderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kjk.reminderapp.R
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
     *  viewModel
     */
    private val viewModel: ReminderDetailViewModel by lazy {
        ViewModelProvider(this).get(ReminderDetailViewModel::class.java)
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

    }


    /**
     *  ViewModel의 LiveData Observe
     */
    private fun observe() {
        viewModel.navigateToHome.observe(viewLifecycleOwner, Observer { toMove ->
            if (toMove) {
                // HomeFragment로 이동
                moveToHomeFragment()
                viewModel.navigateToHomeDone()
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
}