package main.java;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
    private Map<Point, Color> fillPheromone;
    private int xSize;
    private int ySize;
    private final static int cellSize = 20;
    private final static int antSize = 3;

    public Grid(int xSize, int ySize) {
        fillWalls = new ArrayList<Point>();
        fillRoute = new ArrayList<Point>();
        fillAnts = new ArrayList<Point>();
        fillPheromone = new HashMap<Point, Color>();
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Point fillCell : fillWalls) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.BLACK);
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }
        for (Point fillCell : fillRoute) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.WHITE);
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }
        for(Map.Entry<Point, Color> pointColorEntry : fillPheromone.entrySet()) {
            int cellX = cellSize + (pointColorEntry.getKey().x * cellSize);
            int cellY = cellSize + (pointColorEntry.getKey().y * cellSize);
            g.setColor(pointColorEntry.getValue());
            g.fillRect(cellX, cellY, cellSize, cellSize);
        }
        fillPheromone.clear();

        for (Point fillCell : fillAnts) {
            int cellX = cellSize + (fillCell.x * cellSize);
            int cellY = cellSize + (fillCell.y * cellSize);
            g.setColor(Color.blue);
            g.fillRect( cellX + (int)((cellSize - antSize )* Math.random()),
                    cellY + (int)((cellSize - antSize )* Math.random()),
                    antSize, antSize);
        }
        fillAnts.clear();

        g.setColor(Color.BLACK);
        g.drawRect(cellSize, cellSize, xSize * cellSize, ySize * cellSize);

        for (int i = cellSize; i <= xSize * cellSize; i += cellSize) {
            g.drawLine(i, cellSize, i, ySize * cellSize + cellSize);
        }

        for (int i = cellSize; i <= ySize * cellSize; i += cellSize) {
            g.drawLine(cellSize, i, xSize * cellSize + cellSize, i);
        }
    }

    public void addWall(Point p) {
        fillWalls.add(p);
    }

    public void addRoute(Point p) {
        fillRoute.add(p);
    }

    public void addPheromone(Map<Point, Double> pointDoubleMap) {
        for(Map.Entry<Point, Double> pointDoubleEntry : pointDoubleMap.entrySet()) {
            int colorValue = Math.min((int)(pointDoubleEntry.getValue() * 255), 255);
            Color c = new Color(255, 0, 0, colorValue);
            fillPheromone.put(pointDoubleEntry.getKey(), c);
        }
    }

    public void addPheromone(Point p, Double pheromone) {
        int colorValue = Math.min(pheromone.intValue(), 255);
        Color c = new Color(255, colorValue, colorValue, colorValue);
        fillPheromone.put(p, c);
    }

    public void addAnt(Point p) {
        fillAnts.add(p);
    }
}
