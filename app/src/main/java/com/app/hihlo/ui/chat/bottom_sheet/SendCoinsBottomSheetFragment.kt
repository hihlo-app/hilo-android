package com.app.hihlo.ui.chat.bottom_sheet

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.app.hihlo.R
import com.app.hihlo.databinding.FragmentSendCoinsBottomSheetBinding
import com.app.hihlo.model.recharge_package.response.RechargePackageListResponse
import com.app.hihlo.ui.chat.adapter.AdapterSendCoinsBottomSheet
import com.app.hihlo.ui.chat.view_model.SendCoinsBottomSheetViewModel
import com.app.hihlo.utils.CommonUtils.toPx
import com.app.hihlo.utils.network_utils.ProcessDialog
import com.app.hihlo.utils.network_utils.Status
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import kotlin.getValue

class
SendCoinsBottomSheetFragment(val totalAvailableCoins: Int?) : BottomSheetDialogFragment() {
    private var screen: String = ""
    private var userId: String = ""
    private var _binding: FragmentSendCoinsBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SendCoinsBottomSheetViewModel by viewModels()
    var onCoinsSelected: ((RechargePackageListResponse.Payload) -> Unit)? = null


    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendCoinsBottomSheetBinding.inflate(inflater, container, false)
        viewModel.hitRechargeCoinsApi()

        return binding.root
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)

                // Make bottom sheet fill the screen height
//                it.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//                it.requestLayout()
//
//                // Configure behavior to open fully
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                behavior.skipCollapsed = true
//                behavior.isFitToContents = false
//                behavior.expandedOffset = 0 // full expanded height (0px from top)

                // 3. Optional: Set rounded corners
                val cornerRadius = 25f.toPx(requireContext())
                val shapeDrawable = MaterialShapeDrawable(
                    ShapeAppearanceModel.Builder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, cornerRadius)
                        .setTopRightCorner(CornerFamily.ROUNDED, cornerRadius)
                        .build()
                )
                shapeDrawable.fillColor = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.bottom_sheet_color))
                it.background = shapeDrawable

            }
        }

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        onClick()
        binding.totalCoins.text = "$totalAvailableCoins"
        // Retrieve arguments from the bundle
        screen = arguments?.getString("screen") ?: ""
        userId = arguments?.getString("userId") ?: ""
    }

    private fun onClick() {
        binding.closePredefined.setOnClickListener {
            dismiss()
        }
    }

    private fun setObserver() {
        viewModel.getRechargeCoinsLiveData().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.e("TAG", "Following List success: ${Gson().toJson(it)}")
                    if (it.data?.status==1){
                        if (it.data.code == 200){
                            binding.coinsTypeRecycler.adapter = AdapterSendCoinsBottomSheet(it.data.payload, ::getSelectedCoins)

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


    fun getSelectedCoins(data:RechargePackageListResponse.Payload){
        dismiss()
        onCoinsSelected?.invoke(data)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}