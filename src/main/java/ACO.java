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

    public static final int amountOfAnts = 50;
    public static final Coordinate startingPosition = new Coordinate(0, 0);
    public static final double pheromoneDropRate = 200D;
    public static final double evaporationConst = 0.35D;
    public static final double startPheromoneValue = 1.0D;
    public static final double alpha = 1.0D;
    public static final double beta = 3.D;

    public static void main(String[] args) {
        Maze maze = new Maze("src/main/resources/medium_maze.txt");


        List<Ant> antList = new ArrayList<>();

        for(int i = 0; i < amountOfAnts; i++) {
            antList.add(new Ant(new Coordinate(startingPosition), maze));
        }

        Grid grid = new Grid(maze.getColumns(), maze.getRows());
        JFrame window = new JFrame();

        Timer t1 = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(Ant ant : antList) {
                    Point p = new Point(ant.getCurrentPos().getColumn(), ant.getCurrentPos().getRow());
                    grid.addAnt(p);
                    grid.addPheromone(maze.getPheromonedRoute());
                    grid.repaint();
                }
//                System.out.println(Ant.shortestDirections);
            }
        });
        t1.start();


        Thread thread = new Thread(new Runnable() {
            int antIndex = 0;
            @Override
            public void run() {
                while(true) {
//                Ant ant = antList.get(antIndex);
                    for(Ant ant1 : antList) {
                        if(!ant1.isGoalReached()) {
                            ant1.move();
                        }
                    }
                    if(allGoalsReached(antList)) {
                        antList.clear();
                        maze.evaporatePheromone();
                        for(int i = 0; i < amountOfAnts; i++) {
                            antList.add(new Ant(new Coordinate(startingPosition), maze));
                        }
                    }
                }
            }
        });
        thread.start();

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

    private static boolean allGoalsReached(List<Ant> antList) {
        for(Ant ant : antList) {
            if(!ant.isGoalReached())
                return false;
        }
        return true;
    }
}
