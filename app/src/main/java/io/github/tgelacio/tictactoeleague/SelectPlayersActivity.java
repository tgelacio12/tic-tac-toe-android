/*
TicTacToe League Application
PROG3210 Assignment 2

Activity Name: GameActivity
Purpose:
    Allows the user to select players before starting the game

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectPlayersActivity extends AppCompatActivity
    implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    // Declarations
    private Button btnStartGame;
    private Spinner spnPlayer1, spnPlayer2;
    private ArrayList<String> playerList;
    private TextView lblPlayer1Warning, lblPlayer2Warning;
    private String player1Warning, player2Warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_players);
        setTitle(R.string.selectPlayersText);

        // Initialize from intent
        Intent intent = getIntent();
        player1Warning = intent.getStringExtra("selectPlayer1");
        player2Warning = intent.getStringExtra("selectPlayer2");

        // Initializations
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        spnPlayer1 = (Spinner) findViewById(R.id.spnSelectPlayer1);
        spnPlayer2 = (Spinner) findViewById(R.id.spnSelectPlayer2);
        lblPlayer1Warning = (TextView) findViewById(R.id.lblPlayer1Warning);
        lblPlayer2Warning = (TextView) findViewById(R.id.lblPlayer2Warning);

        // Set onClickListener
        spnPlayer1.setOnItemSelectedListener(this);
        spnPlayer2.setOnItemSelectedListener(this);
        btnStartGame.setOnClickListener(this);

        // Loading spinner data from database
        populateSpinner();

        // Set text for labels
        if (player1Warning == null)
        {
            lblPlayer1Warning.setText("");
        }
        else
        {
            lblPlayer1Warning.setText(player1Warning);
        }

        if (player2Warning == null)
        {
            lblPlayer2Warning.setText("");
        }
        else
        {
            lblPlayer2Warning.setText(player2Warning);
        }

    }

    ///
    /// populateSpinner() method
    ///
    private void populateSpinner() {
        // database handler
        PlayerDB db = new PlayerDB(getApplicationContext());

        // Spinner Drop down elements
        playerList = db.getPlayerNames();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, playerList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnPlayer1.setAdapter(dataAdapter);
        spnPlayer2.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnStartGame:
                // Save Player 1 and Player 2 to intent
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("player1", spnPlayer1.getSelectedItem().toString());
                intent.putExtra("player2", spnPlayer2.getSelectedItem().toString());

                //Start Game
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    ///
    /// Event handler of onCreateOptionsMenu method
    ///
    ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selectplayer_activity, menu);
        return true;
    }

    ///
    /// Event handler of onOptionsItemSelected method
    ///
    ///
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.mnuHome:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.mnuViewScoreboard:
                startActivity(new Intent(getApplicationContext(), ScoreboardActivity.class));;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
