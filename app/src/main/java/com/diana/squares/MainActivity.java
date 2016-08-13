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
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static String TAG = "--MyDEBUG--";
    GameMatrix gameMatrix = new GameMatrix();
    Button currentColorButton;
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
        initGameMatrix();
        initScore();
    }

    public void initScore(){
        score = 0;
        changeScore(0);
    }

    ///Code to be optimized///
    public void initGameMatrix() {
        int randomColorIndex = 0;


        gameMatrix.gameCols = gameCols;
        gameMatrix.gameRows = gameRows;

        Random randomGenerator = new Random();

        Button b1 = (Button) findViewById(R.id.b1);
        Button b2 = (Button) findViewById(R.id.b2);
        Button b3 = (Button) findViewById(R.id.b3);
        Button b4 = (Button) findViewById(R.id.b4);
        Button b5 = (Button) findViewById(R.id.b5);
        Button b6 = (Button) findViewById(R.id.b6);
        Button b7 = (Button) findViewById(R.id.b7);
        Button b8 = (Button) findViewById(R.id.b8);
        Button b9 = (Button) findViewById(R.id.b9);

        randomColorIndex = randomGenerator.nextInt(3);
        b1.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b1), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b2.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b2), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b3.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b3), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b4.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b4), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b5.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b5), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b6.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b6), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b7.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b7), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b8.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b8), randomColorIndex);
        randomColorIndex = randomGenerator.nextInt(3);
        b9.setBackgroundColor(Color.parseColor(colors[randomColorIndex]));
        gameMatrix.setGameValue(findButtonId(b9), randomColorIndex);



        for(int i=1; i<=9; i++){
            int resId = getResources().getIdentifier("b" + i, "id", getPackageName());
            Button b = (Button) findViewById(resId);
            b.setText("");
        }

//        for(int i=0; i<gameRows; i++){
//            for(int j=0; j<gameCols; j++){
//                randomColor = randomGenerator.nextInt(3);
//                gameMatrix.Game[i][j] = randomColor;
//                gameMatrix.setGameValue(i, j, randomColor);
//                int id = i*gameCols + j+1;
//                Button b = (Button) findViewById("");
//            }
//        }
        Log.v(TAG, "Game init");
    }


    public void restartGame(View view) {
        initGameMatrix(); initScore();
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