package com.example.fahad.nashra;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import static java.security.AccessController.getContext;

public class SelectGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_select_game);
    }

    public void directBaloot(View v) {
        Intent intent = new Intent(this, BalootActivity.class);
        startActivity(intent);
    }

    public void directBint(View v) {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.players_num)
            .setItems(R.array.four_five_players, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // The 'which' argument contains the index position
                    // of the selected item
                    switch (which){
                        case 0:
                            // four
                            Intent intent = new Intent(getBaseContext(), BintFourActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            // five
                            Intent intent5 = new Intent(getBaseContext(), BintFiveActivity.class);
                            startActivity(intent5);
                            break;
                    }
                }}).show();

    }

}
