/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.swing.JPanel;
import org.catanuniverse.core.game.GroundType;
import org.catanuniverse.core.game.Hextile;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.utils.TriPredicate;

class HexagonTile extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;

    private final Hexagon hexagon;
    private FontMetrics metrics;
    private BiPredicate<Hextile, Integer> onAddSettlement;
    private TriPredicate<Hextile, Integer, Line2D> onAddRoad;
    private Predicate<Hextile> onRobberMoved;

    public HexagonTile(int x, int y, int radius, Hextile tile) {
        final double r3 = Math.sqrt(3);
        this.setOpaque(false);
        this.hexagon =
                new Hexagon(
                        (int) Math.ceil(radius * r3 / 2),
                        radius,
                        radius,
                        tile,
                        new Point(this.getBounds().x, this.getBounds().y));
        this.setBounds(
                (int) Math.ceil(x - radius * r3 / 2),
                y - radius,
                (int) Math.ceil(radius * r3),
                2 * radius);
        this.addMouseListener(this);
    }

    public void setOnAddSettlementCallback(BiPredicate<Hextile, Integer> onAddSettlement) {
        this.onAddSettlement = onAddSettlement;
    }

    /**
     * Updates the onRobberMoved predicate
     *
     * @param onRobberMoved The nex onRobberMoved predicate function
     */
    public void setOnRobberMoved(Predicate<Hextile> onRobberMoved) {
        this.onRobberMoved = onRobberMoved;
    }

    public void setOnAddRoadCallback(TriPredicate<Hextile, Integer, Line2D> onAddRoad) {
        this.onAddRoad = onAddRoad;
    }

    /**
     * Get the set of coordinates of the player's roads
     *
     * @param player The player who owns the roads
     * @return The set of road's coordinates
     */
    public Set<Point> getPlayerRoadCoordinates(Player player) {
        return this.hexagon.getPlayerRoadCoordinates(player);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        metrics = g.getFontMetrics();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));

        String text = this.hexagon.getHextile().getId() + "";
        int w = this.metrics.stringWidth(text);
        int h = this.metrics.getHeight();

        this.hexagon.draw(g2d, this.hexagon.getHextile().getGroundType().getColor(), true);
        this.hexagon.draw(g2d, super.getBackground().getRGB(), false);
        this.hexagon.drawRoads(g2d);
        this.hexagon.drawSettlements(g2d);
        g2d.setColor(new Color(0x0000));
        if (this.hexagon.getHextile().getGroundType() != GroundType.Water
                && this.hexagon.getHextile().getGroundType() != GroundType.Desert) {
            g.drawString(
                    text, this.hexagon.getCenter().x - w / 2, this.hexagon.getCenter().y + h / 2);
        }
        // this.revalidate();
    }

    /**
     * Checks if clicked in the robber zone
     *
     * @param clickPoint The clicked point
     * @return True if clicked inside the robber ellipse
     */
    private boolean isCenterClick(Point clickPoint) {
        return this.hexagon.getRobberEllipse().contains(clickPoint);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.isCenterClick(e.getPoint())) {
            if (this.onRobberMoved != null && this.onRobberMoved.test(this.hexagon.getHextile())) {
                return;
            }
        }
        Integer cornerIndex = this.hexagon.getCornerFromPoint(e.getPoint()),
                sideIndex = this.hexagon.getSideFromPoint(e.getPoint());
        boolean updated =
                cornerIndex != null
                        && this.onAddSettlement != null
                        && this.onAddSettlement.test(this.hexagon.getHextile(), cornerIndex);
        if (updated) {
            return;
        }
        if (sideIndex != null
                && this.onAddRoad != null
                && this.onAddRoad.test(
                        this.hexagon.getHextile(),
                        sideIndex,
                        this.hexagon.getLineModel(sideIndex))) {
            updated = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
