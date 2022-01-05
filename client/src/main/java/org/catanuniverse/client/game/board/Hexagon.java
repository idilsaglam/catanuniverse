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
import org.catanuniverse.core.game.Road;
import org.catanuniverse.core.game.Settlement;

class Hexagon extends Polygon {

    private static final long serialVersionUID = 1L;
    private static final int RADIUS_CORNER_CIRCLE = 25;
    private static final int AUTHORIZED_DISTANCE_FOR_CLICK_TO_SIDE = 10;
    // TODO: Add a static variable for harbor color

    private static final Color ROAD_COLOR = new Color(0x19DCCD);
    private static final Color SETTLEMENT_COLOR = new Color(0x000000);
    private static final int SETTLEMENT_CIRCLE_RADIUS = 10;
    private static final int SIDES = 6;
    private static final int CLICKABLE_AREA_WIDTH = 12;
    private Point[] points = new Point[SIDES];
    private Point center = new Point(0, 0);
    private int radius;
    private int rotation = 90;
    private final Hextile hextile;
    private final Ellipse2D[] circles;
    private final Line2D[] sides;

    public Hexagon(Point center, int radius, Hextile hextile) {
        this.hextile = hextile;
        final double r3 = Math.sqrt(3);
        this.circles = new Ellipse2D[Hexagon.SIDES];
        this.sides = new Line2D[Hexagon.SIDES];
        super.xpoints =
                new int[] {
                    (int) Math.ceil(center.x - radius * r3 / 2),
                    center.x,
                    (int) Math.ceil(center.x + radius * r3 / 2),
                    (int) Math.ceil(center.x + radius * r3 / 2),
                    center.x,
                    (int) Math.ceil(center.x - radius * r3 / 2)
                };
        super.ypoints =
                new int[] {
                    (int) Math.ceil(center.y - radius / 2.),
                    center.y - radius,
                    (int) Math.ceil(center.y - radius / 2.),
                    (int) Math.ceil(center.y + radius / 2.),
                    center.y + radius,
                    (int) Math.ceil(center.y + radius / 2.),
                };

        this.center = center;
        this.radius = radius;
        this.npoints = SIDES;
        for (int i = 0; i < Hexagon.SIDES; i++) {
            this.circles[i] = new Ellipse2D.Double(
                super.xpoints[i],
                super.ypoints[i],
                Hexagon.RADIUS_CORNER_CIRCLE,
                Hexagon.RADIUS_CORNER_CIRCLE
            );
            this.sides[i] = new Line2D.Double(
                this.xpoints[i],
                this.ypoints[i],
                this.xpoints[(i+1) % Hexagon.SIDES],
                this.ypoints[(i+1) % Hexagon.SIDES]
            );
        }
        // updatePoints();
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
        index = index % super.npoints;
        return new Point(super.xpoints[index], super.ypoints[index]);
        /*   return switch (index % Hexagon.SIDES) {
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
        };*/
    }

    Hextile getHextile() {
        return this.hextile;
    }

    void draw(Graphics2D g, int colorValue, boolean filled) {
        // Store before changing.
        Stroke tmpS = g.getStroke();
        Color tmpC = g.getColor();
        int lineThickness = filled ? 0 : Hexagon.CLICKABLE_AREA_WIDTH;
        g.setColor(new Color(colorValue));
        g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        System.out.println("HEXAGON DRAW FUNCTION");
        System.out.println(this.center);
        for (int i = 0; i < npoints; i++) {
            System.out.printf("(%d,%d)", xpoints[i], ypoints[i]);
        }
        if (filled) g.fillPolygon(xpoints, ypoints, npoints);
        else g.drawPolygon(xpoints, ypoints, npoints);

        // Set values to previous when done.
        g.setColor(tmpC);
        g.setStroke(tmpS);
    }

    void drawRoads(Graphics2D g) {
        Color c = g.getColor();
        Line2D line;
        Road r;
        for (int i = 0; i < 6; i++) {
            r = this.getHextile().getRoadSlot(i);
            if (r == null) {
                continue;
            }
            g.setColor(r.getOwner().getColor());
            line = new Line2D.Float(this.getCorner((i - 1 + 6) % 6), this.getCorner(i));
            g.draw(line);
        }
        g.setColor(c);
    }

    void drawSettlements(Graphics2D g) {
        Color c = g.getColor();

        Graphics2D g2d = (Graphics2D) g;
        Settlement settlement;
        for (int i = 0; i < 6; i++) {
            settlement = this.getHextile().getSettlementSlot(i);
            if (settlement == null) {
                continue;
            }
            g.setColor(settlement.getOwner().getColor());
            this.drawSettlementCircle(g2d, this.getCorner(i));
        }
        g.setColor(c);
    }

    /**
     * Check if a given point is in a corner
     * @param point The point to check
     * @return The number of the corner slot or null
     */
    Integer getCornerFromPoint(Point point) {
        for (int i = 0; i<Hexagon.SIDES; i++) {
            if (this.circles[i].contains(point.x, point.y)) {
                return i;
            }
        }
        return null;
    }

    Integer getSideFromPoint(Point point) {
        for (int i = 0; i < Hexagon.SIDES; i++) {
            if (this.sides[i].ptLineDist(point) <= Hexagon.AUTHORIZED_DISTANCE_FOR_CLICK_TO_SIDE) {
                return i;
            }
        }
        return null;
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

    Point getCenter() {
        return this.center;
    }
}
