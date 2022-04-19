package com.akame.akameproject.hotfix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akame.akameproject.R

class HotFixActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_fix)
        HotFixClass().todo()
    }
}