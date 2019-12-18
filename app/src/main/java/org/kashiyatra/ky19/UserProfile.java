package org.kashiyatra.ky19;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {
ImageView ivMerchandise,ivQrcode,ivFaq,ivTeamMemebers,ivEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ivMerchandise=findViewById(R.id.ivMerchandise);
        ivQrcode=findViewById(R.id.ivQrcode);
        ivFaq=findViewById(R.id.ivFaq);
        ivTeamMemebers=findViewById(R.id.ivTeamMembers);
        ivEvents=findViewById(R.id.btnEventProfile);
        ivEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, EventsActivity.class));

            }
        });
        ivMerchandise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, MerchandiseActivity.class));
            }
        });
        ivFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, FaqActivity.class));
            }
        });
        ivTeamMemebers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, TeamActivity.class));
            }
        });
    }
}
