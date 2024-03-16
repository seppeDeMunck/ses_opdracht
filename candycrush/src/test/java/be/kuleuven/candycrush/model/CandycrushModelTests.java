package be.kuleuven.candycrush.model;

import be.kuleuven.CheckNeighboursInGrid;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

public class CandycrushModelTests {

    @Test                                                               //1
    public void set_width(){
        CandycrushModel model = new CandycrushModel("seppe");
        model.setWidth(5);
        int breedte = model.getWidth();
        assert (breedte==5);
    }

    @Test                                                                  //2
    public void set_height(){
        CandycrushModel model = new CandycrushModel("seppe");
        model.setHeight(5);
        int hoogte = model.getHeight();
        assert (hoogte==5);
    }

    @Test                                                                   //3
    public void test_get_neigbers_hoeveelheid(){
        ArrayList<Integer> arraylijstje = new ArrayList();
        for (int i = 0; i < 16; i++) {
            arraylijstje.add(0);
        }
        CheckNeighboursInGrid buren = new CheckNeighboursInGrid();
        ArrayList<Integer> uitput = (ArrayList)buren.getSameNeighboursIds(arraylijstje, 4, 4, 5);
        assert (uitput.size()==8);
    }
    @Test                                                                   //4
    public void test_get_neigbers_waardes(){
        ArrayList<Integer> arraylijstje = new ArrayList();
        for (int i = 0; i < 16; i++) {
            arraylijstje.add(i%4);
        }
        CheckNeighboursInGrid buren = new CheckNeighboursInGrid();
        ArrayList<Integer> uitput = (ArrayList)buren.getSameNeighboursIds(arraylijstje, 4, 4, 5);
        assert (Objects.equals(uitput.getFirst(), arraylijstje.get(5)));
    }

    @Test                                                                   //5
    public void test_model_setpunten_en_getpunten(){
        CandycrushModel model= new CandycrushModel("Test");
        model.setPunten(50);
        assert (model.getPunten()==50);
    }
    @Test                                                                   //6
    public void test_set_speler(){
        CandycrushModel model = new CandycrushModel("seppe");
        model.setSpeler("test");
        assert (model.getSpeler().equals("test"));
    }

    @Test                                                                   //7
    public void test_beginscore(){
        CandycrushModel model= new CandycrushModel("Test");
        assert (model.getPunten()==0);

    }
    @Test                                                                   //8
    public void test_begin_grote_bord(){
        CandycrushModel model= new CandycrushModel("Test");
        int oppervlakte = model.getHeight()* model.getWidth();
        assert (oppervlakte==100);

    }
    @Test                                                                   //9
    public void test_of_alle_getal_onder_5_zijn(){
        CandycrushModel model= new CandycrushModel("Test");
        ArrayList<Integer> oppervlakte = model.getSpeelbord();
        int maxscore=0;
        for (int i = 0; i < oppervlakte.size(); i++) {
            if (oppervlakte.get(i)>maxscore){
                maxscore=oppervlakte.get(i);

            }
        }
        assert (maxscore>=5);

    }
    @Test
    public void test_of_alle_getal_boven_1_zijn(){
        CandycrushModel model= new CandycrushModel("Test");
        ArrayList<Integer> oppervlakte = model.getSpeelbord();
        int minscore=0;
        for (int i = 0; i < oppervlakte.size(); i++) {
            if (oppervlakte.get(i)<minscore){
                minscore=oppervlakte.get(i);

            }
        }
        assert (minscore<=1);

    }

}
