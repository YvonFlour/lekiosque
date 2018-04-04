package com.example.deon_mass.lekiosque;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;

import Adapters.NosComposants;
import Adapters.ViewPagerAdapter;
import Db.Notices;

public class MainActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    public static NosComposants nosComposants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nosComposants = new NosComposants();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        tabLayout = (TabLayout)findViewById(R.id.TabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        tabLayout.addTab(tabLayout.newTab().setText("Mes articles"));
        tabLayout.addTab(tabLayout.newTab().setText("Favoris"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setSoundEffectsEnabled(true);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundColor(Color.parseColor("#0e3870"));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                PopupMenu popupMenu = new PopupMenu(MainActivity.this,fab);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            //TODO: Menu pour la publication d'un article
                            case R.id.M_publication:
                                Intent i = new Intent(getApplicationContext(), Blank_Annonce.class);
                                i.putExtra("NOTHING","NOTHING");
                                startActivity(i);
                                finish();
                                break;
                            //TODO: Menu vers les astuces de fonctionnement
                            case R.id.M_astuce:
                                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout._astuce, null);
                                AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this)
                                        .setCancelable(false)
                                        .setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setView(v);

                                a.show();
                            break;
                            //TODO: Menu vers le profil de l'utilisateur
                            case R.id.M_profil:
                                startActivity(new Intent(MainActivity.this, MonProfil.class));
                                finish();
                                break;
                            //TODO: Menu pour la sortie
                            case R.id.M_Exit:
                                onBackPressed();
                                break;
                            //TODO: Menu pour le rafraichissement des données
                            case R.id.M_actualiser:
                                Notices.update(MainActivity.this);
                                break;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder a = new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Voulez-vous vraiment quitter ?")
                .setCancelable(false)
                .setPositiveButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                a.show();

    }

}
