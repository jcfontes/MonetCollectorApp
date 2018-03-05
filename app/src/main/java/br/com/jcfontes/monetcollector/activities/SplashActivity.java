package br.com.jcfontes.monetcollector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import br.com.jcfontes.monetcollector.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_splash_activity);

        ivLogo = findViewById(R.id.ivLogo);
        this.load();
    }

    private void load() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash);
        animation.reset();
        ivLogo.clearAnimation();
        ivLogo.startAnimation(animation);

        new Handler().postDelayed(() -> {
            Intent nextScreen = new Intent(SplashActivity.this, ValidateLoginActivity.class);
            startActivity(nextScreen);
            SplashActivity.this.finish();
        }, 3000);

    }

}
