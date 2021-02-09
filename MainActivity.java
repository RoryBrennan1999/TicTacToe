package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    // Private variables for widget contents & scores
    private TextView playerOneScore, playerTwoScore, playerStatus;
    private final Button[] buttons = new Button[9];
    private Button resetGame;

    private int playerOneScoreCount, playerTwoScoreCount, roundCount;

    boolean activePlayer;
    int resourcedID;

    // Keeps track of game's state
    // Player 1 turn => 0
    // Player 2 turn => 1
    // Empty => 2
    int [] gameState = {2,2,2,2,2,2,2,2,2};
    int gameStatePointer;

    int [][] winningPositions = {
            {0,1,2}, {3,4,5}, {6,7,8}, // rows
            {0,3,6}, {1,4,7}, {2,5,8}, // columns
            {0,3,8}, {2,4,6} // diagonals
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize variables
        playerOneScore = (TextView) findViewById(R.id.playerONEScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTWOScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        for(int i = 0; i < buttons.length; i++)
        {
            String buttonID = "btn_" + i;
            resourcedID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourcedID); // Get IDs one by one
            buttons[i].setOnClickListener(this); // Set button state to listening for click
        }

        roundCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;

    }

    @Override
    public void onClick(View v)
    {
       if(!((Button)v).getText().toString().equals("")){
           return; // Check that player button presses don't overlap
       }

       // Take ID of pressed button and modify game state
        String buttonID = v.getResources().getResourceEntryName(v.getId());
       gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1, buttonID.length()));

       // Draw X or O for pressed button depending on player
       if(activePlayer)
       {
           ((Button) v).setText("X");
           ((Button) v).setTextColor(Color.parseColor("#FFC34A"));
           gameState[gameStatePointer] = 0; // Player 1
       } else {
           ((Button) v).setText("O");
           ((Button) v).setTextColor(Color.parseColor("#70FFEA"));
           gameState[gameStatePointer] = 1; // Player 2
       }

       roundCount++;

       // Check for winner or end of round
        if(checkForWinner()) {
            if(activePlayer) {
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this,"Player 1 won!", Toast.LENGTH_SHORT).show();
                playAgain();
            } else {
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this,"Player 2 won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
        } else if(roundCount == 9) {
            playAgain();
            Toast.makeText(this,"Draw!", Toast.LENGTH_SHORT).show();
        } else {
            activePlayer =! activePlayer;
        }

        // Update status message
        if(playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("Player 1 is winning!");
        } else if(playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.setText("Player 2 is winning!");
        } else {
            playerStatus.setText("");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
             }
        });
    }

    // Function to check if a player has won
    public boolean checkForWinner()
    {
        boolean winnerResult = false;

        // Match winning positions to game state
        for(int [] winningPosition : winningPositions) {
            if(gameState[winningPosition[0]] == gameState[winningPosition[1]]
                    && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                        && gameState[winningPosition[0]] != 2) {
                winnerResult = true;
            }
        }
        return winnerResult;
    }

    // Update player score function
    public void updatePlayerScore()
    {
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    // Reset buttons to empty and play again
    public void playAgain()
    {
        roundCount = 0;
        activePlayer = true;
        for(int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }
}