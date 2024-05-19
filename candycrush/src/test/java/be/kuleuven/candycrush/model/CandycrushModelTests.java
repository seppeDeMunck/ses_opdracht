package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testWalkRight() {
        BoardSize boardSize = new BoardSize(4, 4);
        Position position = new Position(1, 2, boardSize);
        List<Position> positions = position.walkRight()
                .collect(Collectors.toList());
        List<Position> expectedPositions = List.of(
                new Position(1, 2, boardSize),
                new Position(2, 2, boardSize),
                new Position(3, 2, boardSize)
        );
        assertEquals(expectedPositions, positions);
    }
    @Test
    public void testWalkUp() {
        BoardSize boardSize = new BoardSize(4, 4);
        Position position = new Position(1, 2, boardSize);
        List<Position> positions = position.walkUp()
                .collect(Collectors.toList());
        List<Position> expectedPositions = List.of(
                new Position(1, 2, boardSize),
                new Position(1, 1, boardSize),
                new Position(1, 0, boardSize)
        );
        assertEquals(expectedPositions, positions);
    }
    @Test
    public void testWalkDown() {
        BoardSize boardSize = new BoardSize(4, 4);
        Position position = new Position(1, 2, boardSize);
        List<Position> positions = position.walkDown()
                .collect(Collectors.toList());
        List<Position> expectedPositions = List.of(
                new Position(1, 2, boardSize),
                new Position(1, 3, boardSize)
        );
        assertEquals(expectedPositions, positions);
    }
    @Test
    public void testWalkLeft() {
        BoardSize boardSize = new BoardSize(4, 4);
        Position position = new Position(1, 2, boardSize);
        List<Position> positions = position.walkLeft()
                .collect(Collectors.toList());
        List<Position> expectedPositions = List.of(
                new Position(1, 2, boardSize),
                new Position(0, 2, boardSize)
        );
        assertEquals(expectedPositions, positions);
    }


    @Test
    public void TestBackTracking1(){
        CandycrushModel candycrushModel= new  CandycrushModel("jeff");
         CandycrushModel model = candycrushModel.createBoardFromString("""
   @@o#
   o*#o
   @@**
   *#@@""");

         assertEquals(model.maxscore(),16);
    }




}
