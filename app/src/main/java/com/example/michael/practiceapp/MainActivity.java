package com.example.michael.practiceapp;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button solveButton;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        solveButton = (Button) findViewById(R.id.button);
        clearButton = (Button) findViewById(R.id.button2);

        solveButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                solve();
            }
        });

        clearButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                clear();
            }
        });
    }

    private void solve(){
        int[][] input = new int[9][9];
        EditText[][] values = getValues();
        int[][] boxInput = new int[9][9];
        for(int box = 0; box<9; box++){
            for(int i = 0; i<9; i++){
                String number = values[box][i].getText().toString();
                if(!number.equals("")){
                    boxInput[box][i] = Integer.parseInt(number);
                }
                else{
                    boxInput[box][i] = 0;
                }
            }
        }

        for(int box = 0; box<9; box++){
            for(int j = 0; j<9; j++){
                input[(box/3)*3 + j/3][(box%3)*3 + j%3] = boxInput[box][j];
            }
        }

        Puzzle puzzle = new Puzzle(input);
        puzzle.solve();
        ArrayList<Cell> boxValues;
        for(int box = 0; box<9; box++){
            boxValues = puzzle.returnBox(box);
            for(int i=0; i<9; i++){
                String number = "";
                if(!boxValues.get(i).toString().equals("0")){
                    number = boxValues.get(i).toString();
                }
                values[box][i].setText(number);
            }
        }



    }

    private void clear(){
        EditText[][] values = getValues();
        for(int box = 0; box<9; box++){
            for(int i=0; i<9; i++){
                values[box][i].setText("");
            }
        }
    }

    public EditText[][] getValues(){
        GridLayout puzzle = (GridLayout)findViewById(R.id.gridLayout);
        int index = 0;
        EditText[][] result = new EditText[9][9];
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                //get each edit text and place it in the array
                result[i][j] = (EditText)((ViewGroup)((ViewGroup)((ViewGroup)puzzle.getChildAt(i)).getChildAt(0)).getChildAt(j)).getChildAt(0);

            }
        }
        return result;
    }
}
