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
import com.app.hihlo.databinding.FragmentBlockedUserBinding
import com.app.hihlo.model.block_user.request.BlockUserRequest
import com.app.hihlo.model.get_profile.UserDetails
import com.app.hihlo.model.login.response.LoginResponse
import com.app.hihlo.model.unblock_user.request.UnblockUserRequest
import com.app.hihlo.preferences.LOGIN_DATA
import com.app.hihlo.preferences.Preferences
import com.app.hihlo.ui.profile.adapter.BlockedUserAdapter
import com.app.hihlo.ui.profile.view_model.BlockedUserViewModel
import com.app.hihlo.utils.CommonUtils.showCustomDialogWithBinding
import com.app.hihlo.utils.network_utils.ProcessDialog
import com.app.hihlo.utils.network_utils.Status
import com.google.gson.Gson
import kotlin.getValue

class BlockedUserFragment : Fragment() {
    private lateinit var binding:FragmentBlockedUserBinding
    private lateinit var blockedUserAdapter: BlockedUserAdapter
    private val viewModel: BlockedUserViewModel by viewModels()

    var data = mutableListOf<UserDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentBlockedUserBinding.inflate(layoutInflater)
        return binding.root
    }
    private fun getSelectedUser(userId: String){
        openUnblockUserConfirmationDialog(userId)
    }
    fun openUnblockUserConfirmationDialog(userId: String) {
        showCustomDialogWithBinding(requireContext(), "Are you sure you want to unblock this user?",
            onYes = {
                viewModel.hitUnblockUserApi("Bearer "+ Preferences.getCustomModelPreference<LoginResponse>(requireContext(), LOGIN_DATA)?.payload?.authToken,
                    UnblockUserRequest(unblockId = userId.toString()))
            },
            onNo = {
                //dismiss()
            }
        )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        onClick()
        viewModel.hitBlockedUsersDataApi("Bearer "+ Preferences.getCustomModelPreference<LoginResponse>(requireContext(), LOGIN_DATA)?.payload?.authToken)

    }

    private fun setObserver() {
        viewModel.getBlockedUsersLiveData().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "Blocked List success: ${Gson().toJson(it)}")
                    if (it.data?.status==1){
                        if (it.data.code == 200){
                            val blockedUsers = it.data.payload.blockedUsers

                            if (blockedUsers.isNullOrEmpty()) {
                                binding.rcvBlockedUsers.visibility = View.GONE
                                binding.noBlockId.visibility = View.VISIBLE
                            } else {
                                binding.rcvBlockedUsers.visibility = View.VISIBLE
                                binding.noBlockId.visibility = View.GONE

                                blockedUserAdapter = BlockedUserAdapter(::getSelectedUser, blockedUsers)
                                binding.rcvBlockedUsers.adapter = blockedUserAdapter
                            }
                            Log.i("TAG", "setObserver: "+it.data.payload.blockedUsers)
                            Log.i("TAG", "setObserver: "+it.data.payload)
                           /* blockedUserAdapter= BlockedUserAdapter(::getSelectedUser, it.data.payload.blockedUsers)
                            binding.rcvBlockedUsers.adapter = blockedUserAdapter
                       */     // binding.followersRecycler.adapter = AdapterFollowers(it.data.payload.followersList, screenCheck)
                        }else{
                            Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        }


                    }else{
                        Toast.makeText(requireContext(), "${it.data?.message}", Toast.LENGTH_SHORT).show()
                    }
                    ProcessDialog.dismissDialog(true)
                }
                Status.LOADING -> {
                    ProcessDialog.showDialog(requireContext(), true)
                }
                Status.ERROR -> {
                    Log.e("TAG", "Login Failed: ${it.message}")
                    ProcessDialog.dismissDialog(true)
                }
            }
        }
        viewModel.getUnblockUserLiveData().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "unblock success: ${Gson().toJson(it)}")
                    if (it.data?.status==1){
                        if (it.data.code == 200){
                            Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        }else{
                            Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(), "${it.data?.message}", Toast.LENGTH_SHORT).show()
                    }
                    ProcessDialog.dismissDialog(true)
                }
                Status.LOADING -> {
                    ProcessDialog.showDialog(requireContext(), true)
                }
                Status.ERROR -> {
                    Log.e("TAG", "Login Failed: ${it.message}")
                    ProcessDialog.dismissDialog(true)
                }
            }
        }
    }

    private fun onClick() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}