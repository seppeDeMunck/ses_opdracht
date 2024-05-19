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

    private Position selectedPosition=null;


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

    public Iterable<Position> getNeighbourPositions(Position position) {
        ArrayList<Position> buren = new ArrayList<Position>();
        buren= (ArrayList<Position>) position.neighborPositions();
        return buren;
    }

    public void candyWithIndexSelected(Position position) {
        if(board.getCellAt(position)instanceof LegeCandy){return;}
        if(selectedPosition==null){
            selectedPosition=position;
            System.out.println("eerste added");
            return;
        }
        for (Position p :getNeighbourPositions(selectedPosition)){
            System.out.println(p);
            if(p.kolom()==position.kolom()&&p.rij()==position.rij()){
                Candy candy= (Candy) board.getCellAt(selectedPosition);
                board.replaceCellAt(selectedPosition,board.getCellAt(position));
                board.replaceCellAt(position,candy);
                System.out.println("switch");
                boolean succesvol=updateBoard();
                System.out.println("succes");
                if(succesvol==true){
                    selectedPosition=null;
                    return;
                }
                candy= (Candy) board.getCellAt(selectedPosition);
                board.replaceCellAt(selectedPosition,board.getCellAt(position));
                board.replaceCellAt(position,candy);
                selectedPosition=position;
            }
        }
        System.out.println("fail");
        selectedPosition=position;
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
            if (!(board.getCellAt(match.get(0)) instanceof LegeCandy)){
                punten++;
            }
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
        System.out.println(changed);
        if (changed==0){return false;}
        return true;
    }

    //##########################################BACKTRACKING###################################################


    public boolean updatecurrentBoard(Board<Candy> currentboard){
        Set<List<Position>> setOfLijstvanmatches=findAllMatches();
        int changed=0;
        for(List<Position> p:setOfLijstvanmatches){
            if (!currentboard.getCellAt(p.get(0)).equals(new LegeCandy())){
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
        System.out.println(changed);
        if (changed==0){return false;}
        return true;
    }
    boolean matchAfterSwitch(Position p1, Position p2,Board<Candy> currentboard){
        Candy candy = (Candy) currentboard.getCellAt(p1);
        currentboard.replaceCellAt(p1,currentboard.getCellAt(p2));
        currentboard.replaceCellAt(p2,candy);
        Set<List<Position>> setOfLijstvanmatches=findAllMatches();
        currentboard.replaceCellAt(p2,currentboard.getCellAt(p1));
        currentboard.replaceCellAt(p1,candy);
        if (setOfLijstvanmatches.isEmpty()){
            return false;
        }
        return true;
    }

    boolean wisselSnoepjes(Position p1, Position p2,Board<Candy> currentboard){
        boolean buren=false;
        for (Position p :getNeighbourPositions(p1)){
            if (p.kolom()==p2.kolom()&&p.rij()==p2.rij()){
                buren=true;
                break;}
        }
        if (buren==false){return false;}
        if (currentboard.getCellAt(p1)instanceof LegeCandy){return false;}
        if (currentboard.getCellAt(p2)instanceof LegeCandy){return false;}
        if(!matchAfterSwitch(p1, p2,currentboard)){return false;}
        Candy candy = (Candy) currentboard.getCellAt(p1);
        currentboard.replaceCellAt(p1,currentboard.getCellAt(p2));
        currentboard.replaceCellAt(p2,candy);
        updateBoard();
        return true;
    }

    int aantalLegeCandys(Board<Candy> currentboard){
        int puntjes=0;
        for (int i = 0; i < boardSize.rijen(); i++) {
            for (int j = 0; j < boardSize.kolommen() ; j++) {
                if(currentboard.getCellAt(new Position(j,i,boardSize)) instanceof LegeCandy){puntjes++;};
            }
        }
        return puntjes;
    }
    List<List<Position>> mogelijkeSwaps(Board<Candy> currentboard){                                              //returnt mogelijke swaps
        List<List<Position>> returnlijst= new ArrayList<>();
        for (int i = 0; i < boardSize.kolommen(); i++) {
            for (int j = 0; j < boardSize.rijen(); j++) {                           //checkt alle posities
                Position position1 = new Position(i,j,boardSize);
                if (i< boardSize.kolommen()-1){
                    Position position2 = new Position(i+1,j,boardSize);
                    if (matchAfterSwitch(position1,position2,currentboard)){
                        List<Position> match= new ArrayList<>();
                        match.add(position1);
                        match.add(position2);
                        returnlijst.add(match);
                    }
                }
                if (i< boardSize.rijen()-1){
                    Position position2 = new Position(i,j+1,boardSize);
                    if (matchAfterSwitch(position1,position2,currentboard)){
                        List<Position> match= new ArrayList<>();
                        match.add(position1);
                        match.add(position2);
                        returnlijst.add(match);
                    }
                }
            }
        }
        return returnlijst;
    }

    public record Solution(List<List<Position>> switches, int score){}
    Solution maximizeScore(Board<Candy> currentboard,List<List<Position>> zetten,Solution solution){

        List<List<Position>> mogelijkezetten =mogelijkeSwaps(currentboard);
        if (mogelijkezetten.size()==0){
            if (aantalLegeCandys(currentboard)>solution.score){
                return new Solution(zetten,aantalLegeCandys(currentboard));
            }
            if (aantalLegeCandys(currentboard)==solution.score){
                if (zetten.size()<solution.switches.size()){
                    return new Solution(zetten,aantalLegeCandys(currentboard));
                }
            }
        }
        for (List<Position> mogelijkezet:mogelijkezetten){
            Board<Candy> newBoard =  new Board<>(boardSize, this::randumCandy);
            currentboard.copyTo(newBoard);
            wisselSnoepjes(mogelijkezet.get(0),mogelijkezet.get(1),newBoard);
            zetten.add(mogelijkezet);
            solution= maximizeScore(newBoard,zetten,solution);
        }
    return solution;
    }
}


