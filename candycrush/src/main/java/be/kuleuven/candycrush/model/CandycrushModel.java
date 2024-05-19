package be.kuleuven.candycrush.model;


import javafx.geometry.Pos;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CandycrushModel {
    private String speler;
    private Board board;
    private int punten;
    private BoardSize boardSize;

    private Position selectedPosition = null;


    public CandycrushModel(String speler) {                                 //maakt een lijst aan
        this.speler = speler;
        punten = 0;
        boardSize = new BoardSize(8, 8);
        board = new Board<>(boardSize, this::randumCandy);
        board.fill();
    }

    public CandycrushModel(BoardSize size) {
        board = new Board<>(size);
        speler = null;
        boardSize=size;
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

    public Iterable<Position> getNeighbourPositions(Position position) {
        ArrayList<Position> buren = new ArrayList<Position>();
        buren = (ArrayList<Position>) position.neighborPositions();
        return buren;
    }

    public void candyWithIndexSelected(Position position) {
        maxscore();
        if (board.getCellAt(position) instanceof LegeCandy) {
            return;
        }
        if (selectedPosition == null) {
            selectedPosition = position;
            System.out.println("eerste added");
            return;
        }
        for (Position p : getNeighbourPositions(selectedPosition)) {
            //System.out.println(p);
            if (p.kolom() == position.kolom() && p.rij() == position.rij()) {
                Candy candy = (Candy) board.getCellAt(selectedPosition);
                board.replaceCellAt(selectedPosition, board.getCellAt(position));
                board.replaceCellAt(position, candy);
                System.out.println("switch");
                boolean succesvol = updateBoard();
                System.out.println("succes");
                if (succesvol == true) {
                    selectedPosition = null;
                    return;
                }
                candy = (Candy) board.getCellAt(selectedPosition);
                board.replaceCellAt(selectedPosition, board.getCellAt(position));
                board.replaceCellAt(position, candy);
                selectedPosition = position;
            }
        }
        System.out.println("fail");
        selectedPosition = position;
        updateBoard();
    }

    public boolean firstTwoHaveCandy(Candy candy, Stream<Position> positions) {
        if (candy instanceof LegeCandy) {
            return false;
        }
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

    public void clearMatch(List<Position> m) {
        List<Position> match = new ArrayList<>();
        for (Position p : m) {
            match.add(p);
        }
        if (match.size() > 0) {
            if (!(board.getCellAt(match.get(0)) instanceof LegeCandy)) {
                punten++;
            }
            board.replaceCellAt(match.get(0), new LegeCandy());
            match.remove(0);
            clearMatch(match);
        }
    }

    public void fallDownTo(Position pos) {
        List<Position> lijst = pos.walkUp().collect(Collectors.toList());
        Position prevposition = null;
        for (Position p : lijst) {
            if (prevposition == null) {
                prevposition = p;
                continue;
            }
            if (board.getCellAt(prevposition).equals(new LegeCandy()) && !board.getCellAt(p).equals(new LegeCandy())) {
                board.replaceCellAt(prevposition, board.getCellAt(p));
                board.replaceCellAt(p, new LegeCandy());
                fallDownTo(pos);
                return;
            }
            prevposition = p;
        }
    }

    public boolean updateBoard() {
        Set<List<Position>> setOfLijstvanmatches = findAllMatches();
        int changed = 0;
        for (List<Position> p : setOfLijstvanmatches) {
            if (!(board.getCellAt(p.get(0)) instanceof LegeCandy)) {
                clearMatch(p);
                changed++;
            }
        }
        Position positionlinksonder = new Position(0, boardSize.rijen() - 1, boardSize);
        List<Position> ondersterij = positionlinksonder.walkRight().toList();
        if (changed > 0) {
            for (Position p : ondersterij) {
                fallDownTo(p);
            }
            updateBoard();
        }
        //System.out.println(changed);
        if (changed == 0) {
            return false;
        }
        return true;
    }

    //##########################################BACKTRACKING###################################################


    public boolean matchAfterSwitch(Position p1, Position p2) {
        Candy candy1 = (Candy) board.getCellAt(p1);
        Candy candy2 = (Candy) board.getCellAt(p2);
        board.replaceCellAt(p1, candy2);
        board.replaceCellAt(p2, candy1);
        Set<List<Position>> setOfLijstvanmatches = findAllMatches();
        List<List<Position>> filteredLijst = new ArrayList<>();
        for (List<Position> lijstje:setOfLijstvanmatches){
            if (!(board.getCellAt(lijstje.get(0)) instanceof LegeCandy)){filteredLijst.add(lijstje);};
        }
        //System.out.println(setOfLijstvanmatches.size());
        board.replaceCellAt(p2, candy2);
        board.replaceCellAt(p1, candy1);
        if (filteredLijst.isEmpty()) {return false;}
        return true;
    }

    public boolean wisselSnoepjes(Position p1, Position p2) {
        boolean buren = false;
        for (Position p : getNeighbourPositions(p1)) {
            if (p.kolom() == p2.kolom() && p.rij() == p2.rij()) {
                buren = true;
                break;
            }
        }
        if (buren == false) {
            return false;
        }
        if (board.getCellAt(p1) instanceof LegeCandy) {
            return false;
        }
        if (board.getCellAt(p2) instanceof LegeCandy) {
            return false;
        }
        if (!matchAfterSwitch(p1, p2)) {
            return false;
        }
        Candy candy = (Candy) board.getCellAt(p1);
        board.replaceCellAt(p1, board.getCellAt(p2));
        board.replaceCellAt(p2, candy);
        updateBoard();
        return true;
    }

    public int aantalLegeCandys() {
        int puntjes = 0;
        for (int i = 0; i < boardSize.rijen(); i++) {
            for (int j = 0; j < boardSize.kolommen(); j++) {
                if (board.getCellAt(new Position(j, i, boardSize)) instanceof LegeCandy) {
                    puntjes++;
                }
                ;
            }
        }
        return puntjes;
    }

    public List<List<Position>> mogelijkeSwaps() {                                              //returnt mogelijke swaps
        List<List<Position>> returnlijst = new ArrayList<>();
        for (Position p1 : boardSize.positions()) {
            if (p1.rij()< boardSize.rijen()-1){
                Position p2= new Position(p1.kolom(),p1.rij()+1,boardSize);
                if (matchAfterSwitch(p1, p2)) {
                    List<Position> combinatie = new ArrayList<>();
                    combinatie.add(p1);
                    combinatie.add(p2);
                    returnlijst.add(combinatie);
                }
            }
            if (p1.kolom()< boardSize.kolommen()-1){
                Position p2= new Position(p1.kolom()+1,p1.rij(),boardSize);
                if (matchAfterSwitch(p1, p2)) {
                    List<Position> combinatie = new ArrayList<>();
                    combinatie.add(p1);
                    combinatie.add(p2);
                    returnlijst.add(combinatie);
                }
            }
        }
        return returnlijst;
    }


    public record Solution(List<List<Position>> switches, int score) {
    }

    public int  maxscore() {
        List<List<Position>> zetten = new ArrayList<>();
        Solution solution = maximizeScore( zetten, new Solution(new ArrayList<>(), 0));
        System.out.println("max waarde is :"+ solution.score);
        return solution.score;

    }
    public Solution maximizeScore(List<List<Position>> zetten, Solution solution) {
        List<List<Position>> swaps= mogelijkeSwaps();
        //System.out.println(zetten.size());
        //System.out.println(swaps.size());
        if (swaps.isEmpty()){
            if (aantalLegeCandys()>solution.score){return new Solution(zetten,aantalLegeCandys());}
            if (aantalLegeCandys()<solution.score){return solution;}
            if (solution.switches.size()>zetten.size()){return new Solution(zetten,aantalLegeCandys());}
            return solution;
        }
        for (List<Position> lijstje:swaps){
            if (board.getCellAt(lijstje.get(0)) instanceof LegeCandy){continue;}
            if (board.getCellAt(lijstje.get(1)) instanceof LegeCandy){continue;}
            CandycrushModel newmodel = new CandycrushModel("naam");
            board.copyTo(newmodel.getBoard());
            newmodel.wisselSnoepjes(lijstje.get(0),lijstje.get(1));
            List<List<Position>> niewezetten = new ArrayList<>(zetten);
            niewezetten.add(lijstje);
            newmodel.updateBoard();
            solution=newmodel.maximizeScore(niewezetten,solution);
        }
    return solution;
    }
    public static CandycrushModel createBoardFromString(String configuration) {
        var lines = configuration.toLowerCase().lines().toList();
        BoardSize size = new BoardSize(lines.size(), lines.getFirst().length());
        CandycrushModel model = new CandycrushModel(size); // deze moet je zelf voorzien
        for (int row = 0; row < lines.size(); row++) {
            var line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                Candy candy = characterToCandy(line.charAt(col));
                model.getBoard().replaceCellAt(new Position(col, row, size), candy);
            }
        }
        return model;
    }
    private static Candy characterToCandy(char c) {
        return switch(c) {
            case '.' -> new LegeCandy();
            case 'o' -> new normalCandy(0);
            case '*' -> new normalCandy(1);
            case '#' -> new normalCandy(2);
            case '@' -> new normalCandy(3);
            default -> throw new IllegalArgumentException("Unexpected value: " + c);
        };
    }

}


