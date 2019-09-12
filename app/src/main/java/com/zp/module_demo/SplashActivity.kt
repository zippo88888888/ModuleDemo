package com.zp.module_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zp.module_base.content.jumpActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jumpActivity(MainActivity::class.java)
        overridePendingTransition(0, 0)
        finish()
    }
}
