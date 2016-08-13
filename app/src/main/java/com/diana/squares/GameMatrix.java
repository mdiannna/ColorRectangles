package com.diana.squares;


import android.content.res.Resources;
import android.util.Log;

import java.util.Random;


class GameMatrix {
    private  static String TAG = "--GameMatrix--";
    public int Game[][] = {{0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}};
    int gameRows;
    int gameCols;


    public int findId(String buttonId) {
        String stringId = buttonId.substring(22);
        return Integer.parseInt(stringId);
    }


    public int findIById(int id){
        return (id - 1)/ (gameCols);
    }


    public int findJById(int id){
        return (id - 1) % gameCols;
    }


    public void setGameValue(String buttonId, int value) {
        int i = 0, j = 0;
        int id = findId(buttonId);
        i = findIById(id);
        j = findJById(id);

        Log.v(TAG, "i:" + i + "j:" + j + "Value:" + value);

        Game[i][j] = value;
    }

    public void setGameValue(int i, int j, int value) {
        Log.v(TAG, "i:" + i + "j:" + j + "Value:" + value);
        Game[i][j] = value;
    }


    public int getGameValue(String buttonId) {
        int i = 0, j = 0;
        int id = findId(buttonId);
        i = findIById(id);
        j = findJById(id);
        return Game[i][j];
    }


    public int getGameValue(int i, int j) {
        return Game[i][j];
    }

    public boolean checkSquare(int startI, int startJ){
        int startColor = Game[startI][startJ];
        if(startColor != Game[startI+1][startJ])
            return false;
        if(startColor != Game[startI][startJ+1])
            return false;
        if(startColor != Game[startI+1][startJ+1])
            return false;
        return true;
    }



}