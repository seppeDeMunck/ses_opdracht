package be.kuleuven.candycrush.model;

import java.util.ArrayList;

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
                try {
                    buren.add(new Position(kolom+i,rij+j, bord));
                }catch (IllegalArgumentException ignored){
                }
            }
        }
        return buren;
    }

    public boolean isLastColumn(){
        return kolom==bord.kolommen()-1;
    }

}
