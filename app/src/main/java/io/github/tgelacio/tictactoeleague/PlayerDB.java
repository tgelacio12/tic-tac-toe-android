/*
TicTacToe League Application
PROG3210 Assignment 2

Class Name: PlayerDB

Revision History
    Tonnicca Gelacio, 2019-11-11: Created
    Tonnicca Gelacio, 2019-11-11: Code Updated
    Tonnicca Gelacio, 2019-11-22: Code Updated
    Tonnicca Gelacio, 2019-11-23: Code Updated
    Tonnicca Gelacio, 2019-11-24: Code Updated
 */

package io.github.tgelacio.tictactoeleague;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerDB {

    // database constants
    public static final String DB_NAME = "player.sqlite";
    public static final int DB_VERSION = 1;

    // player table constants
    public static final String PLAYER_TABLE = "players";

    public static final String PLAYER_ID = "_id";
    public static final int PLAYER_ID_COL = 0;

    public static final String PLAYER_NAME = "name";
    public static final int PLAYER_NAME_COL = 1;

    public static final String PLAYER_WINS = "wins";
    public static final int PLAYER_WINS_COL = 2;

    public static final String PLAYER_LOSSES = "losses";
    public static final int PLAYER_LOSSES_COL = 3;

    public static final String PLAYER_TIES = "ties";
    public static final int PLAYER_TIES_COL = 4;

    // CREATE and DROP TABLE statements
    public static final String CREATE_PLAYER_TABLE =
            "CREATE TABLE " + PLAYER_TABLE + " (" +
                    PLAYER_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL, " +
                    PLAYER_NAME + " VARCHAR NOT NULL, " +
                    PLAYER_WINS + " INTEGER NOT NULL  DEFAULT 0, " +
                    PLAYER_LOSSES + " INTEGER NOT NULL  DEFAULT 0, " +
                    PLAYER_TIES + " INTEGER NOT NULL  DEFAULT 0);";

    public static final String DROP_PLAYER_TABLE =
            "DROP TABLE IF EXISTS " + PLAYER_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // create tables
            db.execSQL(CREATE_PLAYER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {

            db.execSQL(DROP_PLAYER_TABLE);
            Log.d("PlayerDB: ", "Upgrading db from version "
                    + oldVersion + " to " + newVersion);

            onCreate(db);
        }
    }

    // database and database helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    // constructor
    public PlayerDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        openWriteableDB();
        closeDB();
    }

    // private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    // Get Players' Stats
    ArrayList<HashMap<String, String>> getPlayersStats() {

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        openReadableDB();

        String selectQuery = "SELECT _id, name, wins, losses, ties " +
                "FROM players " +
                "ORDER BY wins DESC;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("id", cursor.getString(0));
            map.put("name", cursor.getString(1));
            map.put("wins", cursor.getString(2));
            map.put("losses", cursor.getString(3));
            map.put("ties", cursor.getString(4));
            data.add(map);
        }

        if (cursor != null) {
            cursor.close();
        }

        closeDB();

        return data;
    }

    // Get Players' Names
    ArrayList<String> getPlayerNames() {
        ArrayList<String> data = new ArrayList<String>();
        openReadableDB();

        String selectQuery = "SELECT name " +
                "FROM players " +
                "ORDER BY name";

        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()) {
            data.add(cursor.getString(0));

            //Test - delete later
            System.out.println(cursor.getString(0));
        }

        if (cursor != null) {
            cursor.close();
        }

        closeDB();

        return data;
    }

    ///
    /// Add New Player
    ///
    void insertPlayer(String sName) throws Exception {
        openWriteableDB();

        ContentValues content = new ContentValues();
        content.put("name", sName);

        long nResult = db.insert("players", null, content);

        if (nResult == -1) {
            throw new Exception("No data.");
        }

        closeDB();
    }

    ///
    /// Update Player's Wins
    ///
    void updatePlayerWins(String playerName) {

        openWriteableDB();

        String updateWinsSQL = "UPDATE " + PLAYER_TABLE +
                " SET " + PLAYER_WINS + " = " + PLAYER_WINS + "+1 " +
                "WHERE " + PLAYER_NAME + " = '" + playerName + "';";

        db.execSQL(updateWinsSQL);

        closeDB();
    }

    ///
    /// Update Player's Loss
    ///
    void updatePlayerLosses(String playerName) {

        openWriteableDB();

        String updateLossesSQL = "UPDATE " + PLAYER_TABLE +
                " SET " + PLAYER_LOSSES + " = " + PLAYER_LOSSES + "+1 " +
                "WHERE " + PLAYER_NAME + " = '" + playerName + "';";

        db.execSQL(updateLossesSQL);

        closeDB();
    }

    ///
    /// Update Player's Ties
    ///
    void updatePlayerTies(String playerName) {

        openWriteableDB();

        String updatePlayerTiesSQL = "UPDATE " + PLAYER_TABLE +
                " SET " + PLAYER_TIES + " = " + PLAYER_TIES + "+1 " +
                "WHERE " + PLAYER_NAME + " = '" + playerName + "';";

        db.execSQL(updatePlayerTiesSQL);

        closeDB();
    }

    ///
    /// Update Player Name
    ///
    void updatePlayerName(int playerId, String editedName) {

        openWriteableDB();

        String updatePlayerNameSQL = "UPDATE " + PLAYER_TABLE +
                " SET " + PLAYER_NAME + " = '" + editedName + "' " +
                "WHERE " + PLAYER_ID + " = " + playerId + ";";

        db.execSQL(updatePlayerNameSQL);

        closeDB();
    }

    ///
    /// Update Player Name
    ///
    void deletePlayer(int playerId) {

        openWriteableDB();

        String deletePlayerSQL = "DELETE FROM players WHERE _id = " + playerId + ";";

        db.execSQL(deletePlayerSQL);

        closeDB();
    }

    //
    // Get Player's Stats
    //
    HashMap<String, String> getIndividualPlayerStats(int playerId) {

        HashMap<String, String> data = new HashMap<String, String>();
        openReadableDB();

        String selectQuery = "SELECT name, wins, losses, ties " +
                "FROM players " +
                "WHERE _id = " + playerId + " ;";

        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                data.put("name", cursor.getString(0));
                data.put("wins", cursor.getString(1));
                data.put("losses", cursor.getString(2));
                data.put("ties", cursor.getString(3));
            }
        } finally {
            cursor.close();
        }

        closeDB();

        return data;
    }
}
