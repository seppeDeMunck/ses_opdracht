package be.kuleuven.candycrush.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    private String speler;
    private Board board;
    private int punten;
    private BoardSize boardSize;


    public CandycrushModel(String speler) {                                 //maakt een lijst aan
        this.speler = speler;
        punten = 0;
        boardSize = new BoardSize(10,10);
        board=new Board<>(boardSize,this::randumCandy);
        board.fill();
    }

    private Candy randumCandy(Position position){
        Random random = new Random();
        int randomGetal = random.nextInt(5) + 1;

        Candy candy = switch (randomGetal) {
            case 1 -> new drop();
            case 2 -> new kauwgom();
            case 3 -> new lolly();
            case 4 -> new noga();
            default -> new normalCandy(random.nextInt(5)-1);
        };
        return candy;
    }

    public static void main(String[] args) {
        CandycrushModel model = new CandycrushModel("seppe");

    }

    public String getSpeler() {
        return speler;
    }

    public Board getBoard() {
        return board;
    }

    public int getWidth() {
        return boardSize.kolommen();
    }

    public int getPunten() {
        return punten;
    }

    public int getHeight() {return boardSize.rijen();}

    public void setSpeler(String speler) {
        this.speler = speler;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public Candy getCandyFromPosition(Position position){
        return (Candy) board.getCellAt(position);
    }

    public BoardSize getBoardSize() {
        return boardSize;
    }
    public Iterable<Position> getSameNeighbourPositions(Position position){
        ArrayList<Position> gelijkeburen= new ArrayList<Position>();
        for(Position p:position.neighborPositions()){
            if(getCandyFromPosition(p).equals(getCandyFromPosition(position))){
                gelijkeburen.add(p);}
        }
        gelijkeburen.add(position);
        return gelijkeburen;
    }

    public void candyWithIndexSelected(Position position) {
        for(Position p:getSameNeighbourPositions(position)){
            board.replaceCellAt(p,randumCandy(p));
            punten++;
        }
    }
}


