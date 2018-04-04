package com.example.deon_mass.lekiosque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TypeUser extends AppCompatActivity {

    TextView type_ancien, type_nouveau;
    CardView Card_configuration_ForOld,BTN_Next;
    LinearLayout Firstform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_type_user);


        /**
         * Dans le cas où l'utilisateur est un nouveau
         */
        type_nouveau = (TextView) findViewById(R.id.type_nouveau);
        type_nouveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Blank_Profil.class));
                finish();
            }
        });

        /**
         * Dans le cas où l'utilisateur est un ancien
         */
        Firstform = (LinearLayout) findViewById(R.id.Firstform);
        BTN_Next = (CardView) findViewById(R.id.BTN_Next);
        Card_configuration_ForOld = (CardView) findViewById(R.id.Card_configuration_ForOld);
        type_ancien = (TextView) findViewById(R.id.type_ancien);
        type_ancien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card_configuration_ForOld.setVisibility(View.VISIBLE);
                BTN_Next.setVisibility(View.VISIBLE);
                Firstform.setVisibility(View.GONE);
            }
        });
        BTN_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Blank_Profil.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

