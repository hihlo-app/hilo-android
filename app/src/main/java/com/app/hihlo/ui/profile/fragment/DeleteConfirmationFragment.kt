package com.app.hihlo.ui.profile.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.hihlo.R
import com.app.hihlo.databinding.FragmentDeleteConfirmationBinding
import com.app.hihlo.databinding.FragmentNotificationSettingBinding
import com.app.hihlo.model.get_notification_setting.response.GetNotificationSettingResponse
import com.app.hihlo.model.login.response.LoginResponse
import com.app.hihlo.model.set_notification.SetNotificationRequest
import com.app.hihlo.model.static.NotificationsTypeModel
import com.app.hihlo.preferences.LOGIN_DATA
import com.app.hihlo.preferences.NOTIFICATION_TOGGLE
import com.app.hihlo.preferences.Preferences
import com.app.hihlo.ui.profile.view_model.NotificationSettingsViewModel
import com.app.hihlo.ui.reels.bottom_sheet.BlockFlagBottomSheet
import com.app.hihlo.utils.CommonUtils.showCustomDialogWithBinding
import com.app.hihlo.utils.network_utils.ProcessDialog
import com.app.hihlo.utils.network_utils.Status
import com.google.gson.Gson
import kotlin.getValue

class DeleteConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentDeleteConfirmationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteConfirmationBinding.inflate(layoutInflater)
        initViews()
        return binding.root
    }
    private fun initViews(){
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.deleteLayout.setOnClickListener {
            showCustomDialogWithBinding(requireContext(), "Do you really want to Delete the Account?",
                onYes = {
                    val bottomSheetFragment = BlockFlagBottomSheet()
                    val bundle = Bundle().apply {
                        putString("screen", "delete_account")  // Add your arguments here
                    }
                    bottomSheetFragment.arguments = bundle
                    bottomSheetFragment.show(requireActivity().supportFragmentManager, "BlockBottomSheet")
                },
                onNo = {
                    //dismiss()
                }
            )
        }
    }

}
