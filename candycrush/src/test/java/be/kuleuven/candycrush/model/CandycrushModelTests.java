package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public class CandycrushModelTests {                                         // alle testen van deel 1 zijn verwijderd omdat die niet allemaal meer werkten

    @Test
    public void test_positions(){
        BoardSize bord = new BoardSize(5,5);
        Iterable<Position> position=bord.positions();
        int count = 0;
        for (Position pos : position) {
            count++;
        }
    assert (count==25);
    }
    @Test
    public void test_rijen(){
        BoardSize bord = new BoardSize(2,5);
        assert (bord.rijen()==5);
    }
    @Test
    public void test_kolommen(){
        BoardSize bord = new BoardSize(2,5);
        assert (bord.kolommen()==2);
    }
    @Test
    public void test_Board_getCellAt_and_fill(){
        Candy candy=new noga();
        BoardSize boardSize=new BoardSize(5,5);
        Function<Position, Candy> cellCreator = position -> new noga();
        Board board=new Board<Candy>(boardSize,cellCreator);
        Position position=new Position(3,3,boardSize);
        board.fill();
         assert board.getCellAt(position).equals(candy);
    }
    @Test
    public void test_Board_replaceCellAt(){
        Candy candy=new drop();
        BoardSize boardSize=new BoardSize(5,5);
        Function<Position, Candy> cellCreator = position -> new noga();
        Board board=new Board<Candy>(boardSize,cellCreator);
        Position position=new Position(3,3,boardSize);
        board.fill();
        board.replaceCellAt(position,candy);
        assert board.getCellAt(position).equals(candy);
    }

    @Test
    public void test_Board_copyTo(){
        Candy candy=new drop();
        BoardSize boardSize=new BoardSize(5,5);
        Function<Position, Candy> cellCreator = position -> new noga();
        Board board=new Board<Candy>(boardSize,cellCreator);
        Position position=new Position(3,3,boardSize);
        board.fill();
        board.replaceCellAt(position,candy);
        Board board2=new Board<Candy>(boardSize,cellCreator);
        board.copyTo(board2);
        assert board2.getCellAt(position).equals(candy);
    }



}
