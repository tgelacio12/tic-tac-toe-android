/*
TicTacToe League Application
PROG3210 Assignment 2

Activity Name: ScoreboardActivity
Purpose:
    Displays the leaderboard and allows the user to add a new player

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreboardActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {

    // Declarations
    private ListView statsListView;
    private TextView idTextView;
    private EditText txtName;
    private Button btnInsert;
    private PlayerDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        setTitle(R.string.scoreboard_title);

        // Initializations
        statsListView = (ListView) findViewById(R.id.statsListView);
        txtName = (EditText) findViewById(R.id.txtName);
        btnInsert = (Button) findViewById(R.id.btnInsert);

        // Set onClickListener
        btnInsert.setOnClickListener(this);
        statsListView.setOnItemClickListener(this);
        db = new PlayerDB(this);

        // update display
        updateDisplay();
    }

    ///
    /// updateDisplay() method
    ///
    private void updateDisplay() {
        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data = db.getPlayersStats();

        // create the resource, from, and to variables
        int resource = R.layout.statslistview_item;
        String[] from = {"id", "name", "wins", "losses", "ties"};
        int[] to = {R.id.idTextView, R.id.nameTextView, R.id.winsTextView, R.id.lossesTextView, R.id.tiesTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        statsListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        // Get player ID from textview
        TextView idTextView = (TextView) view.findViewById(R.id.idTextView);
        int playerId = Integer.parseInt(idTextView.getText().toString());
        // Get player info
        HashMap<String, String> data = db.getIndividualPlayerStats(playerId);

        // Create intent
        Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);

        intent.putExtra("id", Integer.toString(playerId));
        intent.putExtra("playerName",data.get("name"));
        intent.putExtra("wins", data.get("wins"));
        intent.putExtra("losses", data.get("losses"));
        intent.putExtra("ties", data.get("ties"));

        // Start player activity
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnInsert){
            String newPlayer = txtName.getText().toString();

            try {
                db.insertPlayer(newPlayer);
                updateDisplay();
                txtName.setText("");
            }

            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    ///
    /// Event handler of onCreateOptionsMenu method
    ///
    ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scoreboard_activity, menu);
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
            case R.id.mnuStartGame:
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
