import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class WindowsTilePreviewPanel extends JPanel {

	private WindowsTile tile;
	private boolean isSmallIcon;

	public WindowsTilePreviewPanel() {
		// setBorder(BorderFactory.createLineBorder(Color.black));

		isSmallIcon = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// TODO: Proper tile display
		// System.setProperty("swing.aatext", "true");

		if (isSmallIcon) {
			setBackground(Color.LIGHT_GRAY);

			g.setColor(tile.getBackgroundColor());
			g.fillRect(40, 40, 70, 70);

			if (tile.getCustomImage() && tile.getImage70() != null) {
				g.drawImage(tile.getImage70().getScaledInstance(70, 70, Image.SCALE_SMOOTH), 40, 40, null);
			} else {
				g.drawImage(tile.getIcon(), 40, 40, null);
			}
		} else {
			setBackground(tile.getBackgroundColor());

			if (tile.getCustomImage() && tile.getImage150() != null) {
				g.drawImage(tile.getImage150().getScaledInstance(150, 150, Image.SCALE_SMOOTH), 0, 0, null);
			} else {
				g.drawImage(tile.getIcon(), 0, 0, null);
			}

			if (tile.getShowLabel()) {
				g.setFont(new Font("Segoe UI Normal", Font.PLAIN, 16));
				Color fontColor = tile.isLabelLight() ? Color.WHITE : Color.BLACK;
				g.setColor(fontColor);
				g.drawString(tile.getName(), 10, getHeight() - 10);
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
