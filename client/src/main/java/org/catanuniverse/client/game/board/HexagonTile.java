/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import javax.swing.JPanel;
import org.catanuniverse.core.game.GroundType;
import org.catanuniverse.core.game.Hextile;

class HexagonTile extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;

    private final Hexagon hexagon;
    private FontMetrics metrics;
    private BiPredicate<Hextile, Integer> onAddSettlement, onAddRoad;

    public HexagonTile(int x, int y, int radius, Hextile tile) {
        final double r3 = Math.sqrt(3);
        this.setOpaque(false);
        this.hexagon = new Hexagon((int) Math.ceil(radius * r3 / 2), radius, radius, tile);
        this.setBounds(
                (int) Math.ceil(x - radius * r3 / 2),
                y - radius,
                (int) Math.ceil(radius * r3),
                2 * radius);
        this.addMouseListener(this);
    }

    public void setOnAddSettlementCallback(BiPredicate<Hextile,Integer> onAddSettlement) {
        this.onAddSettlement = onAddSettlement;
    }

    public void setOnAddRoadCallback(BiPredicate<Hextile, Integer> onAddRoad) {
        this.onAddRoad = onAddRoad;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        Integer cornerIndex = this.hexagon.getCornerFromPoint(e.getPoint()),
            sideIndex = this.hexagon.getSideFromPoint(e.getPoint());
        boolean needUpdate =
            cornerIndex != null && this.onAddSettlement != null && this.onAddSettlement
                .test(this.hexagon.getHextile(), cornerIndex);

        if (sideIndex != null && this.onAddRoad != null && this.onAddRoad.test(this.hexagon.getHextile(), sideIndex)) {
            needUpdate = true;
        }

        if (needUpdate) {
            this.revalidate();
            this.repaint();
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
