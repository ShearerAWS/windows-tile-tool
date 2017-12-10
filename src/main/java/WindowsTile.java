package main.java;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Date;

import javax.imageio.ImageIO;

public class WindowsTile {
	final private String DEFAULT_COLOR = "#2982CC";

	private File shortcut, launcher;
	private String name;
	private Color backgroundColor;
	private Image icon, image150, image70;
	private boolean showLabel, isLabelLight, customImage;

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

		if (manifest.exists()) {
			parseVisualElements(manifest);
			// setDefaultSettings();
		} else {
			setDefaultSettings();
		}

	}

	public void setDefaultSettings() {
		showLabel = true;
		isLabelLight = true;
		customImage = false;

		// backgroundColor = UIManager.getColor("desktop");
		backgroundColor = Color.decode(DEFAULT_COLOR);
	}

	private void parseVisualElements(File manifest) {
		setDefaultSettings();
		try {
			String drive = System.getenv("SystemDrive");
			BufferedReader in = new BufferedReader(new FileReader(manifest));
			String fullLine = in.readLine();

			while (fullLine != null) {
				for (String line : fullLine.split("\n")) {
					if (line.contains("ShowNameOnSquare150x150Logo")) {
						String value;
						if (line.contains("'")) {
							value = line.split("'")[1];
						} else {
							value = line.split("\"")[1];
						}
						if (value.equals("on")) {
							showLabel = true;
						} else {
							showLabel = false;
						}
					} else if (line.contains("ForegroundText")) {
						String value;
						if (line.contains("'")) {
							value = line.split("'")[1];
						} else {
							value = line.split("\"")[1];
						}
						if (value.equals("light")) {
							isLabelLight = true;
						} else {
							isLabelLight = false;
						}
					} else if (line.contains("BackgroundColor")) {
						String value;
						if (line.contains("'")) {
							value = line.split("'")[1];
						} else {
							value = line.split("\"")[1];
						}
						try {
							Field field = Color.class.getField(value);
							backgroundColor = (Color) field.get(null);
						} catch (Exception e) {
							backgroundColor = Color.decode(value);
						}
					} else if (line.contains("Square150x150Logo")) {
						String path;
						if (line.contains("'")) {
							path = line.split("'")[1];
						} else {
							path = line.split("\"")[1];
						}
						if (!path.startsWith(drive)) {
							path = launcher.getParentFile().getAbsolutePath() + "/" + path;
						}
						customImage = true;
						image150 = ImageIO.read(new File(path));

					} else if (line.contains("Square70x70Logo")) {
						String path;
						if (line.contains("'")) {
							path = line.split("'")[1];
						} else {
							path = line.split("\"")[1];
						}
						if (!path.startsWith(drive)) {
							path = launcher.getParentFile().getAbsolutePath() + "/" + path;
						}
						customImage = true;
						image70 = ImageIO.read(new File(path));
					}
				}
				fullLine = in.readLine();
			}

			in.close();
		} catch (Exception e) {
			System.out.println("Failed " + name);
			e.printStackTrace();
			// TODO: Handle input error
			setDefaultSettings();
		}

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

					String image150Path = dir.getAbsolutePath() + "\\" + name + "Icon150x150.png";
					File image150File = new File(image150Path);

					BufferedImage buffered150 = new BufferedImage(image150.getWidth(null), image150.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D bGr = buffered150.createGraphics();
					bGr.drawImage(image150, 0, 0, null);
					bGr.dispose();

					ImageIO.write(buffered150, "png", image150File);
					out.println("    Square150x150Logo='VisualElementsResources\\" + name + "Icon150x150.png'");
				}
				if (image70 != null) {
					File dir = new File(launcher.getParentFile().getAbsolutePath() + "/VisualElementsResources/");
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String image70Path = dir.getAbsolutePath() + "\\" + name + "Icon70x70.png";
					File image70File = new File(image70Path);

					BufferedImage buffered70 = new BufferedImage(image70.getWidth(null), image70.getHeight(null),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D bGr = buffered70.createGraphics();
					bGr.drawImage(image70, 0, 0, null);
					bGr.dispose();

					ImageIO.write(buffered70, "png", image70File);
					out.println("    Square70x70Logo='VisualElementsResources\\" + name + "Icon70x70.png'");
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
			System.out.println("Error deleting");
			// TODO: Handle error
		}

		if (!shortcut.setLastModified(new Date().getTime())) {
			// TODO: Handle error
		}

		setDefaultSettings();

	}

	public File getShorcut() {
		return shortcut;
	}

	public File getLauncher() {
		return launcher;
	}

	public String getName() {
		return name;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Image getIcon() {
		return icon;
	}

	public Image getImage150() {
		return image150;
	}

	public boolean hasImage150() {
		return image150 != null;
	}

	public Image getImage70() {
		return image70;
	}

	public boolean hasImage70() {
		return image70 != null;
	}

	public boolean getShowLabel() {
		return showLabel;
	}

	public boolean isLabelLight() {
		return isLabelLight;
	}

	public boolean getCustomImage() {
		return customImage;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setImage150(Image image150) {
		this.image150 = image150;
	}

	public void setImage70(Image image70) {
		this.image70 = image70;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	public void setIsLabelLight(boolean isLabelLight) {
		this.isLabelLight = isLabelLight;

	}

	public void setCustomImage(boolean customImage) {
		this.customImage = customImage;

	}
}
