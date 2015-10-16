package main.java;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ant Colony Optimization
 */
public class ACO {
    // Variables.
    private static final String difficulty = "medium";
    public static final int amountOfAnts = 100;
    public static final List<Coordinate> goalCoordinates = Coordinate.readGoalCoordinates("src/main/resources/" + difficulty + "_coordinates.txt");
    public static List<Coordinate> tspCoordinates = Coordinate.readTspCoordinates("src/main/resources/"+ difficulty +"_tsp_products.txt");
    public static final Coordinate startingCoordinate = goalCoordinates.get(0);
    public static final Coordinate goalCoordinate = goalCoordinates.get(1);
    public static final double pheromoneDropRate = 1000D;
    public static final double evaporationConst = 0.3D;
    public static final double startPheromoneValue = 0.000D;
    public static final double alpha = 1.0D;
    public static final double beta = 3.D;
    private static final boolean guiBoolean = true;


    public static int currentIterations = 0;
    // Coordinate(row, column)

    public static void main(String[] args) {
        Maze maze = new Maze("src/main/resources/"+ difficulty +"_maze.txt");
        List<Ant> antList = new CopyOnWriteArrayList<>();
        Grid grid;
        JFrame window;

        for(int i = 0; i < amountOfAnts; i++) {
            antList.add(new Ant(new Coordinate(startingCoordinate), maze));
        }

        if(guiBoolean) {
            grid = new Grid(maze.getColumns(), maze.getRows());
            window = new JFrame();
            window.setTitle("Ant Colony Optimization");

            // Timer to update the view.
            Timer t1 = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(Ant ant : antList) {
                        Point p = new Point(ant.getCurrentPos().getColumn(), ant.getCurrentPos().getRow());
                        grid.addAnt(p);
                    }
                    grid.addPheromone(maze.getPheromonedRoute());
                    grid.repaint();
                }
            });
            t1.start();
        }

        // Thread to move the ants.
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                while(true) {
                    for(Ant ant : antList) {
                        if(!ant.isGoalReached()) {
                            ant.move();
                        }
                    }
                    if(allGoalsReached(antList) || numberGoalsReached(antList) > 0.1d) {
                        System.out.println("Finished iteration: " + (++currentIterations));
                        antList.clear();
                        maze.evaporatePheromone();
                        for(int i = 0; i < amountOfAnts; i++) {
                            antList.add(new Ant(new Coordinate(startingCoordinate), maze));
                        }
                    }
                }
            }
        });
        thread.start();

        if(guiBoolean) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    }
                               window.setSize(maze.getColumns() * Grid.cellSize + Grid.cellSize * 3, maze.getRows() * Grid.cellSize + Grid.cellSize * 3);
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

    /**
     * True if all ants in the list have
     * reached their goal.
     * @param antList list of ants.
     * @return true if all ants reached their goal.
     */
    private static boolean allGoalsReached(List<Ant> antList) {
        for(Ant ant : antList) {
            if(!ant.isGoalReached())
                return false;
        }
        return true;
    }

    /**
     * Returns the number of ants
     * that reached their goal.
     * @param antList list of ants.
     * @return number of ants.
     */
    private static double numberGoalsReached(List<Ant> antList) {
        double reached = 0.d;
        for(Ant ant : antList) {
            if(ant.isGoalReached())
                reached = reached + 1.d;
        }
        return (reached / ACO.amountOfAnts);
    }
}
