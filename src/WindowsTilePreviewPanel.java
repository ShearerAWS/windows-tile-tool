import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class WindowsTilePreviewPanel extends JPanel {

	private WindowsTile tile;
	private boolean isSmallIcon;

	public WindowsTilePreviewPanel() {
		isSmallIcon = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (isSmallIcon) {
			setBackground(Color.LIGHT_GRAY);

			g2d.setColor(tile.getBackgroundColor());
			g2d.fillRect(40, 40, 70, 70);

			if (tile.getCustomImage() && tile.getImage70() != null) {
				g2d.drawImage(tile.getImage70().getScaledInstance(70, 70, Image.SCALE_SMOOTH), 40, 40, null);
			} else {
				g2d.setColor(Color.BLACK);
				g2d.fillRoundRect(57, 57, 36, 36, 10, 10);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font(null, Font.BOLD, 25));
				g2d.drawString("i", 72, 83);

				// g2d.drawImage(tile.getIcon(), 40, 40, null);
			}
		} else {
			setBackground(tile.getBackgroundColor());

			if (tile.getCustomImage() && tile.getImage150() != null) {
				g2d.drawImage(tile.getImage150().getScaledInstance(150, 150, Image.SCALE_SMOOTH), 0, 0, null);
			} else {
				g2d.setColor(Color.BLACK);
				g2d.fillRoundRect(50, 50, 50, 50, 15, 15);

				g2d.setColor(Color.WHITE);
				g2d.setFont(new Font(null, Font.BOLD, 15));
				g2d.drawString("icon", 61, 80);

				// g2d.drawImage(tile.getIcon(), 0, 0, null);
			}

			if (tile.getShowLabel()) {
				g2d.setFont(new Font(null, Font.PLAIN, 17));
				Color fontColor = tile.isLabelLight() ? Color.WHITE : Color.BLACK;
				g2d.setColor(fontColor);

				String labelText = tile.getName();
				if (g2d.getFontMetrics().stringWidth(labelText) > 120) {
					while (g2d.getFontMetrics().stringWidth(labelText) > 120) {
						labelText = labelText.substring(0, labelText.length() - 1);
					}
					labelText += "...";
				}

				g2d.drawString(labelText, 10, getHeight() - 10);
			}
		}
	}

	public void renderPreview(WindowsTile tile) {
		this.tile = tile;
		repaint();
	}

	public void switchIconSize() {
		isSmallIcon = !isSmallIcon;
		repaint();
	}

	public boolean isSmallIcon() {
		return isSmallIcon;
	}
}
