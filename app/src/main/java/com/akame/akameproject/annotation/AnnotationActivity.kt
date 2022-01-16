package com.akame.akameproject.annotation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.akame.akameproject.R

class AnnotationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annotation)
        AnnotationClickManager.inject(this)
    }

    @InjectClick(R.id.btn1, R.id.btn2)
    fun onClick(view: View) {
        when (view.id) {
            R.id.btn1 -> {
                Toast.makeText(this, "btn1", Toast.LENGTH_SHORT).show()
            }

            R.id.btn2 -> {
                Toast.makeText(this, "btn2", Toast.LENGTH_SHORT).show()
            }
        }
    }
}