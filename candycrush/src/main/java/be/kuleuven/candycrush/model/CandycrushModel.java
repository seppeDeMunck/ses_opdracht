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
        boardSize = new BoardSize(10, 10);
        board = new Board<>(boardSize, this::randumCandy);
        board.fill();
    }

    private Candy randumCandy(Position position) {
        Random random = new Random();
        int randomGetal = random.nextInt(5) + 1;

        Candy candy = switch (randomGetal) {
            case 1 -> new drop();
            case 2 -> new kauwgom();
            case 3 -> new lolly();
            case 4 -> new noga();
            default -> new normalCandy(random.nextInt(5) - 1);
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

    public int getHeight() {
        return boardSize.rijen();
    }

    public void setSpeler(String speler) {
        this.speler = speler;
    }

    public void setPunten(int punten) {
        this.punten = punten;
    }

    public Candy getCandyFromPosition(Position position) {
        return (Candy) board.getCellAt(position);
    }

    public BoardSize getBoardSize() {
        return boardSize;
    }

    public Iterable<Position> getSameNeighbourPositions(Position position) {
        ArrayList<Position> gelijkeburen = new ArrayList<Position>();
        for (Position p : position.neighborPositions()) {
            if (getCandyFromPosition(p).equals(getCandyFromPosition(position))) {
                gelijkeburen.add(p);
            }
        }
        gelijkeburen.add(position);
        return gelijkeburen;
    }

    public void candyWithIndexSelected(Position position) {
        if (!board.getCellAt(position).equals(new LegeCandy())){
            board.replaceCellAt(position, randumCandy(position));
        }
        updateBoard();
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions) {
        Candy legecandy= new LegeCandy();
        if(candy.equals(legecandy)){return false;}
        boolean antwoord = positions.limit(2).allMatch(p -> board.getCellAt(p).equals(candy));
        return antwoord;
    }

    public Stream<Position> horizontalStartingPositions() {
        Position position = new Position(0, 0, boardSize);
        Stream<Position> gefiltderdePositions = position.walkDown().flatMap(Position::walkRight)//alleposities
                .filter(p -> !firstTwoHaveCandy((Candy) board.getCellAt(p), p.walkLeft()) || p.kolom() == 0);//filter
        return gefiltderdePositions;
    }

    public Stream<Position> verticalStartingPositions() {
        Position position = new Position(0, 0, boardSize);
        Stream<Position> gefiltderdePositions = position.walkDown().flatMap(Position::walkRight)//dit geet alle posities
                .filter(p -> !firstTwoHaveCandy((Candy) board.getCellAt(p), p.walkUp()) || p.rij() == 0);//dit firltert
        return gefiltderdePositions;
    }

    public List<Position> longestMatchToRight(Position pos) {
        Stream<Position> posities = pos.walkRight().takeWhile(p -> board.getCellAt(p).equals(board.getCellAt(pos)));
        return posities.toList();
    }

    public List<Position> longestMatchDown(Position pos) {
        Stream<Position> posities = pos.walkDown().takeWhile(p -> board.getCellAt(p).equals(board.getCellAt(pos)));
        return posities.toList();
    }

    public Set<List<Position>> findAllMatches() {
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
    public void clearMatch(List<Position> m){
        List<Position> match= new ArrayList<>();
        for (Position p:m){
            match.add(p);
        }
        if(match.size()>0){
            punten++;
            board.replaceCellAt(match.get(0),new LegeCandy());
            match.remove(0);
            clearMatch(match);
        }
    }

    public void fallDownTo(Position pos){
        List<Position>lijst =pos.walkUp().collect(Collectors.toList());
        Position prevposition=null;
        for (Position p:lijst){
            if (prevposition==null){
                prevposition=p;
                continue;
            }
            if (board.getCellAt(prevposition).equals(new LegeCandy())&&!board.getCellAt(p).equals(new LegeCandy())){
                board.replaceCellAt(prevposition,board.getCellAt(p));
                board.replaceCellAt(p,new LegeCandy());
                fallDownTo(pos);
                return;
            }
            prevposition=p;
        }
    }

    public boolean updateBoard(){
        Set<List<Position>> setOfLijstvanmatches=findAllMatches();
        System.out.print(setOfLijstvanmatches+"\n");
        if (setOfLijstvanmatches.size()<1){return false;}
        int changed=0;
        for(List<Position> p:setOfLijstvanmatches){
            if (!board.getCellAt(p.get(0)).equals(new LegeCandy())){
                clearMatch(p);
                changed++;
            }
        }
        Position positionlinksonder = new Position(0,boardSize.rijen()-1,boardSize);
        List<Position> ondersterij= positionlinksonder.walkRight().toList();
        if(changed>0){
            for (Position p:ondersterij){
                fallDownTo(p);
            }
            updateBoard();
        }
        return true;
    }
}


