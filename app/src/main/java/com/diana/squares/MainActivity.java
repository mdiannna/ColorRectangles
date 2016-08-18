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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static String TAG = "--MyDEBUG--";
    GameMatrix gameMatrix = new GameMatrix();
    TextView timerTextView;
    Button currentColorButton;
    CountDownTimer timer;

    private Toast finalMessage = null;
    final Context context = this;
    Dialog dialog = null;

    String colors[] = {"#e57373", "#BA68C8", "#F06292"};
    int currentColor = Color.parseColor(colors[0]);
    int currentColorIndex = 0;
    int gameCols = 3;
    int gameRows = 3;
    int score = 0;
    long milliseconds;

    final static long TIME_FOR_GAME = 1*30000;

    final static int GAME_SHOULD_START = 0;
    final static int GAME_IN_PROGRESS = 1;
    final static int GAME_IS_FINISHED = 2;
    int gameStatus = GAME_SHOULD_START;

    final static String GAME_STATUS = "gameStatus";
    final static String SCORE = "score";
    final static String TIME = "time";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(savedInstanceState == null){
            initGame();
            gameStatus = GAME_IN_PROGRESS;
        }
        else {
            gameStatus = savedInstanceState.getInt(GAME_STATUS);
            initGameMatrix();
            initToast();
            if (gameStatus == GAME_IS_FINISHED){
                showFinalMessage();
                initTimer(0);
            }
            else{
                milliseconds = savedInstanceState.getLong(TIME)-1;
                initTimer(milliseconds);
            }


            score = savedInstanceState.getInt(SCORE);

            initScore(score);

        }
        Log.d(TAG, "onCreate: gameStatus-" + gameStatus);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(GAME_STATUS, gameStatus);
        savedInstanceState.putInt(SCORE,score);
        savedInstanceState.putLong(TIME, milliseconds);
//        savedInstanceState.putInt(TIMER,timer_sec);
        //save timer
        // save matrix
        hideFinalMessage();

        super.onSaveInstanceState(savedInstanceState);
    }


    protected void onPause(){
        super.onPause();

        hideFinalMessage();
    }


    protected void onStop(){
        super.onStop();
        hideFinalMessage();
    }


    public void initGame(){
        initGameMatrix();
        initScore();
        initTimer(TIME_FOR_GAME);
        initToast();
        gameStatus = GAME_IN_PROGRESS;
    }


    public void restartGame(View view) {
        hideFinalMessage();
        stopTimer();
        initGame();
        gameStatus = GAME_IN_PROGRESS;
//
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
        Toast.makeText(MainActivity.this, "Game restarted!", Toast.LENGTH_SHORT).show();
    }


    public void initToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.final_message_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        //Should include more diverse message as "Awesome! You score is"
        //"I bet you can do better/"
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Your score is " + score);

        finalMessage = new Toast(getApplicationContext());
        finalMessage.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        finalMessage.setDuration(Toast.LENGTH_LONG);
        finalMessage.setView(layout);
    }

    public void initTimerTextView(){
    timerTextView = (TextView) findViewById( R.id.timer );
}

    public void initTimer(long initialMilliseconds){
       initTimerTextView();

        timer = new CountDownTimer(initialMilliseconds, 1000) {

            public void onTick(long millisUntilFinished) {
                milliseconds = millisUntilFinished;
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
                gameStatus = GAME_IS_FINISHED;
                showFinalMessage();
            }
        }.start();
    }


    public void stopTimer(){
        timer.cancel();
    }



    public void initScore(){
        score = 0;
        changeScore(0);
    }

    public void initScore(int init_score){
        score = 0;
        changeScore(init_score);
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



public void showFinalMessage(){
//    LayoutInflater inflater = getLayoutInflater();
//
//    View layout = inflater.inflate(R.layout.final_message_layout,
//            (ViewGroup) findViewById(R.id.custom_toast_container));
//
//    //Should include more diverse message as "Awesome! You score is"
//    //"I bet you can do better/"
//    TextView text = (TextView) layout.findViewById(R.id.text);
//    text.setText("Your score is " + score);
//
//    finalMessage.setView(layout);
//
//
//    finalMessage.show();

    showScore();

}

    public void hideFinalMessage(){
        //if(finalMessage!=null)
            finalMessage.cancel();

    }

    public void startHowToActivity(View view){
        Intent intent = new Intent(this, HowToActivity.class);
        startActivity(intent);
    }

    public void startGameActivity(View view){
        restartGame(view);
    }


    public void showScore(){
        // custom dialog
       dialog = new Dialog(context);
        dialog.setContentView(R.layout.final_message_layout);
        dialog.setTitle("Title...");

        //Should include more diverse message as "Awesome! You score is"
        //"I bet you can do better/"
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Your score is " + score);


        final Button dialogButton = (Button) dialog.findViewById(R.id.try_again);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restartGame(dialogButton);
                dialog.dismiss();
                gameStatus = GAME_SHOULD_START;

            }
        });



        if(gameStatus == GAME_IS_FINISHED) {
            dialog.show();
            gameStatus = GAME_SHOULD_START;
        }
        else
            dialog.dismiss();
    }


}


