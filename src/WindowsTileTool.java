import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Tool that allows user to customize Windows start menu tiles.
 *
 * @author Alexander Shearer
 */
public class WindowsTileTool {

    private static ArrayList<WindowsTile> tiles;

    public static void main(String[] args) throws IOException, ParseException {
	loadTiles();
    }

    /**
     * loads WindowsTiles from the start menu
     */
    private static void loadTiles() {
	String drive = System.getenv("SystemDrive");
	String startMenuPath = drive + "/ProgramData/Microsoft/Windows/Start Menu/Programs/";
	File startMenu = new File(startMenuPath);
	File[] shortcutFiles = getAllLinks(startMenu);
	tiles = new ArrayList<WindowsTile>();
	for (File f : shortcutFiles) {
	    try {
		WindowsShortcut s = new WindowsShortcut(f);
		if (s.getRealFilename().startsWith(drive)) {
		    WindowsTile t = new WindowsTile(f.getName(), s.getRealFilename());
		    tiles.add(t);
		}

	    } catch (Exception e) {
		// TODO: Process website shortcuts
	    }
	}
    }

    /**
     * Finds all .lnk files within a directory and the sub directories
     *
     * @param directory
     *            the root directory to be searched
     * @return array of all .lnk Files
     */
    private static File[] getAllLinks(File directory) {
	ArrayList<File> files = new ArrayList<File>();
	for (File f : directory.listFiles()) {
	    if (f.isFile() && f.getAbsolutePath().endsWith(".lnk")) {
		files.add(f);
	    } else if (f.isDirectory()) {
		for (File subF : getAllLinks(f)) {
		    files.add(subF);
		}
	    }
	}
	return files.toArray(new File[files.size()]);
    }

}
