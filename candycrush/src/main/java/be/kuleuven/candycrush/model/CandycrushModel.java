package be.kuleuven.candycrush.model;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions){
        Position position1 = positions.findFirst().orElse(null);
        positions=positions.skip(1);
        Position position2 = positions.findFirst().orElse(null);
        if (position2==null||position1==null){return false;}
        return board.getCellAt(position1).equals(candy) && board.getCellAt(position2).equals(candy);
    }

    public Stream<Position> horizontalStartingPositions(){
        Position position= new Position(1,1,boardSize);
        Stream<Position> linksePositions = position.walkDown();
        Stream<Position> allePositions=linksePositions.flatMap(Position::walkRight);
        Stream<Position> gefiltderdePositions=allePositions.filter(p -> !firstTwoHaveCandy((Candy)board.getCellAt(p),p.walkLeft()));
        return gefiltderdePositions;
    }

    public Stream<Position> verticalStartingPositions(){
        Position position= new Position(1,1,boardSize);
        Stream<Position> linksePositions = position.walkDown();
        Stream<Position> allePositions=linksePositions.flatMap(Position::walkRight);
        Stream<Position> gefiltderdePositions=allePositions.filter(p -> !firstTwoHaveCandy((Candy)board.getCellAt(p),p.walkUp()));
        return gefiltderdePositions;
    }
    public List<Position> longestMatchToRight(Position pos){
        Stream<Position> posities=pos.walkRight().takeWhile(p -> board.getCellAt(p).equals(board.getCellAt(pos)));
        return posities.toList();
    }

    public List<Position> longestMatchDown(Position pos){
        Stream<Position> posities=pos.walkRight().takeWhile(p -> board.getCellAt(p).equals(board.getCellAt(pos)));
        return posities.toList();
    }

    public Set<List<Position>> findAllMatches(){
        Set<List<Position>> matches = new HashSet<>();

        // Horizontale matches
        Stream<Position> horizontalStartingPositions = horizontalStartingPositions();
        horizontalStartingPositions.forEach(startPos -> {
            List<Position> match = longestMatchToRight(startPos);
            if (match.size() >= 3) {
                matches.add(match);
            }
        });

        // Verticale matches
        Stream<Position> verticalStartingPositions = verticalStartingPositions();
        verticalStartingPositions.forEach(startPos -> {
            List<Position> match = longestMatchDown(startPos);
            if (match.size() >= 3) {
                matches.add(match);
            }
        });

        return matches;
    }
}


