package com.app.hihlo.ui.profile.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.hihlo.R
import com.app.hihlo.base.BaseActivity
import com.app.hihlo.databinding.ActivitySignupFlowBinding
import com.app.hihlo.databinding.ActivityViewAdsBinding

class ViewAdsActivity  : BaseActivity<ActivityViewAdsBinding>() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.view_ads_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_view_ads // Ensure this points to the correct layout resource
    }
}