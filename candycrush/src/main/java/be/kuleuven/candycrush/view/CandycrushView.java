package be.kuleuven.candycrush.view;

import be.kuleuven.candycrush.model.Candy;
import be.kuleuven.candycrush.model.CandycrushModel;
import be.kuleuven.candycrush.model.Position;
import be.kuleuven.candycrush.model.*;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.Iterator;

public class CandycrushView extends Region {
    private CandycrushModel model;
    private int widthCandy;
    private int heigthCandy;

    public CandycrushView(CandycrushModel model) {
        this.model = model;
        widthCandy = 30;
        heigthCandy = 30;
        //update();
    }

    public void update(){
        getChildren().clear();
        for(Position position : model.getBoard().positions()) {
            Node node = makeShapeCandy(position, model.getCandyFromPosition(position));
            getChildren().addAll(node);
            }
        }


    public Node makeShapeCandy(Position position, Candy candy) {
        if (candy instanceof normalCandy) {
            Circle circle = new Circle((position.kolom()+0.5) * widthCandy, (position.rij()+0.5) * heigthCandy, widthCandy/2);
            switch (((normalCandy) candy).color()) {
                case 0:
                    circle.setFill(Color.GREEN);
                    break;
                case 1:
                    circle.setFill(Color.BLUE);
                    break;
                case 2:
                    circle.setFill(Color.YELLOW);
                    break;
                case 3:
                    circle.setFill(Color.RED);
            }
            return circle;
        }
        else{
            Rectangle rectangle = new Rectangle(position.kolom() * widthCandy, position.rij() * heigthCandy, widthCandy, heigthCandy);
            switch (candy.getClass().getSimpleName() ){
                case "drop":
                    rectangle.setFill(Color.GREEN);
                    break;
                case "kauwgom":
                    rectangle.setFill(Color.BLUE);
                    break;
                case "lolly":
                    rectangle.setFill(Color.YELLOW);
                    break;
                case "noga":
                    rectangle.setFill(Color.RED);

            }
            return rectangle;
        }
    }




    public Position getIndexOfClicked(MouseEvent me){
        int index = -1;
        int row = (int) me.getY()/heigthCandy;
        int column = (int) me.getX()/widthCandy;
        System.out.println(me.getX()+" - "+me.getY()+" - "+row+" - "+column);
        Position position= new Position(column,row,model.getBoard());
        return position;
    }
}

