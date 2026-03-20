package com.app.hihlo.ui.chat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.hihlo.R
import com.app.hihlo.databinding.FragmentOpenAdBinding
import com.app.hihlo.databinding.FragmentOpenImageBinding
import com.app.hihlo.ui.home.activity.HomeActivity
import com.bumptech.glide.Glide

class OpenImageFragment : Fragment() {
    private lateinit var binding: FragmentOpenImageBinding
    private val navArgs: OpenImageFragmentArgs by navArgs()
    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOpenImageBinding.inflate(layoutInflater)
        (requireActivity() as HomeActivity).setOnlineStatusVisibility(true)
        imageUrl = navArgs.imageUrl
        Glide.with(requireContext()).load(imageUrl).into(binding.imageView)
        onClick()
        return binding.root
    }

    private fun onClick() {
        binding.backButton.setOnClickListener {
            (requireActivity() as HomeActivity).setOnlineStatusVisibility(false)
            findNavController().popBackStack()
        }
    }

}