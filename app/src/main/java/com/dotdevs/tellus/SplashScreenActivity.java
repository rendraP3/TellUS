package com.dotdevs.tellus;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.dotdevs.tellus.view.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.logo)
    ImageView logo;

    private Runnable mRunnable = () -> {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        AnimatorSet mAnimator = new AnimatorSet();
        long ANIMATION_DURATION_MILLISECONDS = 1000;
        mAnimator.setDuration(ANIMATION_DURATION_MILLISECONDS);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ValueAnimator mScaleUpXAnimator = ObjectAnimator.ofFloat(logo, "scaleX", 0.0f, 1.0f);
        ValueAnimator mScaleUpYAnimator = ObjectAnimator.ofFloat(logo, "scaleY", 0.0f, 1.0f);

        mAnimator.play(mScaleUpXAnimator).with(mScaleUpYAnimator);
        mAnimator.start();

        Handler mHandler = new Handler();
        long DELAY_DURATION_MILLISECONDS = 2000;
        mHandler.postDelayed(mRunnable, DELAY_DURATION_MILLISECONDS);
    }
}
