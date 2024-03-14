package be.kuleuven;
import java.util.ArrayList;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> arraylijstje = new ArrayList<>();
        ArrayList<Integer> uitput=new ArrayList<>();;
        arraylijstje.add(0);
        arraylijstje.add(0);
        arraylijstje.add(1);
        arraylijstje.add(0);
        arraylijstje.add(1);
        arraylijstje.add(1);
        arraylijstje.add(0);
        arraylijstje.add(2);
        arraylijstje.add(2);
        arraylijstje.add(0);
        arraylijstje.add(1);
        arraylijstje.add(3);
        arraylijstje.add(0);
        arraylijstje.add(1);
        arraylijstje.add(1);
        arraylijstje.add(1);
        CheckNeighboursInGrid buren= new CheckNeighboursInGrid();

        uitput   = (ArrayList<Integer>) buren.getSameNeighboursIds(arraylijstje,4,4,5);
    System.out.print(uitput);

    }
}