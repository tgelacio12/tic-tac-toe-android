/*
TicTacToe League Application
PROG3210 Assignment 2

Activity Name: GameActivity
Purpose:
    Allows two players to play TicTacToe and
    updates the database depending on the result

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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity
        implements View.OnClickListener {

    //Declarations - Constants
    public static final int NUMBER_OF_COLUMNS = 3;
    public static final int NUMBER_OF_ROWS = 3;
    public static final int MAX_MOVES = 9;
    private PlayerDB db;

    // Declarations and Initializations - Global Variables
    Button [][] aButtons = new Button[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
    String [][] gameBoardContent = new String[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];
    String  player1, player2, winner, currentPlayer, nextPlayer;
    int movesCounter = 0;
    boolean winnerFound = false;
    boolean vacant = true;
    TextView playerTurnLabel, player1display, player2display;

    // Declaration - SharedPreference
    private SharedPreferences sharedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle(R.string.game_title);

        Intent intent = getIntent();
        player1 = intent.getStringExtra("player1");
        player2 = intent.getStringExtra("player2");

        // Check if player1 and player2 are set
        if (player1 == null || player2 == null)
        {
            // Call SelectPlayersActivity if a player is not selected.
            Intent i = new Intent(getApplicationContext(), SelectPlayersActivity.class);

            if (player1 == null)
            {
                // Prepare label for error.
                i.putExtra("selectPlayer1", "Please select Player 1.");
            }

            if (player2 == null)
            {
                // Prepare label for error.
                i.putExtra("selectPlayer2", "Please select Player 2.");
            }

            startActivity(i);
        }

        else if (player1.equals(player2))
        {
            Intent i = new Intent(getApplicationContext(), SelectPlayersActivity.class);
            i.putExtra("selectPlayer2", "Player 2 must be different from Player 1.");
            startActivity(i);
        }

        else {
            // Initialize database
            db = new PlayerDB(this);

            // Initialize attributes from the form
            playerTurnLabel = findViewById(R.id.playerTurnLabel);
            player1display = findViewById(R.id.player1display);
            player2display = findViewById(R.id.player2display);
            aButtons[0][0] = findViewById(R.id.button00);
            aButtons[0][1] = findViewById(R.id.button01);
            aButtons[0][2] = findViewById(R.id.button02);
            aButtons[1][0] = findViewById(R.id.button10);
            aButtons[1][1] = findViewById(R.id.button11);
            aButtons[1][2] = findViewById(R.id.button12);
            aButtons[2][0] = findViewById(R.id.button20);
            aButtons[2][1] = findViewById(R.id.button21);
            aButtons[2][2] = findViewById(R.id.button22);

            // Initialize SharedPreferences
            this.sharedPlace = getSharedPreferences("SharedPlace", MODE_PRIVATE);

            // Initialize arrays
            for (int j = 0; j < NUMBER_OF_COLUMNS; j++) {
                for (int i = 0; i < NUMBER_OF_ROWS; i++) {
                    // Set click event handler
                    aButtons[i][j].setOnClickListener(this);

                    aButtons[i][j].setText("");
                    gameBoardContent[i][j] = "";
                }
            }

            // Set text
            playerTurnLabel.setText(player1 + " starts.");
            player1display.setText(player1);
            player2display.setText(player2);
        }
    }

    ///
    /// Click event handler of the buttons
    ///
    ///
    @Override
    public void onClick(View v)
    {
        Button selectedButton = (Button) v;
        String buttonValue = (String) selectedButton.getText();

        // Check if selected tile is empty
        if (buttonValue.isEmpty())
        {
            movesCounter++;

            // Change button's text to X or O
            if (movesCounter%2 != 0)
            {
                selectedButton.setText("X");
                currentPlayer = player1;
                nextPlayer = player2;
                playerTurnLabel.setText(nextPlayer + "'s turn.");
            }

            else
            {
                selectedButton.setText("O");
                currentPlayer = player2;
                nextPlayer = player1;
                playerTurnLabel.setText(nextPlayer + "'s turn.");
            }

            // update boardContent
            switch (selectedButton.getId()) {
                case R.id.button00:
                    gameBoardContent[0][0] = selectedButton.getText().toString();
                    break;
                case R.id.button01:
                    gameBoardContent[0][1] = selectedButton.getText().toString();
                    break;
                case R.id.button02:
                    gameBoardContent[0][2] = selectedButton.getText().toString();
                    break;
                case R.id.button10:
                    gameBoardContent[1][0] = selectedButton.getText().toString();
                    break;
                case R.id.button11:
                    gameBoardContent[1][1] = selectedButton.getText().toString();
                    break;
                case R.id.button12:
                    gameBoardContent[1][2] = selectedButton.getText().toString();
                    break;
                case R.id.button20:
                    gameBoardContent[2][0] = selectedButton.getText().toString();
                    break;
                case R.id.button21:
                    gameBoardContent[2][1] = selectedButton.getText().toString();
                    break;
                case R.id.button22:
                    gameBoardContent[2][2] = selectedButton.getText().toString();
                    break;
                default:
                    Toast.makeText(this, "Default switch onClick error.",  Toast.LENGTH_LONG).show();
                    break;
            }

            // Check for winner
            if ((movesCounter >= 5) & (movesCounter <= MAX_MOVES))
            {
                winnerFound = CheckWinner();
                vacant = CheckVacant();

                if (winnerFound)
                {
                    winner = currentPlayer;
                    playerTurnLabel.setText(winner + " wins!");
                    UpdatePlayerWinLoss(winner, nextPlayer);
                    GameFinished();

                }

                else
                {
                    if (vacant == false)
                    {
                        playerTurnLabel.setText("Draw.");
                        UpdatePlayerDraws(player1, player2);
                        GameFinished();
                    }
                }
            }
        }
    }

    //
    // UpdatePlayerWInsLoss() method
    // To update a player's win/loss on the database
    // depending on the results.
    //
    private void UpdatePlayerWinLoss(String winner, String loser) {
        db.updatePlayerWins(winner);
        db.updatePlayerLosses(loser);
    }

    //
    // UpdatePlayerDraws() method
    // To update the players draws on the database
    // after a tie.
    //
    private void UpdatePlayerDraws(String player1, String player2) {
        db.updatePlayerTies(player1);
        db.updatePlayerTies(player2);
    }

    ///
    /// CheckWinner() method
    /// To check if there's a winner after a player's turn
    ///
    public Boolean CheckWinner()
    {
        //Declarations and Initializations
        Boolean winnerFound;
        Boolean rowWinner = false;
        Boolean columnWinner = false;
        Boolean diagonalWinner = false;
        String[][] boardContent = gameBoardContent;

        //Check Rows
        for (int i = 0; i < boardContent.length; i++)
        {
            if ((boardContent[i][0] == boardContent [i][1]) &
                    (boardContent[i][1] == boardContent[i][2]) &
                    (boardContent[i][0].isEmpty() == false ))
            {
                rowWinner = true;
            }
        }

        //Check Columns
        for (int j=0; j < boardContent[0].length; j++)
        {
            if ((boardContent[0][j] == boardContent[1][j]) &
                    (boardContent[1][j] == boardContent[2][j]) &
                    (boardContent[0][j].isEmpty() == false))
            {
                columnWinner = true;
            }
        }

        //Check Diagonal
        if ((boardContent[0][0] == boardContent[1][1] &
                boardContent[1][1] == boardContent[2][2]) ||
                (boardContent[2][0] == boardContent[1][1] &
                        boardContent[1][1] == boardContent[0][2]))
        {
            if (boardContent[1][1].isEmpty() == false)
            {
                diagonalWinner = true;
            }
        }

        if (rowWinner || columnWinner || diagonalWinner)
        {
            winnerFound = true;
        }

        else
        {
            winnerFound = false;
        }

        return winnerFound;
    }

    ///
    /// CheckVacant() method
    /// To check if there are remaining vacant tiles on the board
    ///
    public Boolean CheckVacant()
    {
        //Declarations
        int vacantCount;
        Boolean vacant;
        String[][] boardContent = gameBoardContent;

        //Initializations
        vacantCount = 0;
        vacant = true;

        //Check for vacant slots
        for (int i = 0; i < boardContent.length; i++)
        {
            for (int j=0; j < boardContent[0].length; j++)
            {
                if (boardContent[i][j].isEmpty())
                {
                    vacantCount++;
                }
            }
        }

        if (vacantCount > 0)
        {
            vacant = true;
        }

        else
        {
            vacant = false;
        }

        return vacant;
    }

    ///
    /// NewGame() method
    /// To re-initialize the game board and the labels once players'
    /// decide to start a new game.
    ///
    public void NewGame()
    {
        for (int j = 0; j < NUMBER_OF_COLUMNS; j++)
        {
            for(int i = 0; i < NUMBER_OF_ROWS; i++)
            {
                aButtons[i][j].setText("");
                aButtons[i][j].setClickable(true);
                gameBoardContent[i][j] = "";
                playerTurnLabel.setText(player1 + " starts.");
            }
        }

        // set movesCounter to zero
        movesCounter = 0;
        winnerFound = false;
    }

    ///
    /// GameFinished() method
    /// To disable the tiles on the board once a game ends.
    ///
    public void GameFinished()
    {
        for (int j = 0; j < NUMBER_OF_COLUMNS; j++)
        {
            for(int i = 0; i < NUMBER_OF_ROWS; i++)
            {
                // Make buttons unclickable once game ends
                aButtons[i][j].setClickable(false);
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
        inflater.inflate(R.menu.menu_game_activity, menu);
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
            case R.id.mnuNewGame:
                NewGame();
                return true;
            case R.id.mnuHome:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.mnuViewScoreboard:
                startActivity(new Intent(getApplicationContext(), ScoreboardActivity.class));
                return true;
            case R.id.mnuSelectPlayer:
                startActivity(new Intent(getApplicationContext(), SelectPlayersActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
