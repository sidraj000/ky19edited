package org.kashiyatra.ky19;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PhotoActivity extends AppCompatActivity {
    private View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int day = intent.getIntExtra("day", 1);
        int placeholderId;
        String scheduleUrl;
        switch (day) {

            case 0:
                placeholderId = R.drawable.day0;
                scheduleUrl = "https://i.ibb.co/qdQyVXr/Day-0.jpg";
                getSupportActionBar().setTitle("Day 0");
                break;
            case 1:
                placeholderId = R.drawable.day1;
                scheduleUrl ="https://i.ibb.co/27C28JJ/Day-1.jpg";
                getSupportActionBar().setTitle("Day 1");
                break;
            case 2:
                placeholderId = R.drawable.day2;
                scheduleUrl = "https://i.ibb.co/jhTw3Kc/Day-2.jpg";
                getSupportActionBar().setTitle("Day 2");
                break;
            case 3:
                placeholderId = R.drawable.day3;
                scheduleUrl = "https://i.ibb.co/DMmGWKF/Day-3.jpg";
                getSupportActionBar().setTitle("Day 3");
                break;
            default:
                placeholderId = R.drawable.day0;
                scheduleUrl = "https://i.ibb.co/qdQyVXr/Day-0.jpg";
                getSupportActionBar().setTitle("Day 0");
        }

        ImageView photoView = findViewById(R.id.photo_view);

        Glide.with(this)
                .load(scheduleUrl)
                .apply(new RequestOptions()
                        .placeholder(placeholderId)
                        .error(placeholderId)
                        .fitCenter()
                        .dontAnimate()
                        .dontTransform())
                .into(photoView);

        background = findViewById(R.id.photo_id);

        if (savedInstanceState == null) {
            background.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }
    }

    private void circularRevealActivity() {
        int cx = background.getWidth()/2;
        int cy = background.getBottom() - getDips(250);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                background,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(400);
        background.setVisibility(View.VISIBLE);
        circularReveal.start();

    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = background.getWidth()/2;
            int cy = background.getBottom() - getDips(250);

            float finalRadius = Math.max(background.getWidth(), background.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);

            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    background.setVisibility(View.INVISIBLE);
                    finish();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            circularReveal.setDuration(400);
            circularReveal.start();
        }
        else {
            super.onBackPressed();
        }
    }
}
