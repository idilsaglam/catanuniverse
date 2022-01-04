/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import org.catanuniverse.core.game.GroundType;
import org.catanuniverse.core.game.Hextile;

public class Hexagon extends Polygon {

    private static final long serialVersionUID = 1L;
    private static final Color ROAD_COLOR = new Color(0x19DCCD);
    private static final Color SETTLEMENT_COLOR = new Color(0x000000);
    private static final int SETTLEMENT_CIRCLE_RADIUS = 10;
    private static final int SIDES = 6;

    private Point[] points = new Point[SIDES];
    private Point center = new Point(0, 0);
    private int radius;
    private int rotation = 90;
    private final Hextile hextile;

    public Hexagon(Point center, int radius, Hextile hextile) {
        this.hextile = hextile;
        npoints = SIDES;
        xpoints = new int[SIDES];
        ypoints = new int[SIDES];

        this.center = center;
        this.radius = radius;

        updatePoints();
    }

    public Hexagon(int x, int y, int radius, Hextile hextile) {
        this(new Point(x, y), radius, hextile);
    }

    public Hexagon(Point center, int radius, int id, GroundType groundType) {
        this(center, radius, new Hextile(id, groundType));
    }

    public Hexagon(int x, int y, int radius, int id, GroundType groundType) {
        this(new Point(x, y), radius, new Hextile(id, groundType));
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        updatePoints();
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
        updatePoints();
    }

    public void setCenter(Point center) {
        this.center = center;
        updatePoints();
    }

    public void setCenter(int x, int y) {
        setCenter(new Point(x, y));
    }

    Point getCorner(int index) {
        return switch (index % Hexagon.SIDES) {
            case 0 -> new Point(
                    this.center.x - this.radius, this.center.y - (int) (this.radius / 2));
            case 1 -> new Point(this.center.x, this.center.y - this.radius);
            case 2 -> new Point(
                    this.center.x + this.radius, this.center.y - (int) (this.radius / 2));
            case 3 -> new Point(
                    this.center.x + this.radius, this.center.y + (int) (this.radius / 2));
            case 4 -> new Point(this.center.x, this.center.y + this.radius);
            default -> new Point(
                    this.center.x - this.radius, this.center.y + (int) (this.radius / 2));
        };
    }

    Hextile getHextile() {
        return this.hextile;
    }

    void draw(Graphics2D g, int lineThickness, int colorValue, boolean filled) {
        // Store before changing.
        Stroke tmpS = g.getStroke();
        Color tmpC = g.getColor();

        g.setColor(new Color(colorValue));
        g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        if (filled) g.fillPolygon(xpoints, ypoints, npoints);
        else g.drawPolygon(xpoints, ypoints, npoints);

        // Set values to previous when done.
        g.setColor(tmpC);
        g.setStroke(tmpS);
    }

    void drawRoads(Graphics2D g) {
        Color c = g.getColor();
        g.setColor(Hexagon.ROAD_COLOR);
        Line2D line;
        for (int i = 0; i < 6; i++) {
            if (this.getHextile().getRoadSlot(i) == null) {
                continue;
            }
            line = new Line2D.Float(this.getCorner((i - 1 + 6) % 6), this.getCorner(i));
            g.draw(line);
        }
        g.setColor(c);
    }

    void drawSettlements(Graphics2D g) {
        Color c = g.getColor();
        g.setColor(Hexagon.SETTLEMENT_COLOR);
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < 6; i++) {
            if (this.getHextile().getSettlementSlot(i) == null) {
                // continue;
            }
            this.drawSettlementCircle(g2d, this.getCorner(i));
        }
        g.setColor(c);
    }

    private void drawSettlementCircle(Graphics2D g2d, Point point) {
        Ellipse2D.Double circle =
                new Ellipse2D.Double(
                        point.x - (int) (Hexagon.SETTLEMENT_CIRCLE_RADIUS / 2),
                        point.y - (int) (Hexagon.SETTLEMENT_CIRCLE_RADIUS / 2),
                        Hexagon.SETTLEMENT_CIRCLE_RADIUS,
                        Hexagon.SETTLEMENT_CIRCLE_RADIUS);
        g2d.fill(circle);
    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
    }

    private Point findPoint(double angle) {
        int x = (int) (center.x + Math.cos(angle) * radius);
        int y = (int) (center.y + Math.sin(angle) * radius);
        return new Point(x, y);
    }

    protected void updatePoints() {
        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            Point point = findPoint(angle);
            xpoints[p] = point.x;
            ypoints[p] = point.y;
            points[p] = point;
        }
    }
}
