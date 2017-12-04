import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class WindowsTile {
    private String name;
    private File launcher;
    private Icon icon;
    private boolean hasVisualElements;

    public WindowsTile(File shortcut, String path) {
	name = shortcut.getName().replaceFirst("[.][^.]+$", "");
	launcher = new File(path);
	icon = FileSystemView.getFileSystemView().getSystemIcon(shortcut);

	/*
	 * Checks if tile has a .VisualElementsManifest.xml
	 */
	String launcherPathWOExtension = launcher.getAbsolutePath().replaceFirst("[.][^.]+$", "");
	String manifestPath = launcherPathWOExtension + ".VisualElementsManifest.xml";
	File manifest = new File(manifestPath);
	hasVisualElements = manifest.exists();
    }

    public String getName() {
	return name;
    }

    public File launcher() {
	return launcher;
    }

    public boolean hasVisualElements() {
	return hasVisualElements;
    }

    public void exportVisualElements() {
	// TODO: Export VisualElementsManifest.xml
    }
}
