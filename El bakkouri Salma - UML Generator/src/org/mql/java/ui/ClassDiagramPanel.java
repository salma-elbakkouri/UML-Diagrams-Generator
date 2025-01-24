package org.mql.java.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import org.mql.java.models.ClassModel;
import org.mql.java.models.ProjectModel;
import org.mql.java.models.RelationModel;
import org.mql.java.enums.RelationType;

public class ClassDiagramPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ProjectModel project;
	private Map<String, Rectangle> classRectangles;
	private Set<String> drawnRelationships;
	private Rectangle selectedRect;
	private Point offset;

	public ClassDiagramPanel(ProjectModel project) {
		this.project = project;
		this.classRectangles = new HashMap<>();
		this.drawnRelationships = new HashSet<>();
		this.selectedRect = null;
		this.offset = new Point();
		setBackground(Color.WHITE);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				for (Map.Entry<String, Rectangle> entry : classRectangles.entrySet()) {
					if (entry.getValue().contains(e.getPoint())) {
						selectedRect = entry.getValue();
						offset.setLocation(e.getX() - selectedRect.x, e.getY() - selectedRect.y);
						break;
					}
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				selectedRect = null;
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (selectedRect != null) {
					selectedRect.setLocation(e.getX() - offset.x, e.getY() - offset.y);
					repaint();
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (project == null)
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int x = 50, y = 50;
		int maxHeight = 0;

		for (ClassModel cls : project.getPackages().get(0).getClasses()) {
			Rectangle rect = classRectangles.get(cls.getName());
			if (rect == null) {
				rect = drawClass(g2, cls, x, y);
				classRectangles.put(cls.getName(), rect);
			} else {
				drawClass(g2, cls, rect.x, rect.y);
			}
			x += rect.width + 50;
			maxHeight = Math.max(maxHeight, rect.height);

			if (x + rect.width > getWidth()) {
				x = 50;
				y += maxHeight + 50;
				maxHeight = 0;
			}
		}

		drawnRelationships.clear();

		for (ClassModel cls : project.getPackages().get(0).getClasses()) {
			for (RelationModel relationship : cls.getRelationships()) {
				drawRelationship(g2, relationship);
			}
		}

		setPreferredSize(new Dimension(getWidth(), y + maxHeight + 100));
		revalidate();
	}

	private Rectangle drawClass(Graphics2D g2, ClassModel cls, int x, int y) {
		FontMetrics metrics = g2.getFontMetrics();
		int padding = 10;
		int lineHeight = metrics.getHeight();
		int width = 180;

		int height = padding * 2 + lineHeight;
		if (!cls.getFields().isEmpty()) {
			height += padding * 2 + lineHeight * cls.getFields().size();
		}
		if (!cls.getMethods().isEmpty()) {
			height += padding * 2 + lineHeight * cls.getMethods().size();
		}

		g2.setColor(new Color(0x8CD4D2));
		g2.fillRect(x, y, width, lineHeight + padding * 2);
		g2.setColor(Color.BLACK);
		g2.drawRect(x, y, width, lineHeight + padding * 2);

		Font originalFont = g2.getFont();
		g2.setFont(new Font(originalFont.getName(), Font.BOLD, 14));
		int textY = y + padding + (lineHeight + padding) / 2;
		g2.drawString(cls.getName(), x + padding, textY);
		g2.setFont(originalFont);

		g2.drawLine(x, y + lineHeight + padding * 2, x + width, y + lineHeight + padding * 2);

		textY = y + lineHeight + padding * 2;
		if (!cls.getFields().isEmpty()) {
			g2.setColor(new Color(0xDCFEFD));
			g2.fillRect(x, textY, width, padding * 2 + lineHeight * cls.getFields().size());
			g2.setColor(Color.BLACK);
			g2.drawRect(x, textY, width, padding * 2 + lineHeight * cls.getFields().size());

			textY += padding;
			for (var field : cls.getFields()) {
				g2.drawString("- " + field.getName() + ": " + field.getType(), x + padding, textY + lineHeight);
				textY += lineHeight;
			}
			textY += padding;
			g2.drawLine(x, textY, x + width, textY);
		}

		if (!cls.getMethods().isEmpty()) {
			g2.setColor(new Color(0xDCFEFD));
			g2.fillRect(x, textY, width, padding * 2 + lineHeight * cls.getMethods().size());
			g2.setColor(Color.BLACK);
			g2.drawRect(x, textY, width, padding * 2 + lineHeight * cls.getMethods().size());

			textY += padding;
			for (var method : cls.getMethods()) {
				g2.drawString("+ " + method.getName() + "(): " + method.getReturnType(), x + padding,
						textY + lineHeight);
				textY += lineHeight;
			}
			textY += padding;
			g2.drawLine(x, textY, x + width, textY);
		}

		return new Rectangle(x, y, width, height);
	}

	private void drawRelationship(Graphics2D g2, RelationModel relationship) {
		if (relationship.getSourceClass().equals(relationship.getTargetClass())) {
			return;
		}
		String relationshipKey = relationship.getSourceClass().compareTo(relationship.getTargetClass()) < 0
				? relationship.getSourceClass() + "->" + relationship.getTargetClass()
				: relationship.getTargetClass() + "->" + relationship.getSourceClass();

		if (drawnRelationships.contains(relationshipKey)) {
			return;
		}

		Rectangle fromRect = classRectangles.get(relationship.getSourceClass());
		Rectangle toRect = classRectangles.get(relationship.getTargetClass());

		if (fromRect == null || toRect == null) {
			return;
		}

		Point fromPoint = getNearestPoint(fromRect, toRect);
		Point toPoint = getNearestPoint(toRect, fromRect);

		fromPoint = adjustPoint(fromPoint, fromRect, toRect);
		toPoint = adjustPoint(toPoint, toRect, fromRect);

		switch (relationship.getRelationType()) {
		case EXTENDS:
			g2.setColor(new Color(0x3b4259));
			break;
		case IMPLEMENTS:
			g2.setColor(new Color(0x3d404a));
			break;
		case AGGREGATES:
			g2.setColor(new Color(0x7c7f8f));
			break;
		case ASSOCIATES:
			g2.setColor(new Color(0x4c566a));
			break;
		case USES:
			g2.setColor(new Color(0x5e81ac));
			break;
		default:
			g2.setColor(Color.BLACK);
		}

		drawRelationLine(g2, fromPoint, toPoint, relationship.getRelationType());
		drawRelationshipText(g2, fromPoint, toPoint, relationship.getRelationType().toString());
		drawnRelationships.add(relationshipKey);
	}

	private void drawRelationLine(Graphics2D g2, Point from, Point to, RelationType relationType) {
		g2.drawLine(from.x, from.y, to.x, to.y);

		switch (relationType) {
		case EXTENDS:
			drawArrow(g2, from, to);
			break;
		case IMPLEMENTS:
			drawDashedLine(g2, from, to);
			drawHollowArrow(g2, from, to);
			break;
		case AGGREGATES:
			drawDiamond(g2, from, to, false);
			break;
		case ASSOCIATES:
			drawDiamond(g2, from, to, true);
			break;
		case USES:
			drawSolidLine(g2, from, to);
			drawArrow(g2, from, to);
			break;
		}
	}

	private void drawRelationshipText(Graphics2D g2, Point from, Point to, String text) {
		int midX = (from.x + to.x) / 2;
		int midY = (from.y + to.y) / 2;

		int offsetX = 10;
		int offsetY = 10;

		if (Math.abs(to.x - from.x) > Math.abs(to.y - from.y)) {
			if (to.y > from.y) {
				midY -= offsetY;
			} else {
				midY += offsetY;
			}
		} else {
			if (to.x > from.x) {
				midX -= offsetX;
			} else {
				midX += offsetX;
			}
		}

		Font originalFont = g2.getFont();
		g2.setFont(new Font(originalFont.getName(), Font.BOLD, 12));
		g2.drawString(text, midX, midY);
		g2.setFont(originalFont);
	}

	private void drawArrow(Graphics2D g2, Point start, Point end) {
		int arrowSize = 20;
		double angle = Math.atan2(end.y - start.y, end.x - start.x);

		int x1 = end.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
		int y1 = end.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

		int x2 = end.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
		int y2 = end.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

		Polygon arrowHead = new Polygon(new int[] { end.x, x1, x2 }, new int[] { end.y, y1, y2 }, 3);

		g2.fillPolygon(arrowHead);
	}

	private void drawHollowArrow(Graphics2D g2, Point start, Point end) {
		int arrowSize = 10;
		double angle = Math.atan2(end.y - start.y, end.x - start.x);

		int x1 = end.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
		int y1 = end.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));

		int x2 = end.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
		int y2 = end.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

		g2.setColor(Color.BLACK);
		g2.drawLine(end.x, end.y, x1, y1);
		g2.drawLine(end.x, end.y, x2, y2);
	}

	private void drawDiamond(Graphics2D g2, Point from, Point to, boolean filled) {
		int diamondSize = 10;

		double dx = to.x - from.x;
		double dy = to.y - from.y;
		double length = Math.sqrt(dx * dx + dy * dy);

		double unitDx = dx / length;
		double unitDy = dy / length;

		int offsetX = (int) (unitDx * diamondSize);
		int offsetY = (int) (unitDy * diamondSize);

		int centerX = from.x + offsetX;
		int centerY = from.y + offsetY;

		int topX = centerX;
		int topY = centerY - diamondSize;

		int leftX = centerX - diamondSize;
		int leftY = centerY;

		int bottomX = centerX;
		int bottomY = centerY + diamondSize;

		int rightX = centerX + diamondSize;
		int rightY = centerY;

		Polygon diamond = new Polygon(new int[] { topX, leftX, bottomX, rightX },
				new int[] { topY, leftY, bottomY, rightY }, 4);

		double angle = Math.atan2(dy, dx);
		AffineTransform transform = new AffineTransform();
		transform.setToRotation(angle, centerX, centerY);

		Shape rotatedDiamond = transform.createTransformedShape(diamond);

		if (filled) {
			g2.setColor(Color.BLACK);
			g2.fill(rotatedDiamond);
		} else {
			g2.setColor(Color.WHITE);
			g2.fill(rotatedDiamond);
		}

		g2.setColor(Color.BLACK);
		g2.draw(rotatedDiamond);
	}

	private void drawDashedLine(Graphics2D g2, Point start, Point end) {
		float[] dashPattern = { 10.0f, 10.0f };
		Stroke originalStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dashPattern, 0.0f));
		g2.drawLine(start.x, start.y, end.x, end.y);
		g2.setStroke(originalStroke);
	}

	private void drawSolidLine(Graphics2D g2, Point from, Point to) {
		g2.setStroke(new BasicStroke(1));
		g2.drawLine(from.x, from.y, to.x, to.y);
	}

	private Point adjustPoint(Point point, Rectangle rect, Rectangle otherRect) {
		int padding = 10;
		if (otherRect.x > rect.x + rect.width) {
			point.x = rect.x + rect.width + padding;
		} else if (otherRect.x + otherRect.width < rect.x) {
			point.x = rect.x - padding;
		} else if (otherRect.y > rect.y + rect.height) {
			point.y = rect.y + rect.height + padding;
		} else if (otherRect.y + otherRect.height < rect.y) {
			point.y = rect.y - padding;
		}
		return point;
	}

	private Point getNearestPoint(Rectangle fromRect, Rectangle toRect) {
		int x = fromRect.x;
		int y = fromRect.y;

		if (toRect.x > fromRect.x + fromRect.width) {
			x = fromRect.x + fromRect.width;
		} else if (toRect.x + toRect.width < fromRect.x) {
			x = fromRect.x;
		} else {
			x = fromRect.x + fromRect.width / 2;
		}

		if (toRect.y > fromRect.y + fromRect.height) {
			y = fromRect.y + fromRect.height;
		} else if (toRect.y + toRect.height < fromRect.y) {
			y = fromRect.y;
		} else {
			y = fromRect.y + fromRect.height / 2;
		}

		return new Point(x, y);
	}
}