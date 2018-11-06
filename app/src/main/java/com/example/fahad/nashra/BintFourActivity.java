package com.example.fahad.nashra;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class BintFourActivity extends AppCompatActivity {
    int focusBox; // current focused edittext
    EditText[] boxes; // for storing pointers to score boxes

    int[] totals;

    TextView[] scores;

    TextView[] lastRecords;

    final int GAME_LIMIT = -1; // when game is done, -1 means not feature
    final int ALERT_WAIT_TIME = 800; // 3s

    final int PLAYERS_COUNT = 4;

    final Context c = this;

    int winner_index;
    int loser_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bint_four);

        // initilizing variables
        boxes = new EditText[PLAYERS_COUNT];
        scores = new TextView[PLAYERS_COUNT];
        lastRecords = new TextView[PLAYERS_COUNT];
        totals = new int[PLAYERS_COUNT];

        // winner and loser are not chosen yet
        winner_index = 0; loser_index = 0;

        for(int i=0; i < PLAYERS_COUNT; i++){
            int resId = getResources().getIdentifier("player" + String.valueOf(i+1) + "Enter", "id", getPackageName());
            boxes[i] = (EditText) findViewById(resId);

            resId = getResources().getIdentifier("player" + String.valueOf(i+1) + "Score", "id", getPackageName());
            scores[i] = (TextView) findViewById(resId);

            resId = getResources().getIdentifier("lastRecord" + String.valueOf(i+1), "id", getPackageName());
            lastRecords[i] = (TextView) findViewById(resId);
        }


        final int unfocusedColor = getResources().getColor(R.color.bint_acc);
        final int focusedColor = getResources().getColor(R.color.baloot_acc);

        for(int i=0; i < PLAYERS_COUNT; i++) {
            final EditText box = boxes[i];
            final int finI = i;
            box.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        box.getBackground().setColorFilter(focusedColor, PorterDuff.Mode.SRC_ATOP);
                        focusBox = finI;
                    } else {
                        box.getBackground().setColorFilter(unfocusedColor, PorterDuff.Mode.SRC_ATOP);
                        focusBox = -1;
                    }
                }
            });
        }

        // set focus of first player
        boxes[0].requestFocus();

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
            case R.id.calc_left_minus_btn:
                digit = -2;
                break;
        }

        if(focusBox != -1){
            if(digit == -1) {
                boxes[focusBox].setText("");
            }else if(digit == -2){
                String currVal = boxes[focusBox].getText().toString();
                if(currVal.length() == 0){
                    boxes[focusBox].setText("-");
                }else if(currVal.charAt(0) != '-'){
                    StringBuilder test = new StringBuilder();
                    test.append("-");
                    test.append(currVal);
                    boxes[focusBox].setText(test);
                }
            }else{
                String currVal = boxes[focusBox].getText().toString();
                StringBuilder test = new StringBuilder();
                test.append(currVal);
                test.append(digit);
                boxes[focusBox].setText(test);
            }
        }
    }
    public void returnBack(View v) {
        this.finish();
    }

    public void registerResult(final View v) {

        int gameDone = -1; // -1 means not done, assign id to winner

        int new_winner = -1, new_loser = -1; // if changed then new players are found

        for(int i=0; i < PLAYERS_COUNT; i++) {
            String boxStr = boxes[i].getText().toString();
            int enteredResult = ("".equals(boxStr))? 0 : Integer.parseInt(boxStr);
            totals[i] += enteredResult;

            //update final scores
            scores[i].setText(Integer.toString(totals[i]));

            //zero field
            boxes[i].setText("");

            // update the board
            String newRecord = enteredResult + lastRecords[i].getText().toString().substring(1);
            if(enteredResult == 0) newRecord = "-" + lastRecords[i].getText().toString().substring(1);
            lastRecords[i].setText(newRecord);

            // get the board
            int resId = getResources().getIdentifier("player" + String.valueOf(i+1) + "Board", "id", getPackageName());
            LinearLayout board = (LinearLayout) findViewById(resId);

            TextView value = new TextView(this);
            value.setText("-/"+scores[i].getText());
            value.setTextAppearance(R.style.record_typo);
            value.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            board.addView(value);

            lastRecords[i] = value;

            if(GAME_LIMIT > 0 && totals[i] >= GAME_LIMIT) gameDone = i;

        }

        for(int i=0; i < PLAYERS_COUNT; i++){
            // check for winners and losers
            if(totals[i] <= totals[winner_index] && (new_winner == -1 || totals[i] < totals[new_winner])){
                new_winner = i;
            }
            if(totals[i] >= totals[loser_index] && (new_loser == -1 || totals[i] > totals[new_loser])){
                new_loser = i;
            }
        }

        if(new_winner != -1){
            // switch winner flag
            changeStatus(true, winner_index, new_winner);
            winner_index = new_winner;
        }
        if(new_loser != -1){
            // switch loser flag
            changeStatus(false, loser_index, new_loser);
            loser_index = new_loser;
        }
        // check if game is done
        if(gameDone >= 0){
            for(int i=0; i < PLAYERS_COUNT; i++) {
                // look for highest winner
                if(i != gameDone && totals[i] > totals[gameDone]){
                    // found a higher winner
                    gameDone = i;
                }
            }

            int resId = getResources().getIdentifier("player" + String.valueOf(gameDone+1) + "Text", "id", getPackageName());
            TextView player_name = (TextView) findViewById(resId);

            final String finMessage = player_name.toString() + R.string.has_won;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    newGameAlert(v, finMessage, "a");
                }
            }, ALERT_WAIT_TIME);
        }else{
            boxes[0].requestFocus();
        }

        // scroll scrollview to down
        ScrollView mScrollview = (ScrollView) findViewById(R.id.scrollView2);
        mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void nextField(View v) {
        if (focusBox == -1){
            boxes[0].requestFocus();
        }else{
            boxes[(focusBox + 1) % PLAYERS_COUNT].requestFocus();
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

    private void changeStatus(boolean action, int old_index, int new_index){ // true for win, false for lose
        if(action) {
            scores[old_index].setBackgroundColor(getResources().getColor(R.color.inside_bgcolor));
            scores[new_index].setBackgroundColor(getResources().getColor(R.color.winning));
        }else{
            scores[old_index].setBackgroundColor(getResources().getColor(R.color.inside_bgcolor));
            scores[new_index].setBackgroundColor(getResources().getColor(R.color.losing));
        }
    }

    public void changeName(View v){
        // create a pop-up and change name of the player's name

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_change_name, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        final TextView player_name = (TextView) v;
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        player_name.setText(userInputDialogEditText.getText());
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
}
