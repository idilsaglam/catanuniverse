/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import javax.swing.JPanel;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.exceptions.TileTypeNotSupportedException;
import org.catanuniverse.core.game.Board;
import org.catanuniverse.core.game.GroundType;
import org.catanuniverse.core.game.Hextile;

class GameBoardPane extends JPanel {

    private final static int SIZE = 7;
    private static final long serialVersionUID = 1L;
    private Font font = new Font("Arial", Font.BOLD, 18);

    private final Dimension size;
    private final Board board;
    private FontMetrics metrics;

    public GameBoardPane(Dimension size) {
        this.size = size;
        try {
            this.board = new Board(GameBoardPane.SIZE);
        } catch (NoSuchSlotException|SlotAlreadyTakenException|TileTypeNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error happened while creating the board pane");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Point origin = new Point(size.width / 2, size.height / 2);

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2d.setFont(font);
        metrics = g.getFontMetrics();

        //drawCircle(g2d, origin, 380, true, true, 0x4488FF, 0);
        drawHexGridLoop(g2d, origin, 7, 50, 8);
    }

    private void drawHexGridLoop(Graphics g, Point origin, int size, int radius, int padding) {
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = GameBoardPane.SIZE / 2;
        int counter = 0;
        for (int row = 0; row < GameBoardPane.SIZE; row++) {
            int cols = size - java.lang.Math.abs(row - half);

            for (int column = 0; column < cols; column++) {
                int xLbl = row < half ? column - row : column - half;
                int yLbl = row - half;
                int x = (int) (origin.x + xOff * (column * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);
                drawHex(g, x, y, radius, this.board.get(row, column));
            }
        }
    }

    private void drawHex(Graphics g, int x, int y, int r, Hextile tile) {
        Graphics2D g2d = (Graphics2D) g;
        Hexagon hex = new Hexagon(x, y, r, tile);
        String text = tile.getId() + "";
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();

        hex.draw(g2d, x, y, 0, tile.getGroundType().getColor(), true);
        hex.draw(g2d, x, y, 4, tile.getGroundType().getColor(), false);
        // TODO: Change line color for road
        this.drawRoads(g, x,y,r,tile, new Color(0x19DCCD));
        g.setColor(new Color(0xFFFFFF));

        //TODO: Add road and settlements like that
        if (tile.getGroundType() != GroundType.Water && tile.getGroundType() != GroundType.Desert) {
            g.drawString(text, x - w / 2, y + h / 2);
        }
    }

    /**
     * Draw the roads for the road slots
     * @param g The graphics of the current JPnale
     * @param x The x coordinate of the center of the hexagon
     * @param y The y coordinate of the center of the haxagon
     * @param r The radius of the hexagon
     * @param tile The Hextile model related to the hexagon tile
     * @param lineColor The color of the line for the road
     */
    private void drawRoads(Graphics g, int x, int y, int r, Hextile tile, Color lineColor) {
        g.setColor(lineColor);
        Graphics2D g2d = (Graphics2D) g;
        Line2D line;
        if (tile.getRoadSlot(0) != null) {
            line = new Line2D.Float(x-r, y+(int)(r/2), x-r, y-(int)(r/2));
            g2d.draw(line);
        }
        if (tile.getRoadSlot(1) != null) {
            line = new Line2D.Float(x - r, y - (int) (r / 2), x, y - r);
            g2d.draw(line);
        }
        if (tile.getRoadSlot(2) != null) {
            line = new Line2D.Float(x, y - r, x + r, y - (int) (r / 2));
            g2d.draw(line);
        }
        if (tile.getRoadSlot(3) != null) {
            line = new Line2D.Float(x+r, y-(int)(r/2), x+r, y+(int)(r/2));
            g2d.draw(line);
        }
        if (tile.getRoadSlot(4) != null) {
            line = new Line2D.Float(x + r, y + (int) (r / 2), x, y + r);
            g2d.draw(line);
        }
        if (tile.getRoadSlot(5) != null) {
            line = new Line2D.Float(x, y + r,x-r, y+(int)(r/2));
            g2d.draw(line);
        }
    }


    public void drawCircle(
            Graphics2D g,
            Point origin,
            int radius,
            boolean centered,
            boolean filled,
            int colorValue,
            int lineThickness) {
        // Store before changing.
        Stroke tmpS = g.getStroke();
        Color tmpC = g.getColor();

        g.setColor(new Color(colorValue));
        g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int diameter = radius * 2;
        int x2 = centered ? origin.x - radius : origin.x;
        int y2 = centered ? origin.y - radius : origin.y;

        if (filled) g.fillOval(x2, y2, diameter, diameter);
        else g.drawOval(x2, y2, diameter, diameter);

        // Set values to previous when done.
        g.setColor(tmpC);
        g.setStroke(tmpS);
    }
}
