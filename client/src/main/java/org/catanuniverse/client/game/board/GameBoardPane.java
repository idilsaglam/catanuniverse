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
import javax.swing.JPanel;
import org.catanuniverse.core.game.GroundType;

class GameBoardPane extends JPanel {

    private final Dimension size;

    public GameBoardPane(Dimension size) {
        this.size = size;
    }

    private static final long serialVersionUID = 1L;

    private Font font = new Font("Arial", Font.BOLD, 18);
    FontMetrics metrics;

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
        int half = size / 2;
        int counter = 0;
        for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);

            for (int col = 0; col < cols; col++) {
                int xLbl = row < half ? col - row : col - half;
                int yLbl = row - half;
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);
                if (
                    (row == 0) ||
                    (row == size -1) ||
                    (col == 0) ||
                    (col == cols -1)
                ) {
                    drawHex(g, xLbl, yLbl, x, y, radius, counter++, GroundType.Water, 0x000000, 0xC1F8C0);

                    continue;
                }
                drawHex(g, xLbl, yLbl, x, y, radius, counter++, GroundType.Farm, 0x000000, 0xDC198A);
            }
        }
    }

    private void drawHex(Graphics g, int posX, int posY, int x, int y, int r, int id, GroundType groundType, int borderColor, int color) {
        Graphics2D g2d = (Graphics2D) g;

        Hexagon hex = new Hexagon(x, y, r, id, groundType);
        String text = String.format("%s : %s", coord(posX), coord(posY));
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();

        hex.draw(g2d, x, y, 0, color, true);
        hex.draw(g2d, x, y, 4, borderColor, false);

        g.setColor(new Color(0xFFFFFF));
        g.drawString(text, x - w / 2, y + h / 2);
    }

    private String coord(int value) {
        return (value > 0 ? "+" : "") + Integer.toString(value);
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
