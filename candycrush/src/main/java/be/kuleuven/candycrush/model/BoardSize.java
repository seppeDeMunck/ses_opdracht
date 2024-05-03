package be.kuleuven.candycrush.model;

import java.util.ArrayList;

public record BoardSize(int kolommen, int rijen) {
    public BoardSize {
        if (kolommen<=0||rijen<=0) throw new IllegalArgumentException("waarde moet groter zijn dan 0");
    }

    public Iterable<Position> positions(){
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i <= rijen*kolommen - 1; i++) {
            positions.add(Position.fromIndex(i, this));
        }
        return positions;
    }

}
