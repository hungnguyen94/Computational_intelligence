package main.java;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Ant Colony Optimization
 */
public class ACO {

    public static final int amountOfAnts = 100;
    public static final Coordinate startingPosition = new Coordinate(0, 0);
    public static final double pheromoneDropRate = 200D;
    public static final double evaporationConst = 0.1D;
    public static final double startPheromoneValue = 1.0D;
    public static final double alpha = 1.0D;
    public static final double beta = 0.5D;

    public static void main(String[] args) {
        Maze maze = new Maze("src/main/resources/easy_maze.txt");


        List<Ant> antList = new ArrayList<>();

        for(int i = 0; i < amountOfAnts; i++) {
            antList.add(new Ant(new Coordinate(startingPosition), maze));
        }

        Grid grid = new Grid(maze.getColumns(), maze.getRows());
        JFrame window = new JFrame();

        Timer timer = new Timer(1, new ActionListener() {
            int antIndex = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                Ant ant = antList.get(antIndex);

                for(Ant ant1 : antList) {
                    if(!ant1.isGoalReached())
                        ant1.move();
                    Point p = new Point(ant1.getCurrentPos().getColumn(), ant1.getCurrentPos().getRow());
                    grid.addAnt(p);
                    grid.addPheromone(maze.getPheromonedRoute());
                    grid.repaint();
                }

                if(!ant.isGoalReached()) {
                    ant.move();
                } else {
//                    maze.evaporatePheromone();
                    if(antIndex < amountOfAnts - 1)
                        antIndex++;
                    else {
                        antIndex = 0;
                        for(Ant ant2 : antList) {
                            ant2.setGoalReached(false);
                        }
                    }
                }
            }
        });
        timer.start();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                window.setSize(maze.getColumns() * 20 + 40, maze.getRows() * 20 + 40);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.add(grid);
                window.setVisible(true);
                for(Point p : maze.getRoute()) {
                    grid.addRoute(p);
                }
                for(Point p : maze.getWalls()) {
                    grid.addWall(p);
                }
            }
        });

    }
}
