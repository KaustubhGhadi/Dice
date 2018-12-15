package in.programmeraki.glaty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = this;

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }, 2500);
    }
}
