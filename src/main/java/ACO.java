package main.java;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ant Colony Optimization
 */
public class ACO {
    // Maze settings.
    private static final String difficulty = "medium";
    public static final int amountOfAnts = 200;
    public static final int amountOfEliteAnts = 5;
    public static final List<Coordinate> goalCoordinates = Coordinate.readGoalCoordinates("src/main/resources/" + difficulty + "_coordinates.txt");
    public static List<Coordinate> tspCoordinates = Coordinate.readTspCoordinates("src/main/resources/"+ difficulty + "_tsp_products.txt");
    public static final Maze maze = new Maze("src/main/resources/"+ difficulty + "_maze.txt");
    public static final String outputRouteFile = "src/main/resources/" + difficulty + "_route.txt";
    // Ant variables
    public static final double pheromoneDropRate = Math.pow(1000d, 1);
    public static final double evaporationConst = 0.3d;
    public static final double startPheromoneValue = 1.d;
    public static final double alpha = 4.0d;
    public static final double beta = 1.0d;
    // If current ant route length is higher than
    // this multiplied by the shortest route length,
    // stop looking further.
    public static final long stopCriterionRouteLength = 10;
    public static final double minReachedAntPercentage = 0.2d;
    private static final boolean guiBoolean = true;

    private static boolean stopThread = false;
    private static int numberOfThreads = 1;

    public static final Coordinate startingCoordinate = goalCoordinates.get(0);
    public static final Coordinate goalCoordinate = goalCoordinates.get(1);

    public static Stack<Direction> shortestDirections = new Stack<>();
    public static Map<Coordinate, Integer> tspCoordinateNumberMap;

    public static int currentIterations = 0;
    public static List<Ant> allAnts = new CopyOnWriteArrayList<>();
    // Coordinate(row, column)

    public static void main(String[] args) {
        Grid grid;
        JFrame window;
        List<Thread> threadList = new ArrayList<>();
        List<Ant> specialAnts = new CopyOnWriteArrayList<>();

        if(guiBoolean) {
            grid = new Grid(maze.getColumns(), maze.getRows());
            window = new JFrame();
            window.setTitle("Ant Colony Optimization");

            // Timer to update the view.
            Timer t1 = new Timer(0, (e) -> {
                for(Ant ant : allAnts) {
                    Point p = new Point(ant.getCurrentPos().getColumn(), ant.getCurrentPos().getRow());
                    grid.addAnt(p);
                }
                for(Ant specialAnt : specialAnts) {
                    grid.addPoint(new Point(specialAnt.getCurrentPos().getColumn(), specialAnt.getCurrentPos().getRow()), Color.cyan);
                }
                grid.addVertex(maze.getVertexPoints());
                grid.addPheromone(maze.getPheromonedRoute());
                grid.repaint();
            });
            t1.start();
        }

        Runnable runAnts = new Runnable() {
            @Override
            public void run() {
                List<Ant> antList = new ArrayList<>();
                List<Ant> eAntList = new ArrayList<>();
                for(int i = 0; i < amountOfEliteAnts; i++) {
                    eAntList.add(new EliteAnt(new Coordinate(startingCoordinate), maze));
                }
                for(int i = 0; i < amountOfAnts; i++) {
                    antList.add(new Ant(new Coordinate(startingCoordinate), maze));
                }
                specialAnts.addAll(eAntList);
                antList.addAll(eAntList);
                allAnts.addAll(antList);
                while(!stopThread) {
                    for(Ant ant : antList) {
                        if(!ant.isGoalReached())
                            ant.move();
                    }
                }
            }
        };

        Runnable resetAnts = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(300);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(numberGoalsReached(allAnts) > minReachedAntPercentage) {
                        stopThread = true;
                        for(Thread thread : threadList) {
                            if(thread.isAlive())
                                thread.stop();
                        }
                        currentIterations++;
//                        System.out.println("Finished iteration: " + (++currentIterations));
                        allAnts.clear();
                        specialAnts.clear();
                        maze.evaporatePheromone();
                        stopThread = false;
                        for(Thread thread : threadList) {
                            thread = new Thread(runAnts);
                            thread.start();
                        }
                    }
                }
            }
        };


        for(int i = 0; i < numberOfThreads; i++) {
            threadList.add(new Thread(runAnts));
        }

        // Thread to move the ants.
        for(Thread thread : threadList) {
            thread.start();
        }

        Thread resetThread = new Thread(resetAnts);
        resetThread.start();
        if(guiBoolean) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    }

                    window.setSize(maze.getColumns() * Grid.cellSize + Grid.cellSize * 3, maze.getRows() * Grid.cellSize + Grid.cellSize * 4);
                    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    window.add(grid);
                    window.setVisible(true);

                    grid.addRoute(maze.getRoute());
                    grid.addWalls(maze.getWalls());

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
        for(Ant ant : allAnts) {
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
        double reached = 0;
        for(Ant ant : allAnts) {
            if(ant.isGoalReached())
                reached = reached + 1;
        }
        return reached / (allAnts.size() - 1);
    }

    /**
     * Output the route as a sequence of actions using the given syntax.
     * @return Route in string.
     */
    public static String routeToString() {
        String route = ACO.shortestDirections.size() + ";\n";
        route += ACO.startingCoordinate.getColumn() + ", " + ACO.startingCoordinate.getRow() + ";\n";
        for(Direction direction : ACO.shortestDirections) {
            route += direction.getDirectionCode() + ";";
        }
        return route;
    }

    /**
     * Write the route to file.
     */
    public static void writeRoute() {
        try {
            Writer fileWriter = new BufferedWriter(new FileWriter(new File(outputRouteFile)));
            fileWriter.write(ACO.routeToString());
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error writing to file. ");
        }
    }
}
