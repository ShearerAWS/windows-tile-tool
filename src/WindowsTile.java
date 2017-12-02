import java.io.File;

public class WindowsTile {
    private String name;
    private File launcher;
    boolean hasVisualElements;

    public WindowsTile(String name, String path) {
	this.name = name;
	launcher = new File(path);

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

    public boolean hasVisualElements() {
	return hasVisualElements;
    }

    public void exportVisualElements() {

    }
}
