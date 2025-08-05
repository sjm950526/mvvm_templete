package com.example.mvvm_templete.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_templete.AppApplication
import com.example.mvvm_templete.common.Utils
import com.example.mvvm_templete.common.base.BaseActivity
import com.example.mvvm_templete.common.base.BaseRecyclerViewAdapter
import com.example.mvvm_templete.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val mViewModel: MainViewModel by viewModels()
    lateinit var mAdapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = MainRecyclerViewAdapter(this)
        permissionCheck()
        uiInit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var tmpGranted = true

        when (requestCode) {
            100 -> {
                if (grantResults.isNotEmpty()) {
                    for ((i, _) in permissions.withIndex()) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            tmpGranted = false
                            Toast.makeText(this, "서비스 사용 불가", Toast.LENGTH_SHORT).show()
                        }
                    }

                    if (tmpGranted) {
                        AppApplication.mService?.requestStartService()
                    }
                }
            }
        }
    }

    fun permissionCheck() {
        if (!Utils.hasPermissions(this, Manifest.permission.POST_NOTIFICATIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }


    fun uiInit() {
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = mAdapter

        mViewModel.mErrorMsg.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        mViewModel.mDataList.observe(this) {
            mAdapter.updateItems(it)
        }

        mAdapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    mAdapter.getItem(position)?.title ?: "정보 없음",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        mViewModel.getList()
    }
}