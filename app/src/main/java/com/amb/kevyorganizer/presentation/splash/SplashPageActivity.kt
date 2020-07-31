package com.amb.kevyorganizer.presentation.splash

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.amb.kevyorganizer.R
import com.amb.kevyorganizer.presentation.MainActivity

class SplashPageActivity : AppCompatActivity() {

    private val splashAnimationView by lazy { findViewById<LottieAnimationView>(R.id.splashAnimationView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_page)

        splashAnimationView.run {
            setAnimation(SPLASH_ANIMATION_PATH)
            playAnimation()
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    redirectToMainActivity()
                }
            })
        }
    }

    private fun redirectToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    companion object {
        const val SPLASH_TIME_OUT = 1500L
        const val SPLASH_ANIMATION_PATH = "cubes_isometric.json"
    }
}
