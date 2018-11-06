package com.example.fahad.nashra;

import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class BalootActivity extends AppCompatActivity {
    EditText themBox;
    EditText usBox;
    EditText focusBox; // current focused edittext
    int themTotal = 0;
    int usTotal = 0;

    TextView themScore;
    TextView usScore;
    ArrayList<Integer> themScoreRecords;
    ArrayList<Integer> usScoreRecords;

    ArrayList<Integer> themRecordIds;
    ArrayList<Integer> usRecordIds;

    final int GAME_LIMIT = 152; // when game is done
    final int ALERT_WAIT_TIME = 800; // 3s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baloot);

        themScore = (TextView) findViewById(R.id.themScore);
        usScore = (TextView) findViewById(R.id.player1Score);

        themScoreRecords = new ArrayList<>();
        usScoreRecords = new ArrayList<>();

        themScoreRecords.add(0);
        usScoreRecords.add(0);

        themRecordIds = new ArrayList<>();
        usRecordIds = new ArrayList<>();

        themRecordIds.add(R.id.themLastRecord);
        usRecordIds.add(R.id.usLastRecord);


        final int unfocusedColor = getResources().getColor(R.color.bint_acc);
        final int focusedColor = getResources().getColor(R.color.baloot_acc);
        themBox = (EditText) findViewById(R.id.themEnter);
        themBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    themBox.getBackground().setColorFilter(focusedColor, PorterDuff.Mode.SRC_ATOP);
                    focusBox = themBox;
                } else {
                    themBox.getBackground().setColorFilter(unfocusedColor, PorterDuff.Mode.SRC_ATOP);
                    focusBox = null;
                }
            }
        });

        usBox = (EditText) findViewById(R.id.usEnter);
        usBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    usBox.getBackground().setColorFilter(focusedColor, PorterDuff.Mode.SRC_ATOP);
                    focusBox = usBox;
                } else {
                    usBox.getBackground().setColorFilter(unfocusedColor, PorterDuff.Mode.SRC_ATOP);
                    focusBox = null;
                }
            }





        });




    }

    public void calcClick(View v) {

        int digit = 0;

        switch(v.getId()){
            case R.id.calc_0_btn:
                digit = 0;
                break;
            case R.id.calc_1_btn:
                digit = 1;
                break;
            case R.id.calc_2_btn:
                digit = 2;
                break;
            case R.id.calc_3_btn:
                digit = 3;
                break;
            case R.id.calc_4_btn:
                digit = 4;
                break;
            case R.id.calc_5_btn:
                digit = 5;
                break;
            case R.id.calc_6_btn:
                digit = 6;
                break;
            case R.id.calc_7_btn:
                digit = 7;
                break;
            case R.id.calc_8_btn:
                digit = 8;
                break;
            case R.id.calc_9_btn:
                digit = 9;
                break;
            case R.id.calc_left_btn:
                digit = -1;
                break;
        }

        if(focusBox != null){
            if(digit == -1){
                focusBox.setText("");
            }else {
                String currVal = focusBox.getText().toString();
                StringBuilder test = new StringBuilder();
                test.append(currVal);
                test.append(digit);
                focusBox.setText(test);
            }
        }
    }
    public void returnBack(View v) {
        this.finish();
    }

    public void registerResult(final View v) {
        String themStr = themBox.getText().toString();
        String usStr = usBox.getText().toString();
        int themResult = ("".equals(themStr))? 0 : Integer.parseInt(themBox.getText().toString());
        int usResult = ("".equals(usStr))? 0 : Integer.parseInt(usBox.getText().toString());

        if(themResult >= 0 && usResult >= 0) {
            themTotal += themResult;
            usTotal += usResult;
        }

        // update final scores
        themScore.setText(Integer.toString(themTotal));
        usScore.setText(Integer.toString(usTotal));

        //update recoreds
        themScoreRecords.add(themResult);
        usScoreRecords.add(usResult);

        // zero fields
        themBox.setText("");
        usBox.setText("");

        // get last record
        TextView themLastRecord = (TextView) findViewById(themRecordIds.get(themRecordIds.size() - 1));
        TextView usLastRecord = (TextView) findViewById(usRecordIds.get(usRecordIds.size() - 1));

        // update record history
        String themNewRecord = themResult + themLastRecord.getText().toString().substring(1);
        String usNewRecord = usResult + usLastRecord.getText().toString().substring(1);

        if(themResult == 0) themNewRecord = "-" + themLastRecord.getText().toString().substring(1);
        if(usResult == 0) usNewRecord = "-" + usLastRecord.getText().toString().substring(1);


        themLastRecord.setText(themNewRecord);
        usLastRecord.setText(usNewRecord);


        LinearLayout themBoard = (LinearLayout) findViewById(R.id.themBoard);
        LinearLayout usBoard = (LinearLayout) findViewById(R.id.usBoard);

        TextView themValue = new TextView(this);
        int someId = View.generateViewId();
        themValue.setId(someId);
        themRecordIds.add(someId); // register the view ID
        themValue.setText("-/"+themScore.getText());
        themValue.setTextAppearance(R.style.record_typo);
        themValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        themBoard.addView(themValue);

        TextView usValue = new TextView(this);
        someId = View.generateViewId();
        usValue.setId(someId);
        usRecordIds.add(someId); // register the view ID
        usValue.setText("-/"+usScore.getText());
        usValue.setTextAppearance(R.style.record_typo);
        usValue.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        usBoard.addView(usValue);

        // update last record variable
//        themLastRecord = themValue;
//        usLastRecord = usValue;


        // check result in case the game is done.
        int usScoreInt = Integer.parseInt(usScore.getText().toString());
        int themScoreInt = Integer.parseInt(themScore.getText().toString());
        final int remaining = Math.abs(usScoreInt - themScoreInt);

        if(usScoreInt > GAME_LIMIT || themScoreInt > GAME_LIMIT){
            String message = "";

            if(remaining == 0){
                //draw
                message = getString(R.string.gameIsDraw);
            }else if(usScoreInt > themScoreInt){
                // we won
                message = getString(R.string.weWonGame);
            }else{
                //them won
                message = getString(R.string.themWonGame);
            }

            final String finMessage = message;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newGameAlert(v, finMessage, Integer.toString(remaining));
                }
            }, ALERT_WAIT_TIME);
        }else{
            // focus on them box
            themBox.requestFocus();
        }

    }

    public void nextField(View v) {
        if (focusBox == null){
            themBox.requestFocus();
        }else if (focusBox == themBox){
            usBox.requestFocus();
        }else if (focusBox == usBox){
            themBox.requestFocus();
        }
    }


    private void newGameAlert(final View v, String whoWon, String remaining) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(whoWon)
                .setMessage(R.string.gameIsDone + " " + remaining)
                .setPositiveButton(R.string.newGame, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        newGame(v); // restart the game
                    }
                })
                .setNegativeButton(R.string.reviewGame, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }

    public void newGame(View v){
        // create a new game
        recreate();
    }

    public void editLastGame(View v){

        int themSize = themRecordIds.size();
        int usSize = usRecordIds.size();

        if(themSize < 2 || usSize < 2) return;

        // update official score
        themTotal -= themScoreRecords.get(themSize - 1);
        usTotal -= usScoreRecords.get(usSize - 1);

        themScore.setText(Integer.toString(themTotal));
        usScore.setText(Integer.toString(usTotal));

        //fill boxes with last score and delete last view
        themBox.setText(Integer.toString(themScoreRecords.get(themSize - 1)));
        usBox.setText(Integer.toString(usScoreRecords.get(usSize - 1)));

        // update before last record
        if(themSize > 1 && usSize > 1 ) {
            TextView themView = (TextView) findViewById(themRecordIds.get(themSize - 2));
            TextView usView = (TextView) findViewById(usRecordIds.get(usSize - 2));

            themView.setText("-/"+themScore.getText());
            usView.setText("-/"+usScore.getText());
        }

        TextView themView = (TextView) findViewById(themRecordIds.get(themSize - 1));
        TextView usView = (TextView) findViewById(usRecordIds.get(usSize - 1));

        themRecordIds.remove(themSize - 1);
        usRecordIds.remove(usSize - 1);

        ((ViewGroup)themView.getParent()).removeView(themView);
        ((ViewGroup)usView.getParent()).removeView(usView);

    }

}