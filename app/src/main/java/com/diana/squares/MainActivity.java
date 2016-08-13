/*---------------Button name CONVENTION-----------------
ALL BUTTONS MUST BE NAMED [b + id], ids MUST BE IN ASCENDING ORDER.
Example for 2x2 game:
b1 | b2 |
b3 | b4 |
Example for 3x3 game:
b1 | b2 | b3 |
b4 | b5 | b6 |
b7 | b8 | b9 |
PLEASE FOLLOW THIS CONVENTION IN ORDER FOR GAME TO WORK PROPERLY
------------------------------------------------------*/
package com.diana.squares;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static String TAG = "--MyDEBUG--";
    GameMatrix gameMatrix = new GameMatrix();
    TextView timerTextView;
    Button currentColorButton;
    CountDownTimer timer;

    String colors[] = {"#e57373", "#BA68C8", "#F06292"};
    int currentColor = Color.parseColor(colors[0]);
    int currentColorIndex = 0;
    int gameCols = 3;
    int gameRows = 3;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGame();
    }


    public void initGame(){
        initGameMatrix();
        initScore();
        initTimer();
    }


    public void restartGame(View view) {
        timer.cancel();
        initGame();
    }


    public void initTimer(){
        timerTextView = (TextView) findViewById( R.id.timer );
        timer = new CountDownTimer(1*60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("" +new SimpleDateFormat("mm:ss").format(new Date( millisUntilFinished)));
//                timerTextView.setText(" " +new SimpleDateFormat("mm:ss:SS").format(new Date( millisUntilFinished)));
            }

            public void onFinish() {
                timerTextView.setText("done!");

                for(int i=1; i<=9; i++){
                    int resId = getResources().getIdentifier("b" + i, "id", getPackageName());
                    Button b = (Button) findViewById(resId);
                    b.setClickable(false);
                }
            }
        }.start();



    }

    public void initScore(){
        score = 0;
        changeScore(0);
    }


    public void initGameMatrix() {
        int randomColorIndex = 0;
        
        gameMatrix.gameCols = gameCols;
        gameMatrix.gameRows = gameRows;

        Random randomGenerator = new Random();


        for(int i=1; i<=9; i++){
            int resId = getResources().getIdentifier("b" + i, "id", getPackageName());
            Button b = (Button) findViewById(resId);
            b.setText("");
            b.setClickable(true);
            randomColorIndex = randomGenerator.nextInt(3);
            b.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
            gameMatrix.setGameValue(findButtonId(b), randomColorIndex);
        }

        checkFullSquares();

        Log.v(TAG, "Game init");
    }




    public int generateRandomColorIndex() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(3);
        return index;
    }


    public String findButtonId(Button b) {
        return b.getResources().getResourceName(b.getId());
    }

    public void changeSquareColor(View view) {

        Button b = (Button) view;
        b.setBackgroundColor(currentColor);
        String stringButtonId = findButtonId(b);
        gameMatrix.setGameValue(stringButtonId, currentColorIndex);
        Log.v(TAG, "Id clicked: " + stringButtonId.substring(21));
        Log.v(TAG, "GAme of i, j:" + gameMatrix.getGameValue(stringButtonId));

        checkFullSquares();

        int colorIndex = generateRandomColorIndex();
        currentColorIndex = colorIndex;
        currentColor = Color.parseColor(colors[colorIndex]);


        currentColorButton = (Button) findViewById(R.id.currentColorButton);
        currentColorButton.setBackgroundColor(currentColor);
    }


    public void randomColorSquare(int i, int j){
        int colorIndex = generateRandomColorIndex();
        int id = i*gameRows + j+1;
        Log.v(TAG, "id=" + id);
        int resId = getResources().getIdentifier("b" + id, "id", getPackageName());
        Log.v(TAG, "resId:"+resId);
        Button b = (Button) findViewById(resId);

        gameMatrix.Game[i][j] = colorIndex;
//        b.setBackgroundColor(Color.parseColor("#FFFFFF"));
        b.setBackgroundColor(Color.parseColor(colors[colorIndex]));
    }


    public void changeScore(int points){
        score += points;
        TextView scoreTextView = (TextView) findViewById(R.id.score);
        scoreTextView.setText("Score: " + score);
    }


    public void disappearSquare(int startI, int startJ){
        randomColorSquare(startI, startJ);
        randomColorSquare(startI+1, startJ);
        randomColorSquare(startI, startJ+1);
        randomColorSquare(startI+1, startJ+1);
        changeScore(10);
        checkFullSquares();
    }




    public void checkFullSquares(){
        for(int i=0; i<gameRows -1; i++){
            for(int j=0; j<gameCols - 1; j++){
                if(gameMatrix.checkSquare(i, j))
                    disappearSquare(i, j);
            }
        }
    }




    public void startHowToActivity(View view){
        Intent intent = new Intent(this, HowToActivity.class);
        startActivity(intent);
    }

}


