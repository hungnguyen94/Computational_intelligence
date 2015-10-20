package main.java;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Grid class to gui draw.
 * @author hung
 */
public class Grid extends JPanel {

    private List<Point> fillWalls;
    private List<Point> fillRoute;
    private List<Point> fillAnts;
    private List<Point> fillGoals;
    private List<Point> fillVertex;
    private Map<Point, Color> fillPheromone;
    private int xSize;
    private int ySize;
    public final static int cellSize = 10;
    public final static int antSize = 2;

    private int numberOfClicks;

    public Grid(int xSize, int ySize) {
        fillWalls = new ArrayList<Point>();
        fillRoute = new ArrayList<Point>();
        fillAnts = new ArrayList<Point>();
        fillVertex = new ArrayList<Point>();
        fillGoals = new ArrayList<Point>();
        fillPheromone = new HashMap<Point, Color>();
        this.xSize = xSize;
        this.ySize = ySize;
        for(Coordinate tspCoordinate : ACO.tspCoordinates) {
            Point goalPoint = new Point(tspCoordinate.getColumn(), tspCoordinate.getRow());
            fillGoals.add(goalPoint);
        }
        Point startPoint = new Point(ACO.startingCoordinate.getColumn(), ACO.startingCoordinate.getRow());
        fillGoals.add(startPoint);
        Point endPoint = new Point(ACO.goalCoordinate.getColumn(), ACO.goalCoordinate.getRow());
        fillGoals.add(endPoint);
        numberOfClicks = 0;
        addMouseListener(new mouseHandler());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Point fillCell : fillRoute) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.white);
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }

        for(Map.Entry<Point, Color> pointColorEntry : fillPheromone.entrySet()) {
            int cellX = cellSize + (pointColorEntry.getKey().x * cellSize);
            int cellY = cellSize + (pointColorEntry.getKey().y * cellSize);
            g.setColor(pointColorEntry.getValue());
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }
        fillPheromone.clear();

        for(Point fillCell : fillGoals) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.MAGENTA);
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }

        for (Point fillCell : fillVertex) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.green);
            int smallCellSize = (int)(cellSize*0.2);
            int halfCell = (int) (cellSize * 0.5);
            g.fillOval(cellX + halfCell, cellY + halfCell, smallCellSize, smallCellSize);
        }
        fillVertex.clear();

        for (Point fillCell : fillAnts) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.blue);
            g.fillRect( cellX + (int)((cellSize - antSize )* Math.random()),
                    cellY + (int)((cellSize - antSize )* Math.random()),
                    antSize, antSize);
        }
        fillAnts.clear();

        for (Point fillCell : fillWalls) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.BLACK);
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }

        g.setColor(Color.BLACK);
        g.drawRect(cellSize, cellSize, xSize * cellSize, ySize * cellSize);

        for (int i = cellSize; i <= xSize * cellSize; i += cellSize) {
            g.drawLine(i, cellSize, i, ySize * cellSize + cellSize);
        }

        for (int i = cellSize; i <= ySize * cellSize; i += cellSize) {
            g.drawLine(cellSize, i, xSize * cellSize + cellSize, i);
        }
    }

    public void addWallPoint(Point point) {
        fillWalls.add(point);
    }

    public void addRoutePoint(Point point) {
        fillRoute.add(point);
    }

    public void addWalls(List<Point> wallPoints) {
        fillWalls = wallPoints;
    }

    public void addRoute(List<Point> routePoints) {
        fillRoute = routePoints;
    }

    public void addVertex(List<Point> listVertex) {
        fillVertex = new ArrayList<>(listVertex);
    }

    /**
     * Add (point, color) pairs to the list to be drawn.
     * The color is calculated based on the (pheromone / highestPheromone in the Maze).
     * @param pointDoubleMap
     */
    public void addPheromone(Map<Point, Double> pointDoubleMap) {
        for(Map.Entry<Point, Double> pointDoubleEntry : pointDoubleMap.entrySet()) {
            int colorValue = Math.min((int) (pointDoubleEntry.getValue() * 255), 255);
            Color c = new Color(colorValue, 0, 0, colorValue);
            fillPheromone.put(pointDoubleEntry.getKey(), c);
        }
    }

    public void addAnt(Point p) {
        fillAnts.add(p);
    }


    class mouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = (int)((e.getX()) * (1f / cellSize)) - 1;
            int y = (int)((e.getY()) * (1f / cellSize)) - 1;
            System.out.println(++numberOfClicks + ": " + x + ", " + y + ";");
//            ACO.maze.matrix[y][x] = ACO.maze.matrix[y][x] == 0? 1: 0;
//            if(ACO.maze.matrix[y][x] == 0)
//                addWallPoint(new Point(x, y));
//            else
//                fillWalls.remove(new Point(x, y));
//            ACO.shortestDirections.clear();

            System.out.println(ACO.maze.getVertex(new Coordinate(y, x)));
            System.out.println(ACO.maze.getPheromone(new Coordinate(y, x), Direction.NONE));
        }
    }
}
