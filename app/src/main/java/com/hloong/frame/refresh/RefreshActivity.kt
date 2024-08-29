package com.hloong.frame.refresh

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hloong.frame.R
import com.hloong.ui.refresh.Refresh
import com.hloong.ui.refresh.RefreshLayout
import com.hloong.ui.refresh.TextOverView

class RefreshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh)
        val refreshLayout = findViewById<RefreshLayout>(R.id.refresh_layout)
        val overView = TextOverView(this)
        refreshLayout.setRefreshOverView(overView)
        refreshLayout.setRefreshListener(object:Refresh.RefreshListener{
            override fun onRefresh() {
                Handler().postDelayed({refreshLayout.refreshFinished()},1000)
            }

            override fun enableRefresh(): Boolean {
               return true
            }

        })
    }
}
