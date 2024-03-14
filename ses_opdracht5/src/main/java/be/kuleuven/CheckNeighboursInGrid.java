package be.kuleuven;

import java.util.ArrayList;

public class CheckNeighboursInGrid {
    ArrayList<Integer> lijstje=new ArrayList<>();
    ArrayList<Integer> output=new ArrayList<>();

    /**
     * This method takes a 1D Iterable and an element in the array and gives back an iterable containing the indexes of all neighbours with the same value as the specified element
     *@return - Returns a 1D Iterable of ints, the Integers represent the indexes of all neighbours with the same value as the specified element on index 'indexToCheck'.
     *@param grid - This is a 1D Iterable containing all elements of the grid. The elements are integers.
     *@param width - Specifies the width of the grid.
     *@param height - Specifies the height of the grid (extra for checking if 1D grid is complete given the specified width)
     *@param indexToCheck - Specifies the index of the element which neighbours that need to be checked
     */
    public Iterable<Integer> getSameNeighboursIds(Iterable<Integer> grid, int width, int height, int indexToCheck){
        int breedte;
        int hoogte;
        breedte=width;
        hoogte=height;
        for (int x:grid){
            lijstje.add(x);
        }
        if((indexToCheck-breedte-1)>0&& (indexToCheck-breedte-1)<breedte*hoogte-1 && indexToCheck%breedte>0){
            if(lijstje.get(indexToCheck - breedte - 1).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck-breedte-1);
            }
        }
        if((indexToCheck-breedte)>0&& (indexToCheck-breedte)<breedte*hoogte-1){
            if(lijstje.get(indexToCheck - breedte).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck-breedte);
            }
        }
        if((indexToCheck-breedte+1)>0&& (indexToCheck-breedte+1)<breedte*hoogte-1&& indexToCheck%breedte<breedte-1){
            if(lijstje.get(indexToCheck - breedte + 1).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck-breedte+1);
            }
        }
        if((indexToCheck-1)>0&& (indexToCheck-1)<breedte*hoogte-1&& indexToCheck%breedte>0){
            if(lijstje.get(indexToCheck - 1).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck-1);
            }
        }

        if((indexToCheck+1)>0&& (indexToCheck+1)<breedte*hoogte-1&& indexToCheck%breedte<breedte-1){
            if(lijstje.get(indexToCheck + 1).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck+1);
            }
        }
        if((indexToCheck+breedte-1)>0 && (indexToCheck+breedte-1)<breedte*hoogte-1 && indexToCheck%breedte>0){
            if(lijstje.get(indexToCheck + breedte - 1).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck+breedte-1);
            }
        }
        if((indexToCheck+breedte)>0  && (indexToCheck+breedte)<breedte*hoogte-1 ){
            if(lijstje.get(indexToCheck + breedte).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck+breedte);
            }
        }
        if(0<(indexToCheck+breedte+1) && (indexToCheck+breedte+1)<breedte*hoogte-1 && indexToCheck%breedte<breedte-1){
            if(lijstje.get(indexToCheck + breedte + 1).equals(lijstje.get(indexToCheck))){
                output.add(indexToCheck+breedte+1);
            }
        }
        return output;
    }
}
