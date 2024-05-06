package be.kuleuven.candycrush.model;

import java.util.ArrayList;
import java.util.stream.Stream;

public record Position(int kolom,int rij, BoardSize bord) {
    public Position {
        if (rij<0|rij>bord.rijen()) throw new IllegalArgumentException("rij ligt buiten het bord");
        if (kolom<0|kolom>bord.kolommen()) throw new IllegalArgumentException("kolom ligt buiten het bord");
    }
    public int toIndex(){
        return kolom + rij*bord.kolommen();
    }

    public static Position fromIndex(int index,BoardSize size){
        int kolom= index % size.kolommen();
        int rij = index/size.rijen();
        return new Position(kolom,rij,size);
    }

    public Iterable<Position> neighborPositions(){
        ArrayList<Position> buren = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                if(kolom+i<Position.this.bord.kolommen()&&kolom+i>=0&&rij+j<Position.this.bord.rijen()&&rij+j>=0){
                    buren.add(new Position(kolom+i,rij+j, bord));
                }
            }
        }
        return buren;
    }

    public Stream<Position> walkLeft(){
        return Stream.iterate(this, pos -> new Position(pos.kolom() - 1, pos.rij(), bord))
                .limit(kolom()+1);
    }
    public Stream<Position> walkRight(){
        return Stream.iterate(this, pos -> new Position(pos.kolom + 1, pos.rij, bord))
                .limit(bord.kolommen() - kolom );
    }
    public Stream<Position> walkUp(){
        return Stream.iterate(this, pos -> new Position(pos.kolom, pos.rij - 1, bord))
            .limit(rij+1);
    }
    public Stream<Position> walkDown(){
        return Stream.iterate(this, pos -> new Position(pos.kolom, pos.rij + 1, bord))
            .limit(bord.rijen() - rij );
    }
}
