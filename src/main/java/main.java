package main.java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hung on 6-10-15.
 */
public class main {

    public static void main(String[] args) {
        Matrix matrix = new Matrix("src/main/resources/easy_maze.txt");
        System.out.println(matrix.toString());
        Ant ant = new Ant(new Coordinates(3, 4), matrix);
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ant.isVertexReached()) {
                    ant.move();
                }
            }
        });
        timer.setRepeats(true);
        timer.start();
        while(true) {
            if(ant.isVertexReached()) {
                timer.stop();
                break;
            }
        }
    }

}
