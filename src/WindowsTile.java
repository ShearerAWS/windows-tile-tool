import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import javax.imageio.ImageIO;

public class WindowsTile {
	private String name;
	private File shortcut, launcher;
	private Image icon, image150, image70;
	private Color backgroundColor;
	private boolean showLabel;
	private boolean isLabelLight;
	private boolean customImage;
	private boolean hasVisualElements;

	final private String DEFAULT_COLOR = "#2982CC";

	public WindowsTile(File shortcut, String path) {
		name = shortcut.getName().replaceFirst("[.][^.]+$", "");
		this.shortcut = shortcut;
		launcher = new File(path);

		try {
			// icon = sun.awt.shell.ShellFolder.getShellFolder(launcher).getIcon(true);
			// icon = JFileChooser.getUI().getFileView(fc).getIcon(shortcut);
		} catch (Exception e) {
			// TODO: Handle file not found
		}

		/*
		 * Checks if tile has a .VisualElementsManifest.xml
		 */
		String launcherPathWOExtension = launcher.getAbsolutePath().replaceFirst("[.][^.]+$", "");
		String manifestPath = launcherPathWOExtension + ".VisualElementsManifest.xml";
		File manifest = new File(manifestPath);
		hasVisualElements = manifest.exists();

		if (hasVisualElements) {
			// TODO: Parse VisualElements
			setDefaultSettings();
		} else {
			setDefaultSettings();
		}

	}

	public void setDefaultSettings() {
		showLabel = true;
		isLabelLight = true;
		customImage = false;
		backgroundColor = Color.decode(DEFAULT_COLOR);
	}

	public void exportVisualElements() {
		try {
			String launcherPathWOExtension = launcher.getAbsolutePath().replaceFirst("[.][^.]+$", "");
			String manifestPath = launcherPathWOExtension + ".VisualElementsManifest.xml";
			PrintWriter out = new PrintWriter(new FileWriter(manifestPath));

			out.println("<Application xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>");
			out.println("  <VisualElements");

			out.println("    ShowNameOnSquare150x150Logo=" + (showLabel ? "'on'" : "'off'"));
			out.println("    ForegroundText=" + (isLabelLight ? "'light'" : "'dark'"));
			out.println("    BackgroundColor='" + String.format("#%02x%02x%02x", backgroundColor.getRed(),
					backgroundColor.getGreen(), backgroundColor.getBlue()) + "'");

			if (customImage) {
				if (image150 != null) {
					File dir = new File(launcher.getParentFile().getAbsolutePath() + "/VisualElementsResources/");
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String image150Path = dir.getAbsolutePath() + "\\icon150x150.png";
					File image150File = new File(image150Path);

					BufferedImage buffered150 = new BufferedImage(image150.getWidth(null), image150.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D bGr = buffered150.createGraphics();
					bGr.drawImage(image150, 0, 0, null);
					bGr.dispose();

					ImageIO.write(buffered150, "png", image150File);
					out.println("    Square150x150Logo='VisualElementsResources\\icon150x150.png'");
				}
				if (image70 != null) {
					File dir = new File(launcher.getParentFile().getAbsolutePath() + "/VisualElementsResources/");
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String image70Path = dir.getAbsolutePath() + "\\icon70x70.png";
					File image70File = new File(image70Path);

					BufferedImage buffered70 = new BufferedImage(image70.getWidth(null), image70.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D bGr = buffered70.createGraphics();
					bGr.drawImage(image70, 0, 0, null);
					bGr.dispose();

					ImageIO.write(buffered70, "png", image70File);
					out.println("    Square70x70Logo='VisualElementsResources\\icon70x70.png'");
				}
			}

			out.println("  />");
			out.println("</Application>");

			out.close();

			if (!shortcut.setLastModified(new Date().getTime())) {
				System.out.println("Failed");
				// TODO: Handle error
			}

		} catch (Exception e) {
			// TODO: Handle error
			e.printStackTrace();
		}
	}

	public void removeVisualElements() {
		String launcherPathWOExtension = launcher.getAbsolutePath().replaceFirst("[.][^.]+$", "");
		String manifestPath = launcherPathWOExtension + ".VisualElementsManifest.xml";
		File manifestFile = new File(manifestPath);
		if (!manifestFile.delete()) {
			// TODO: Handle error
		}

		if (!shortcut.setLastModified(new Date().getTime())) {
			// TODO: Handle error
		}

		setDefaultSettings();

	}

	public String getName() {
		return name;
	}

	public File getLauncher() {
		return launcher;
	}

	public File getShorcut() {
		return shortcut;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public boolean hasVisualElements() {
		return hasVisualElements;
	}

	public Image getImage150() {
		return image150;
	}

	public Image getImage70() {
		return image70;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isLabelLight() {
		return isLabelLight;
	}

	public boolean getShowLabel() {
		return showLabel;
	}

	public boolean getCustomImage() {
		return customImage;
	}

	public Image getIcon() {
		return icon;
	}

	public void setIsLabelLight(boolean isLabelLight) {
		this.isLabelLight = isLabelLight;

	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public void setCustomImage(boolean customImage) {
		this.customImage = customImage;

	}

	public void setImage150(Image image150) {
		this.image150 = image150.getScaledInstance(1024, 1024, Image.SCALE_DEFAULT);
	}

	public void setImage70(Image image70) {
		this.image70 = image70.getScaledInstance(1024, 1024, Image.SCALE_DEFAULT);
	}
}
