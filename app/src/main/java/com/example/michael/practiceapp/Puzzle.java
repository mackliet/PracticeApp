package com.example.michael.practiceapp; /**
 * Created by Michael on 3/22/2016.
 */

import java.util.ArrayList;
/**
 * This class is meant to represent a sudoku puzzle. Methods used to solve the puzzle
 * are based off techniques described at https://www.kristanix.com/sudokuepic/sudoku-solving-techniques.php
 * @author Michael Mackliet
 *
 */
public class Puzzle {
    private Cell[][] puzzle = new Cell[9][9];

    public Puzzle(int arr[][]){
        if(arr.length != 9 || arr[0].length != 9){
            puzzle = null;
            return;
        }
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                puzzle[i][j]= new Cell(arr[i][j], i, j);
            }
        }
    }
    public boolean isSolved(){
        for(int i=0; i<9;i++){
            for(int j=0;j<9;j++){
                if(puzzle[i][j].isSolved() == false){
                    return false;
                }
            }
        }
        return true;
    }
    public String toString(){
        String result = "";
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                result = result + puzzle[i][j];
                if((j-1)%3 == 1 && j!=8){
                    result += " ";
                }
            }
            result += "\n";
            if((i-1)%3 == 1 && i!=8){
                result += "\n";
            }
        }
        return result;
    }


    public boolean equals(Puzzle other){
        for(int i=0; i<9; i++){
            for(int j=0;j<9;j++){
                if(!puzzle[i][j].equals(other.getPuzzle()[i][j])){
                    return false;
                }
            }
        }
        return true;
    }

    public Cell[][] getPuzzle(){
        return puzzle;
    }

    public void soleCandidateAlgorithm(){
        for(int row = 0;row<9;row++){
            for(int column = 0; column<9;column++){
                for(int i=0; i<9; i++){
                    for(int j=0; j<9; j++){
                        if(puzzle[row][column].isSolved()){
                            break;
                        }
                        boolean sameRow = puzzle[row][column].getRow() == puzzle[i][j].getRow();
                        boolean sameColumn = puzzle[row][column].getColumn() == puzzle[i][j].getColumn();
                        boolean sameBox = puzzle[row][column].getBox() == puzzle[i][j].getBox();
                        if(sameRow || sameColumn || sameBox){
                            puzzle[row][column].setPossibility(puzzle[i][j].getValue(), false);
                        }
                    }
                }
                puzzle[row][column].isSolved();
            }
        }
    }
    public void uniqueCandidateAlgorithm(){
        //Check each row to see if there is a value that can only go in a certain cell
        boolean[][] rowPossibilities = new boolean[9][9];
        boolean[][] row0 = new boolean[9][9];
        for(int row = 0; row<9; row++){
            for(int i=0; i<9;i++){
                rowPossibilities[i]=puzzle[row][i].getPossibleValues();
                if(row==0){
                    row0[i] = puzzle[row][i].getPossibleValues();
                }
            }
            for(int val = 1; val<=9; val++){
                int count = 0;
                int column = 0;
                for(int col = 0; col<9; col++){
                    if(rowPossibilities[col][val-1]){
                        count++;
                        column = col;
                    }
                    if(count>1){
                        break;
                    }
                }
                if(count == 1){
                    puzzle[row][column].setValue(val);
                }
            }
        }

        //Check each column to see if there is a value that can only go in a certain cell
        boolean[][] colPossibilities = new boolean[9][9];
        for(int col = 0; col<9; col++){
            for(int i=0; i<9;i++){
                colPossibilities[i]=puzzle[i][col].getPossibleValues();
            }
            for(int val = 1; val<=9; val++){
                int count = 0;
                int row = 0;
                for(int r = 0; r<9; r++){
                    if(colPossibilities[r][val-1]){
                        count++;
                        row = r;
                    }
                    if(count>1){
                        break;
                    }
                }
                if(count == 1){
                    puzzle[row][col].setValue(val);
                }
            }
        }

        //Check each box to see if there is a value that can only go in a certain cell
        boolean[][] boxPossibilities = new boolean[9][9];
        for(int box = 0; box<9; box++){
            ArrayList<Cell> cellsInBox = new ArrayList<Cell>();
            for(int i = 0; i<9; i++){
                for(int j = 0; j<9; j++){
                    if(puzzle[i][j].getBox() == box){
                        cellsInBox.add(puzzle[i][j]);
                    }
                }
            }
            for(int i=0; i<9;i++){
                boxPossibilities[i]=cellsInBox.get(i).getPossibleValues();
            }
            for(int val = 1; val<=9; val++){
                int count = 0;
                int cellNum = 0;
                for(int i = 0; i<9; i++){
                    if(boxPossibilities[i][val-1]){
                        count++;
                        cellNum = i;
                    }
                    if(count>1){
                        break;
                    }
                }
                if(count == 1){
                    int row = cellsInBox.get(cellNum).getRow();
                    int col = cellsInBox.get(cellNum).getColumn();
                    puzzle[row][col].setValue(val);
                }
            }
        }
        this.soleCandidateAlgorithm();
    }
    public void xWingAlgorithm(){
        //Do Algorithm to eliminate row possibilities
        ArrayList<Cell> candidates1;
        ArrayList<Cell> candidates2;
        for(int row = 0; row<8; row++){
            for(int val = 1; val<=9; val++){
                candidates1 = this.checkRowCandidates(val, row);
                if(candidates1.size() <2){
                    continue;
                }
                for(int row2 = row+1; row2<9; row2++){
                    int col1 = -1;
                    int col2 = -1;
                    candidates2 = this.checkRowCandidates(val, row2);
                    if(candidates2.size()<2){
                        continue;
                    }
                    for(int i=0; i<candidates1.size() ;i++){
                        boolean hasPair = false;
                        for(int j=0; j<candidates2.size();j++){
                            if(candidates2.get(j).getColumn() == candidates1.get(i).getColumn()){
                                hasPair = true;
                            }
                        }
                        if(hasPair){
                            ArrayList<Cell> columnCandidates = this.checkColumnCandidates(val, candidates1.get(i).getColumn());
                            if(columnCandidates.size()>2){
                                continue;
                            }
                            else{
                                if(col1!=-1 && col2 == -1){
                                    col2 = candidates1.get(i).getColumn();
                                    for(int col = 0; col<9; col++){
                                        if(col != col1 && col != col2){
                                            puzzle[row][col].setPossibility(val, false);
                                            puzzle[row2][col].setPossibility(val, false);
                                        }
                                    }
                                }
                                if(col1==-1 && col2 == -1){
                                    col1 = candidates1.get(i).getColumn();
                                }
                            }
                        }
                    }
                }
            }
        }
        //Do xWing algorithm to eliminate column possibilities
        for(int col = 0; col<8; col++){
            for(int val = 1; val<=9; val++){
                candidates1 = this.checkColumnCandidates(val, col);
                if(candidates1.size() <2){
                    continue;
                }
                for(int col2 = col+1; col2<9; col2++){
                    int row1 = -1;
                    int row2 = -1;
                    candidates2 = this.checkColumnCandidates(val, col2);
                    if(candidates2.size()<2){
                        continue;
                    }
                    for(int i=0; i<candidates1.size() ;i++){
                        boolean hasPair = false;
                        for(int j=0; j<candidates2.size();j++){
                            if(candidates2.get(j).getRow() == candidates1.get(i).getRow()){
                                hasPair = true;
                            }
                        }
                        if(hasPair){
                            ArrayList<Cell> rowCandidates = this.checkRowCandidates(val, candidates1.get(i).getRow());
                            if(rowCandidates.size()>2){
                                continue;
                            }
                            else{
                                if(row1!=-1 && row2 == -1){
                                    row2 = candidates1.get(i).getRow();
                                    for(int row = 0; row<9; row++){
                                        if(row != row1 && row != row2){
                                            puzzle[row][col].setPossibility(val, false);
                                            puzzle[row][col2].setPossibility(val, false);
                                        }
                                    }
                                }
                                if(row1==-1 && row2 == -1){
                                    row1 = candidates1.get(i).getRow();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void blockAndColumnOrRowInteraction(){
        //if in a given box the only candidates for a value are in the same row
        //then eliminate all candidates for that value in the same row
        ArrayList<Cell> boxCandidates;
        for(int box = 0; box<9; box++){
            for(int val = 1; val<=9; val++){
                boxCandidates = this.checkBoxCandidates(val, box);
                boolean allInSameRow = true;
                int previousRow = -1;
                if(boxCandidates.size()>0){
                    previousRow = boxCandidates.get(0).getRow();
                }
                else{
                    allInSameRow = false;
                    continue;
                }
                for(int i = 0; i<boxCandidates.size();i++){
                    if(previousRow != boxCandidates.get(i).getRow()){
                        allInSameRow = false;
                    }
                }
                if(allInSameRow){
                    for(int i = 0; i<9; i++){
                        if(puzzle[previousRow][i].getBox() != box){
                            puzzle[previousRow][i].setPossibility(val, false);
                        }
                    }
                }
            }
        }
        //if in a given box the only candidates for a value are in the same column
        //then eliminate all candidates for that value in the same column
        for(int box = 0; box<9; box++){
            for(int val = 1; val<=9; val++){
                boxCandidates = this.checkBoxCandidates(val, box);
                boolean allInSameColumn = true;
                int previousColumn = -1;
                if(boxCandidates.size()>0){
                    previousColumn = boxCandidates.get(0).getColumn();
                }
                else{
                    allInSameColumn = false;
                    continue;
                }
                for(int i = 0; i<boxCandidates.size();i++){
                    if(previousColumn != boxCandidates.get(i).getColumn()){
                        allInSameColumn = false;
                    }
                }
                if(allInSameColumn){
                    for(int i = 0; i<9; i++){
                        if(puzzle[i][previousColumn].getBox() != box){
                            puzzle[i][previousColumn].setPossibility(val, false);
                        }
                    }
                }
            }
        }
    }

    public void blockAndBlockInteraction(){
        int row1 = -1;
        int row2 = -1;
        for(int val = 1; val<=9; val++){
            for(int i = 0; i<3; i++){
                for(int box = 0; box<2; box++){
                    int firstBox = box + 3*i;
                    ArrayList<Cell> boxCandidates1 = this.checkBoxCandidates(val,firstBox);
                    for(int j = box+1; j<3; j++){
                        int secondBox = j + 3*i;
                        ArrayList<Cell> boxCandidates2 = this.checkBoxCandidates(val,secondBox);
                        for(int k = 0; k<boxCandidates1.size(); k++){
                            int currentRow = boxCandidates1.get(k).getRow();
                            if(k==0){
                                row1 = currentRow;
                                row2 = -1;
                            }
                            if(currentRow != row1 && row2 == -1){
                                row2 = currentRow;
                            }
                            if(row2 != -1 && currentRow != row2){
                                row1 = -1;
                                row2 = -1;
                                break;
                            }
                        }
                        boolean sameTwoRows = true;
                        for(int k = 0; k<boxCandidates2.size(); k++){
                            int currentRow = boxCandidates2.get(k).getRow();
                            if(row1 == -1){
                                sameTwoRows = false;
                                break;
                            }
                            if(row2 == -1){
                                row2 = currentRow;
                            }
                            if(currentRow != row1 && currentRow != row2){
                                sameTwoRows = false;
                            }
                        }
                        if(sameTwoRows){
                            for(int k = 0; k<9; k++){
                                Cell current1 = puzzle[row1][k];
                                Cell current2 = puzzle[row2][k];
                                if(current1.getBox()!=firstBox && current1.getBox() != secondBox){
                                    current1.setPossibility(val, false);
                                    current2.setPossibility(val, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        int col1 = -1;
        int col2 = -1;
        for(int val = 1; val<=9; val++){
            for(int i = 0; i<3; i++){
                for(int box = 0; box<2; box++){
                    int firstBox = box*3 + i;
                    ArrayList<Cell> boxCandidates1 = this.checkBoxCandidates(val,firstBox);
                    for(int j = box+1; j<3; j++){
                        int secondBox = j*3 + i;
                        ArrayList<Cell> boxCandidates2 = this.checkBoxCandidates(val,secondBox);
                        for(int k = 0; k<boxCandidates1.size(); k++){
                            int currentColumn = boxCandidates1.get(k).getColumn();
                            if(k==0){
                                col1 = currentColumn;
                                col2 = -1;
                            }
                            if(currentColumn != col1 && col2 == -1){
                                col2 = currentColumn;
                            }
                            if(col2 != -1 && currentColumn != col2){
                                col1 = -1;
                                col2 = -1;
                                break;
                            }
                        }
                        boolean sameTwoColumns = true;
                        for(int k = 0; k<boxCandidates2.size(); k++){
                            int currentColumn = boxCandidates2.get(k).getColumn();
                            if(col1 == -1){
                                sameTwoColumns = false;
                                break;
                            }
                            if(col2 == -1){
                                col2 = currentColumn;
                            }
                            if(currentColumn != col1 && currentColumn != col2){
                                sameTwoColumns = false;
                            }
                        }
                        if(sameTwoColumns){
                            for(int k = 0; k<9; k++){
                                Cell current1 = puzzle[k][col1];
                                Cell current2 = puzzle[k][col2];
                                if(current1.getBox()!=firstBox && current1.getBox() != secondBox){
                                    current1.setPossibility(val, false);
                                    current2.setPossibility(val, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void nakedSubset(){
        for(int row = 0; row<9; row++){
            ArrayList<Cell> currentRow = new ArrayList<Cell>();
            for(int j=0; j<9; j++){
                currentRow.add(puzzle[row][j]);
            }
            for(int i = 0; i<8; i++){
                boolean isNakedSubset = false;
                ArrayList<Cell> nakedSubset = new ArrayList<Cell>();
                int thresholdSize = currentRow.get(i).numberPossible();
                Cell current = currentRow.get(i);
                nakedSubset.add(current);
                for(int j = i+1; j<9; j++){
                    Cell next = currentRow.get(j);
                    if(current.samePossibles(next)){
                        nakedSubset.add(next);
                    }
                    if(nakedSubset.size() == thresholdSize){
                        isNakedSubset = true;
                        break;
                    }
                }
                if(isNakedSubset){
                    boolean[] subsetValues = current.getPossibleValues();
                    for(int j = 0; j<9; j++){
                        if(!current.samePossibles(currentRow.get(j))){
                            for(int k = 0; k<9; k++){
                                if(subsetValues[k]){
                                    puzzle[row][j].setPossibility(k+1, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        for(int col = 0; col<9; col++){
            ArrayList<Cell> currentColumn = new ArrayList<Cell>();
            for(int j=0; j<9; j++){
                currentColumn.add(puzzle[j][col]);
            }
            for(int i = 0; i<8; i++){
                boolean isNakedSubset = false;
                ArrayList<Cell> nakedSubset = new ArrayList<Cell>();
                int thresholdSize = currentColumn.get(i).numberPossible();
                Cell current = currentColumn.get(i);
                nakedSubset.add(current);
                for(int j = i+1; j<9; j++){
                    Cell next = currentColumn.get(j);
                    if(current.samePossibles(next)){
                        nakedSubset.add(next);
                    }
                    if(nakedSubset.size() == thresholdSize){
                        isNakedSubset = true;
                        break;
                    }
                }
                if(isNakedSubset){
                    boolean[] subsetValues = current.getPossibleValues();
                    for(int j = 0; j<9; j++){
                        if(!current.samePossibles(currentColumn.get(j))){
                            for(int k = 0; k<9; k++){
                                if(subsetValues[k]){
                                    puzzle[j][col].setPossibility(k+1, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        for(int box = 0; box<9; box++){
            ArrayList<Cell> currentBox = this.returnBox(box);
            for(int i = 0; i<8; i++){
                boolean isNakedSubset = false;
                ArrayList<Cell> nakedSubset = new ArrayList<Cell>();
                int thresholdSize = currentBox.get(i).numberPossible();
                Cell current = currentBox.get(i);
                nakedSubset.add(current);
                for(int j = i+1; j<9; j++){
                    Cell next = currentBox.get(j);
                    if(current.samePossibles(next)){
                        nakedSubset.add(next);
                    }
                    if(nakedSubset.size() == thresholdSize){
                        isNakedSubset = true;
                        break;
                    }
                }
                if(isNakedSubset){
                    boolean[] subsetValues = current.getPossibleValues();
                    for(int j = 0; j<9; j++){
                        if(!current.samePossibles(currentBox.get(j))){
                            for(int k = 0; k<9; k++){
                                if(subsetValues[k]){
                                    currentBox.get(j).setPossibility(k+1, false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void solve(){
        long elapsed = 0;
        long start = System.currentTimeMillis();
        while(elapsed<300 && !this.isSolved()){
            this.soleCandidateAlgorithm();
            this.uniqueCandidateAlgorithm();
            this.xWingAlgorithm();
            this.blockAndColumnOrRowInteraction();
            this.blockAndBlockInteraction();
            this.nakedSubset();
            elapsed = System.currentTimeMillis() - start;
        }
//		if(!this.isSolved()){
//			int[][] input = new int[9][9];
//			Cell[][] cellInput = this.getPuzzle();
//			for(int i = 0; i<9; i++){
//				for(int j = 0; j<9; j++){
//					input[i][j] = cellInput[i][j].getValue();
//				}
//			}
//			RecursiveSolver.solve(input);
//			for(int i = 0; i<9; i++){
//				for(int j = 0; j<9; j++){
//					puzzle[i][j].setValue(input[i][j]);
//				}
//			}
//		}
    }

    public ArrayList<Cell> checkRowCandidates(int value, int row){
        ArrayList<Cell> cellsWithCandidate = new ArrayList<Cell>();
        for(int j = 0; j<9; j++){
            Cell current = puzzle[row][j];
            if(current.canBe(value)){
                cellsWithCandidate.add(current);
            }
        }
        return cellsWithCandidate;
    }
    public ArrayList<Cell> checkColumnCandidates(int value, int col){
        ArrayList<Cell> cellsWithCandidate = new ArrayList<Cell>();
        for(int i = 0; i<9; i++){
            Cell current = puzzle[i][col];
            if(current.canBe(value)){
                cellsWithCandidate.add(current);
            }
        }
        return cellsWithCandidate;
    }
    public ArrayList<Cell> returnBox(int box){
        if(box>8 || box<0){
            return null;
        }
        ArrayList<Cell> result = new ArrayList<Cell>();
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                if(puzzle[i][j].getBox() == box){
                    result.add(puzzle[i][j]);
                }
            }
        }
        return result;
    }
    public ArrayList<Cell> checkBoxCandidates(int value, int box){
        ArrayList<Cell> cellsWithCandidate = new ArrayList<Cell>();
        for(int i = 0; i<9; i++){
            for(int j = 0; j<9; j++){
                if(puzzle[i][j].getBox() == box){
                    cellsWithCandidate.add(puzzle[i][j]);
                }
            }
        }

        for(int i = cellsWithCandidate.size()-1; i>=0; i--){
            Cell current = cellsWithCandidate.get(i);
            if(!current.canBe(value)){
                cellsWithCandidate.remove(i);
            }
        }
        return cellsWithCandidate;
    }

    public boolean solvedWithoutErrors(){
        for(int val = 1; val<=9; val++){
            for(int row = 0; row<9; row++){
                if(this.checkRowCandidates(val, row).size() != 1){
                    return false;
                }
            }
            for(int col = 0; col<9; col++){
                if(this.checkColumnCandidates(val, col).size() != 1){
                    return false;
                }
            }
            for(int box = 0; box<9; box++){
                if(this.checkBoxCandidates(val, box).size() != 1){
                    return false;
                }
            }
        }
        return true;
    }


}


