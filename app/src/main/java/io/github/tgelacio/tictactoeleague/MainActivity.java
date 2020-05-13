/*
TicTacToe League Application
PROG3210 Assignment 2

Activity Name: MainActivity
Purpose:
    Displays the application's main menu

Revision History
    Tonnicca Gelacio, 2019-11-11: Created
    Tonnicca Gelacio, 2019-11-11: Code Updated
    Tonnicca Gelacio, 2019-11-12: Code Updated
    Tonnicca Gelacio, 2019-11-19: UI Updated
    Tonnicca Gelacio, 2019-11-22: Code Updated
    Tonnicca Gelacio, 2019-11-23: Code Updated
    Tonnicca Gelacio, 2019-11-24: Code and UI Updated
 */

package io.github.tgelacio.tictactoeleague;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    // Declarations
    private Button btnStartGame, btnScoreboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializations
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        btnScoreboard = (Button) findViewById(R.id.btnScoreboard);

        // Set buttons' onClickListener
        btnStartGame.setOnClickListener(this);
        btnScoreboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnStartGame:
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                break;
            case R.id.btnScoreboard:
                startActivity(new Intent(getApplicationContext(), ScoreboardActivity.class));
                break;
            default:
                break;
        }

    }

}
