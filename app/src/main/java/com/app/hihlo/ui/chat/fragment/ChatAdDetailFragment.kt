package com.app.hihlo.ui.chat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.hihlo.R
import com.app.hihlo.databinding.FragmentChatAdDetailBinding
import com.app.hihlo.databinding.FragmentPredefinedChatBinding
import com.app.hihlo.ui.home.activity.HomeActivity

class ChatAdDetailFragment : Fragment() {
    private lateinit var binding: FragmentChatAdDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatAdDetailBinding.inflate(layoutInflater)
        onClick()
        (requireActivity() as? HomeActivity)?.setOnlineStatusVisibility(false)
        return binding.root
    }
    fun onClick(){
        binding.apply {
            backButton.setOnClickListener {
                (requireActivity() as? HomeActivity)?.setOnlineStatusVisibility(true)
                findNavController().popBackStack()
            }
            exitButton.setOnClickListener {
                (requireActivity() as? HomeActivity)?.setOnlineStatusVisibility(true)
                findNavController().popBackStack()
            }
            viewAdsButton.setOnClickListener {
                (requireActivity() as? HomeActivity)?.setOnlineStatusVisibility(false)
                findNavController().navigate(R.id.viewAdsFragment)
            }
        }
    }
}