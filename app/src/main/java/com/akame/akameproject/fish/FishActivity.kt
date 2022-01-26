package com.akame.akameproject.fish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akame.akameproject.R
import com.akame.akameproject.databinding.ActivityFishBinding

class FishActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFishBinding
    private val fishDrawable by lazy { FishDrawable() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivTest.setImageDrawable(fishDrawable)
    }

    override fun onResume() {
        super.onResume()
        fishDrawable.startAnimator()
    }
}