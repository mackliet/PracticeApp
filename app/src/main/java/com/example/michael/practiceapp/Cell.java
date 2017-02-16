package com.example.michael.practiceapp; /**
 * Created by Michael on 3/22/2016.
 */

public class Cell {
    private int actualValue;
    private boolean[] possibleValues = new boolean[9];
    private int row;
    private int column;
    private int box;

    public Cell(int value, int _row, int _column){
        row = _row;
        column = _column;
        box = this.findBox();
        if(value>=1 && value <=9){
            actualValue = value;
            for(int i = 0; i<9; i++){
                possibleValues[i] = false;
            }
            possibleValues[value-1] = true;
        }
        else{
            actualValue = 0;
            for(int i = 0; i<9; i++){
                possibleValues[i] = true;
            }
        }
    }

    public int findBox(){
        int[][] boxes = new int[3][3];
        for(int i=0; i<3; i++){
            for(int j = 0; j<3; j++){
                boxes[i][j] = j + 3*i;
            }
        }
        int i = row/3;
        int j = column/3;
        return boxes[i][j];
    }

    public boolean isSolved(){
        int count = 0;
        int value = 0;
        for(int i = 0; i<9; i++){
            if(possibleValues[i]){
                count++;
                value = i+1;
            }
            if(count>1){
                return false;
            }
        }
        actualValue = value;
        return true;
    }
    public boolean canBe(int value){
        if(value<1 || value >9){
            return false;
        }
        return possibleValues[value -1];
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }

    public int getBox(){
        return box;
    }
    public int getValue(){
        return actualValue;
    }
    public boolean[] getPossibleValues(){
        return possibleValues;
    }
    public boolean samePossibles(Cell other){
        for(int i = 0; i<9; i++){
            if(this.possibleValues[i] != other.possibleValues[i]){
                return false;
            }
        }
        return true;
    }
    public int numberPossible(){
        int count = 0;
        for(int i = 0; i<9; i++){
            if(possibleValues[i]){
                count++;
            }
        }
        return count;
    }
    public String toString(){
        String result = "" + actualValue;//possibleValues[0];
        return result;
    }
    public boolean equals(Cell other){
        boolean sameValue = actualValue==other.getValue();
        boolean sameRow = row == other.getRow();
        boolean sameColumn = column == other.getColumn();
        boolean samePossibles = true;
        for(int i = 0; i<9; i++){
            if(possibleValues[i]^other.getPossibleValues()[i]){
                samePossibles = false;
            }
        }
        if(sameValue&&sameRow&&sameColumn&&samePossibles){
            return true;
        }
        return false;
    }

    public void setValue(int value){
        actualValue = value;
        for(int i = 0; i<9; i++){
            possibleValues[i] = false;
        }
        possibleValues[value-1] = true;
    }

    public void setPossibility(int value, boolean isOrIsNot){
        if(value <= 0 || value > 9){
            return;
        }
        possibleValues[value-1] = isOrIsNot;
        this.isSolved();
    }

}
