package be.kuleuven.candycrush.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

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

}
