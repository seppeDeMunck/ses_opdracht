package be.kuleuven.candycrush.model;

import be.kuleuven.CheckNeighboursInGrid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CandycrushModel {
    private String speler;
    private ArrayList<Integer> speelbord;
    private int width;
    private int height;
    private int punten;


    public CandycrushModel(String speler) {                                 //maakt een lijst aan
        this.speler = speler;
        speelbord = new ArrayList<>();
        width = 10;
        height = 10;
        punten = 0;

        for (int i = 0; i < width * height; i++) {                            //vult array met randum getallen
            Random random = new Random();
            int randomGetal = random.nextInt(5) + 1;
            speelbord.add(randomGetal);
        }
    }

    public static void main(String[] args) {
        CandycrushModel model = new CandycrushModel("arne");
        int i = 1;
        Iterator<Integer> iter = model.getSpeelbord().iterator();
        while (iter.hasNext()) {
            int candy = iter.next();
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

    public ArrayList<Integer> getSpeelbord() {
        return speelbord;
    }

    public int getWidth() {
        return width;
    }

    public int getPunten() {
        return punten;
    }

    public int getHeight() {
        return height;
    }

    public void candyWithIndexSelected(int index) {
        if (index != -1) {
            ArrayList<Integer> arraylijstje;
            Random random = new Random();
            CheckNeighboursInGrid check = new CheckNeighboursInGrid();
            arraylijstje = (ArrayList<Integer>) check.getSameNeighboursIds(speelbord, width, height, index);
            arraylijstje.add(index);
            System.out.print(arraylijstje);
            for (int i = 0; i < arraylijstje.size(); i++) {
                int randomGetal = random.nextInt(5) + 1;
                speelbord.set(arraylijstje.get(i), randomGetal);
                punten++;
            }
        } else {
            System.out.println("model:candyWithIndexSelected:indexWasMinusOne");
        }
    }
    public void ingelogd(){



    }

    public int getIndexFromRowColumn(int row, int column) {
        return column + row * width;
    }


}


