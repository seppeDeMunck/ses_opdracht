package be.kuleuven.candycrush.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    private String speler;
    private ArrayList<Candy> speelbord;
    private int punten;
    private BoardSize board;


    public CandycrushModel(String speler) {                                 //maakt een lijst aan
        this.speler = speler;
        speelbord = new ArrayList<>();
        punten = 0;
        board = new BoardSize(10,10);

        for (int i = 0; i < board.kolommen() * board.rijen(); i++) {                            //vult array met randum getallen
            Random random = new Random();
            int randomGetal = random.nextInt(5) + 1;
            Position position=new Position(i% board.kolommen(),i/ board.kolommen(),board);
            speelbord.add(randumCandy());
        }
    }

    private Candy randumCandy(){
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
        int i = 1;
        Iterator<Candy> iter = model.getSpeelbord().iterator();
        while (iter.hasNext()) {
            Candy candy = iter.next();
            System.out.print(candy);
            if (i % model.getWidth() == 0) {
                System.out.print("\n");
                i = 0;
            }
            i++;
        }
        System.out.print("\n");


    }

    public String getSpeler() {
        return speler;
    }

    public ArrayList<Candy> getSpeelbord() {
        return speelbord;
    }

    public int getWidth() {
        return board.kolommen();
    }

    public int getPunten() {
        return punten;
    }

    public int getHeight() {return board.rijen();}

    public void setSpeler(String speler) {
        this.speler = speler;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public Candy getCandyFromPosition(Position position){
        return speelbord.get(position.kolom() + board.kolommen() * position.rij());
    }

    public BoardSize getBoard() {
        return board;
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
            speelbord.set(p.toIndex(),randumCandy());
            punten++;
        }
    }
}


