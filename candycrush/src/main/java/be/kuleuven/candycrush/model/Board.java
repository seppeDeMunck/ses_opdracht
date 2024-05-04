package be.kuleuven.candycrush.model;

import javafx.print.PrinterJob;

import java.util.ArrayList;
import java.util.function.Function;

public class Board <T> {
    ArrayList<T> list;
    BoardSize boardSize;
    Function<Position , T> CellCreator;
    public Board(BoardSize boardSize, Function<Position , T> CellCreator){
        this.CellCreator=CellCreator;
        this.boardSize=boardSize;
        int grotelijst=boardSize.kolommen()*boardSize.rijen();
        list=new ArrayList<T>(grotelijst);
        for (int i = 0; i < grotelijst; i++) {
            list.add(null);
        }
    }

    public T getCellAt(Position position){
        try{ return list.get(position.toIndex());}
        catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    public void replaceCellAt(Position position, T newCell){
        try{list.set(position.toIndex() , newCell);}
        catch (Exception e){System.out.print(e);}
    }
    public void fill(){
        for(int i = 0 ; i< boardSize.rijen();i++){
            for(int j = 0; j <boardSize.kolommen();j++){
                T item=CellCreator.apply(new Position(i,j,boardSize));
                list.set(j + (boardSize.kolommen()*i),item );
            }
        }
    }
    public void copyTo(Board<T> otherBoard){
        if (otherBoard.boardSize.rijen()!=this.boardSize.rijen()||
                otherBoard.boardSize.kolommen()!=this.boardSize.kolommen()){
            throw new IllegalArgumentException("wrong size board");
        }
        otherBoard.list=new ArrayList<>(this.list);
    }


}
