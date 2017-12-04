import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Tool that allows user to customize Windows start menu tiles.
 *
 * @author Alexander Shearer
 */
public class WindowsTileTool extends JFrame implements ActionListener {

    private JPanel contentPane, colorPreviewField;
    private JButton btnChangeSize, btnApply, btnRestoreDefault, btnOpenShortcutLocation, btnAbout, btnImageSelect150,
	    btnImageSelect70;
    private JLabel lblApplicationName;
    private JComboBox<String> programComboBox, labelColorComboBox;
    private JTextField backgroundColorTextField;
    private JRadioButton showLabelRadio, customImageRadio;
    private Canvas previewCanvas;
    private ImageIcon minIcon, minRollIcon, maxIcon, maxRollIcon;

    private ArrayList<WindowsTile> tiles;

    public WindowsTileTool() {
	loadTiles();
	setUpGUI();

    }

    private void setUpGUI() {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}

	setResizable(false);
	setTitle("Windows Tile Tool");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setBounds(100, 100, 473, 324);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
	contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(contentPane);

	JPanel displayPanel = new JPanel();
	contentPane.add(displayPanel, BorderLayout.WEST);
	GridBagLayout gbl_displayPanel = new GridBagLayout();
	gbl_displayPanel.columnWidths = new int[] { 8, 4, 0 };
	gbl_displayPanel.rowHeights = new int[] { 25, 0, 0, 0, 23, 0 };
	gbl_displayPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
	gbl_displayPanel.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
	displayPanel.setLayout(gbl_displayPanel);

	minIcon = new ImageIcon(TestingWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize.gif"));
	minRollIcon = new ImageIcon(
		TestingWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/minimize-pressed.gif"));
	maxIcon = new ImageIcon(TestingWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/maximize.gif"));
	maxRollIcon = new ImageIcon(
		TestingWindow.class.getResource("/javax/swing/plaf/metal/icons/ocean/maximize-pressed.gif"));

	btnChangeSize = new JButton("");
	btnChangeSize.setBorderPainted(false);
	btnChangeSize.setBorder(null);
	btnChangeSize.setFocusable(false);
	btnChangeSize.setMargin(new Insets(0, 0, 0, 0));
	btnChangeSize.setContentAreaFilled(false);
	btnChangeSize.setIcon(minIcon);
	btnChangeSize.setRolloverIcon(minRollIcon);
	btnChangeSize.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    }
	});

	GridBagConstraints gbc_btnChangeSize = new GridBagConstraints();
	gbc_btnChangeSize.fill = GridBagConstraints.VERTICAL;
	gbc_btnChangeSize.insets = new Insets(0, 0, 5, 5);
	gbc_btnChangeSize.gridx = 0;
	gbc_btnChangeSize.gridy = 0;
	displayPanel.add(btnChangeSize, gbc_btnChangeSize);

	lblApplicationName = new JLabel("Chrome");
	GridBagConstraints gbc_lblApplicationName = new GridBagConstraints();
	gbc_lblApplicationName.fill = GridBagConstraints.BOTH;
	gbc_lblApplicationName.insets = new Insets(0, 0, 5, 0);
	gbc_lblApplicationName.gridx = 1;
	gbc_lblApplicationName.gridy = 0;
	displayPanel.add(lblApplicationName, gbc_lblApplicationName);
	lblApplicationName.setFont(new Font("Tahoma", Font.BOLD, 16));

	JPanel previewPanel = new JPanel();
	previewPanel.setBackground(Color.LIGHT_GRAY);
	GridBagConstraints gbc_previewPanel = new GridBagConstraints();
	gbc_previewPanel.gridwidth = 2;
	gbc_previewPanel.insets = new Insets(0, 0, 5, 0);
	gbc_previewPanel.fill = GridBagConstraints.BOTH;
	gbc_previewPanel.gridx = 0;
	gbc_previewPanel.gridy = 1;
	displayPanel.add(previewPanel, gbc_previewPanel);

	previewCanvas = new Canvas();
	previewPanel.add(previewCanvas);

	btnApply = new JButton("Apply");
	GridBagConstraints gbc_btnApply = new GridBagConstraints();
	gbc_btnApply.gridwidth = 2;
	gbc_btnApply.fill = GridBagConstraints.BOTH;
	gbc_btnApply.insets = new Insets(0, 0, 5, 0);
	gbc_btnApply.gridx = 0;
	gbc_btnApply.gridy = 2;
	displayPanel.add(btnApply, gbc_btnApply);

	btnRestoreDefault = new JButton("Restore Default");
	GridBagConstraints gbc_btnRestoreDefault = new GridBagConstraints();
	gbc_btnRestoreDefault.gridwidth = 2;
	gbc_btnRestoreDefault.insets = new Insets(0, 0, 5, 0);
	gbc_btnRestoreDefault.fill = GridBagConstraints.BOTH;
	gbc_btnRestoreDefault.gridx = 0;
	gbc_btnRestoreDefault.gridy = 3;
	displayPanel.add(btnRestoreDefault, gbc_btnRestoreDefault);
	btnRestoreDefault.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    }
	});

	btnOpenShortcutLocation = new JButton("Open Shortcut Location");
	GridBagConstraints gbc_btnOpenShortcutLocation = new GridBagConstraints();
	gbc_btnOpenShortcutLocation.gridwidth = 2;
	gbc_btnOpenShortcutLocation.anchor = GridBagConstraints.NORTH;
	gbc_btnOpenShortcutLocation.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnOpenShortcutLocation.gridx = 0;
	gbc_btnOpenShortcutLocation.gridy = 4;
	displayPanel.add(btnOpenShortcutLocation, gbc_btnOpenShortcutLocation);

	JPanel settingsPanel = new JPanel();
	contentPane.add(settingsPanel, BorderLayout.CENTER);
	GridBagLayout gbl_settingsPanel = new GridBagLayout();
	gbl_settingsPanel.columnWidths = new int[] { 98, 78, 0 };
	gbl_settingsPanel.rowHeights = new int[] { 23, 0, 27, 0, 0, 0, 0, 0, 0, 0 };
	gbl_settingsPanel.columnWeights = new double[] { 1.0, 1.0, 0.0 };
	gbl_settingsPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
	settingsPanel.setLayout(gbl_settingsPanel);

	JLabel lblProgram = new JLabel("Program:");
	GridBagConstraints gbc_lblProgram = new GridBagConstraints();
	gbc_lblProgram.insets = new Insets(0, 0, 5, 5);
	gbc_lblProgram.anchor = GridBagConstraints.EAST;
	gbc_lblProgram.gridx = 0;
	gbc_lblProgram.gridy = 1;
	settingsPanel.add(lblProgram, gbc_lblProgram);

	programComboBox = new JComboBox<String>();
	lblProgram.setLabelFor(programComboBox);
	GridBagConstraints gbc_programComboBox = new GridBagConstraints();
	gbc_programComboBox.gridwidth = 2;
	gbc_programComboBox.fill = GridBagConstraints.HORIZONTAL;
	gbc_programComboBox.insets = new Insets(0, 0, 5, 0);
	gbc_programComboBox.gridx = 1;
	gbc_programComboBox.gridy = 1;
	settingsPanel.add(programComboBox, gbc_programComboBox);
	for (WindowsTile t : tiles) {
	    programComboBox.addItem(t.getName());
	}
	programComboBox.setPrototypeDisplayValue("");

	JLabel lblColor = new JLabel("Color:");
	GridBagConstraints gbc_lblColor = new GridBagConstraints();
	gbc_lblColor.anchor = GridBagConstraints.EAST;
	gbc_lblColor.insets = new Insets(0, 0, 5, 5);
	gbc_lblColor.gridx = 0;
	gbc_lblColor.gridy = 2;
	settingsPanel.add(lblColor, gbc_lblColor);

	backgroundColorTextField = new JTextField();
	lblColor.setLabelFor(backgroundColorTextField);
	backgroundColorTextField.setText("#FFFFFF");
	GridBagConstraints gbc_backgroundColorTextField = new GridBagConstraints();
	gbc_backgroundColorTextField.fill = GridBagConstraints.BOTH;
	gbc_backgroundColorTextField.insets = new Insets(0, 0, 5, 5);
	gbc_backgroundColorTextField.gridx = 1;
	gbc_backgroundColorTextField.gridy = 2;
	settingsPanel.add(backgroundColorTextField, gbc_backgroundColorTextField);
	backgroundColorTextField.setColumns(10);

	colorPreviewField = new JPanel();
	colorPreviewField.setBackground(Color.RED);
	GridBagConstraints gbc_colorPreviewField = new GridBagConstraints();
	gbc_colorPreviewField.fill = GridBagConstraints.BOTH;
	gbc_colorPreviewField.insets = new Insets(0, 0, 5, 0);
	gbc_colorPreviewField.gridx = 2;
	gbc_colorPreviewField.gridy = 2;
	settingsPanel.add(colorPreviewField, gbc_colorPreviewField);

	JLabel lblLabelColor = new JLabel("Label Color:");
	GridBagConstraints gbc_lblLabelColor = new GridBagConstraints();
	gbc_lblLabelColor.anchor = GridBagConstraints.EAST;
	gbc_lblLabelColor.insets = new Insets(0, 0, 5, 5);
	gbc_lblLabelColor.gridx = 0;
	gbc_lblLabelColor.gridy = 3;
	settingsPanel.add(lblLabelColor, gbc_lblLabelColor);

	labelColorComboBox = new JComboBox<String>();
	lblLabelColor.setLabelFor(labelColorComboBox);
	labelColorComboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "light", "dark" }));
	GridBagConstraints gbc_labelColorComboBox = new GridBagConstraints();
	gbc_labelColorComboBox.insets = new Insets(0, 0, 5, 5);
	gbc_labelColorComboBox.fill = GridBagConstraints.BOTH;
	gbc_labelColorComboBox.gridx = 1;
	gbc_labelColorComboBox.gridy = 3;
	settingsPanel.add(labelColorComboBox, gbc_labelColorComboBox);

	showLabelRadio = new JRadioButton("Show Label");
	GridBagConstraints gbc_showLabelRadio = new GridBagConstraints();
	gbc_showLabelRadio.insets = new Insets(0, 0, 5, 0);
	gbc_showLabelRadio.fill = GridBagConstraints.HORIZONTAL;
	gbc_showLabelRadio.gridx = 2;
	gbc_showLabelRadio.gridy = 3;
	settingsPanel.add(showLabelRadio, gbc_showLabelRadio);

	btnAbout = new JButton("About");
	btnAbout.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    }
	});

	Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
	GridBagConstraints gbc_rigidArea = new GridBagConstraints();
	gbc_rigidArea.gridwidth = 3;
	gbc_rigidArea.insets = new Insets(0, 0, 5, 5);
	gbc_rigidArea.gridx = 0;
	gbc_rigidArea.gridy = 4;
	settingsPanel.add(rigidArea, gbc_rigidArea);

	customImageRadio = new JRadioButton("Custom Image");
	GridBagConstraints gbc_customImageRadio = new GridBagConstraints();
	gbc_customImageRadio.gridwidth = 3;
	gbc_customImageRadio.insets = new Insets(0, 0, 5, 0);
	gbc_customImageRadio.gridx = 0;
	gbc_customImageRadio.gridy = 5;
	settingsPanel.add(customImageRadio, gbc_customImageRadio);

	JLabel lblImage150 = new JLabel("150x150 Image");
	GridBagConstraints gbc_lblImage150 = new GridBagConstraints();
	gbc_lblImage150.anchor = GridBagConstraints.EAST;
	gbc_lblImage150.insets = new Insets(0, 0, 5, 5);
	gbc_lblImage150.gridx = 0;
	gbc_lblImage150.gridy = 6;
	settingsPanel.add(lblImage150, gbc_lblImage150);

	btnImageSelect150 = new JButton("Select Image");
	btnImageSelect150.setEnabled(false);
	GridBagConstraints gbc_btnImageSelect150 = new GridBagConstraints();
	gbc_btnImageSelect150.gridwidth = 2;
	gbc_btnImageSelect150.fill = GridBagConstraints.BOTH;
	gbc_btnImageSelect150.insets = new Insets(0, 0, 5, 0);
	gbc_btnImageSelect150.gridx = 1;
	gbc_btnImageSelect150.gridy = 6;
	settingsPanel.add(btnImageSelect150, gbc_btnImageSelect150);
	lblImage150.setLabelFor(btnImageSelect150);

	JLabel lblImage70 = new JLabel("70x70 Image");
	GridBagConstraints gbc_lblImage70 = new GridBagConstraints();
	gbc_lblImage70.anchor = GridBagConstraints.EAST;
	gbc_lblImage70.insets = new Insets(0, 0, 5, 5);
	gbc_lblImage70.gridx = 0;
	gbc_lblImage70.gridy = 7;
	settingsPanel.add(lblImage70, gbc_lblImage70);

	btnImageSelect70 = new JButton("Select Image");
	lblImage70.setLabelFor(btnImageSelect70);
	btnImageSelect70.setEnabled(false);
	GridBagConstraints gbc_btnImageSelect70 = new GridBagConstraints();
	gbc_btnImageSelect70.fill = GridBagConstraints.BOTH;
	gbc_btnImageSelect70.gridwidth = 2;
	gbc_btnImageSelect70.insets = new Insets(0, 0, 5, 0);
	gbc_btnImageSelect70.gridx = 1;
	gbc_btnImageSelect70.gridy = 7;
	settingsPanel.add(btnImageSelect70, gbc_btnImageSelect70);
	GridBagConstraints gbc_btnAbout = new GridBagConstraints();
	gbc_btnAbout.fill = GridBagConstraints.HORIZONTAL;
	gbc_btnAbout.anchor = GridBagConstraints.SOUTH;
	gbc_btnAbout.gridx = 2;
	gbc_btnAbout.gridy = 8;
	settingsPanel.add(btnAbout, gbc_btnAbout);

	setVisible(true);
    }

    /**
     * loads WindowsTiles from the start menu
     */
    private void loadTiles() {
	String drive = System.getenv("SystemDrive");
	String startMenuAppdataPath = System.getenv("APPDATA") + "/Microsoft/Windows/Start Menu/Programs/";
	String startMenuPath = drive + "/ProgramData/Microsoft/Windows/Start Menu/Programs/";

	File startMenu = new File(startMenuPath);
	File startMenuAppdata = new File(startMenuAppdataPath);

	ArrayList<File> shortcutFiles = getAllLinks(startMenu);
	shortcutFiles.addAll(getAllLinks(startMenuAppdata));

	tiles = new ArrayList<WindowsTile>();
	for (File f : shortcutFiles) {
	    try {
		WindowsShortcut s = new WindowsShortcut(f);
		if (s.getRealFilename().startsWith(drive)) {
		    WindowsTile t = new WindowsTile(f, s.getRealFilename());
		    tiles.add(t);
		} else {
		    // TODO: Process corrupt shortcuts
		}
	    } catch (Exception e) {
		// TODO: Process website shortcuts
	    }
	}

	tiles.sort(new Comparator<WindowsTile>() {
	    @Override
	    public int compare(WindowsTile wt1, WindowsTile wt2) {
		return wt1.getName().compareToIgnoreCase(wt2.getName());
	    }
	});
    }

    /**
     * Finds all .lnk files within a directory and the sub directories
     *
     * @param directory
     *            the root directory to be searched
     * @return array of all .lnk Files
     */
    private ArrayList<File> getAllLinks(File directory) {
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
	return files;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	if (source.equals(programComboBox)) {

	}

    }

}
