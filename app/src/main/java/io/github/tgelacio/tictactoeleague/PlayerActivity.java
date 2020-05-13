/*
TicTacToe League Application
PROG3210 Assignment 2

Activity Name: PlayerActivity
Purpose:
    Display the player's statistics and offers the option
    to edit or delete the player

Revision History
    Tonnicca Gelacio, 2019-11-22: Created
    Tonnicca Gelacio, 2019-11-22: Code Updated
    Tonnicca Gelacio, 2019-11-23: Code Updated
    Tonnicca Gelacio, 2019-11-24: Code and UI Updated
 */

package io.github.tgelacio.tictactoeleague;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class PlayerActivity extends AppCompatActivity
        implements View.OnClickListener, EditDialog.EditDialogListener {

    // Declarations
    String name, playerId, wins, losses, ties, updatedName;
    TextView playerNameTextView, playerWinsTextView,
            playerLossesTextView, playerTiesTextView;
    Button btnEdit, btnDelete;
    PlayerDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // get references to widgets
        playerNameTextView = (TextView) findViewById(R.id.lblPlayerName);
        playerWinsTextView = (TextView) findViewById(R.id.txtPlayerWins);
        playerLossesTextView = (TextView) findViewById(R.id.txtPlayerLosses);
        playerTiesTextView = (TextView) findViewById(R.id.txtPlayerTies);
        btnEdit = (Button) findViewById(R.id.btnEditPlayerName);
        btnDelete = (Button) findViewById(R.id.btnDeletePlayer);
        db = new PlayerDB(this);

        // set onClickListener
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        // get the intent
        Intent intent = getIntent();

        // get data from the intent
        playerId = intent.getStringExtra("id");
        name = intent.getStringExtra("playerName");
        wins = intent.getStringExtra("wins");
        losses = intent.getStringExtra("losses");
        ties = intent.getStringExtra("ties");

        // display data on the widgets
        playerNameTextView.setText(name);
        playerWinsTextView.setText(wins);
        playerLossesTextView.setText(losses);
        playerTiesTextView.setText(ties);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditPlayerName:
                showEditDialog();
                break;
            case R.id.btnDeletePlayer:
                showDeleteDialog(Integer.parseInt(playerId));
                break;
            default:
                break;
        }
    }

    ///
    /// showEditDialog() method
    ///
    private void showEditDialog() {
        EditDialog editDialog = new EditDialog();
        editDialog.show(getSupportFragmentManager(), "Edit Dialog");
    }

    ///
    /// updatePlayer() method
    ///
    public void updatePlayer(int id, String editedName)
    {
        db.updatePlayerName(id, editedName);

        playerNameTextView.setText(editedName);
        Toast.makeText(this, "Player name has been updated.", Toast.LENGTH_LONG).show();
    }

    ///
    /// showDeleteDialog() method
    ///
    public void showDeleteDialog (int playerId)
    {
        final int toDelete = playerId;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to delete this player?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePlayer(toDelete);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    ///
    /// deletePlayer() method
    ///
    public void deletePlayer (int id)
    {
        db.deletePlayer(id);

        startActivity(new Intent(getApplicationContext(), ScoreboardActivity.class));
        Toast.makeText(this, "Player has been deleted.", Toast.LENGTH_LONG).show();
    }

    ///
    /// getEditedPlayerName method
    ///
    @Override
    public void getEditedPlayerName(String editedName) {

        int id = Integer.parseInt(playerId);
        updatePlayer(id, editedName);
    }

    ///
    /// Event handler of onCreateOptionsMenu method
    ///
    ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_player_activity, menu);
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
            case R.id.mnuViewScoreboard:
                startActivity(new Intent(getApplicationContext(), ScoreboardActivity.class));;
                return true;
            case R.id.mnuHome:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.mnuStartGame:
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                //Toast.makeText(this, "Start Game selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
