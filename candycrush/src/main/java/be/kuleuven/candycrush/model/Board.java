package be.kuleuven.candycrush.model;

import javafx.geometry.Pos;

import java.util.*;
import java.util.function.Function;


public class Board <T> {
    private Map<Position , T> positionToCellMap = new HashMap<>();

    private Map<T, Set<Position>> cellToPositionsMap = new HashMap<>();
    BoardSize boardSize;
    Function<Position , T> cellCreator;
    public Board(BoardSize boardSize, Function<Position , T> cellCreator){
        this.cellCreator =cellCreator;
        this.boardSize=boardSize;
        for (int row = 0; row < boardSize.rijen(); row++) {
            for (int col = 0; col < boardSize.kolommen(); col++) {
                Position position = new Position(row, col,boardSize);
                T cell = cellCreator.apply(position);
                positionToCellMap.put(position, cell);
                cellToPositionsMap.computeIfAbsent(cell, k -> new HashSet<>()).add(position);
            }
        }
    }

    public Board(BoardSize boardSize) {
        this.boardSize = boardSize;
        positionToCellMap = new HashMap<>();
        cellToPositionsMap = new HashMap<>();
    }

    public T getCellAt(Position position){
        if(positionToCellMap.get(position)==null){
            return (T) new LegeCandy();
        }
        return positionToCellMap.get(position);
    }

    public void replaceCellAt(Position position, T newCell){
        positionToCellMap.put(position, newCell);
    }
    public void fill(){
        for(int i = 0 ; i< boardSize.rijen();i++){
            for(int j = 0; j <boardSize.kolommen();j++){
                Position position=new Position(i,j,boardSize);
                T item= cellCreator.apply(position);
                positionToCellMap.put(position, item);
            }
        }
    }
    public void copyTo(Board<T> otherBoard) {
        if (otherBoard.boardSize.rijen() != this.boardSize.rijen() ||
                otherBoard.boardSize.kolommen() != this.boardSize.kolommen()) {
            throw new IllegalArgumentException("wrong size board");
        }
        otherBoard.positionToCellMap.clear();
        otherBoard.cellToPositionsMap.clear();

        for (Map.Entry<Position, T> entry : positionToCellMap.entrySet()) {
            Position position = entry.getKey();
            T cell = entry.getValue();
            otherBoard.positionToCellMap.put(position, cell);
            otherBoard.cellToPositionsMap.computeIfAbsent(cell, k -> new HashSet<>()).add(position);
        }
    }
    public Set<Position> getPositionsOfElement(T cell){
        Set<Position> positions = cellToPositionsMap.getOrDefault(cell, Collections.emptySet());
        return Collections.unmodifiableSet(new HashSet<>(positions));
    }


}
