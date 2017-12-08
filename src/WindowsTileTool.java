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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Tool that allows user to customize Windows start menu tiles.
 *
 * @author Alexander Shearer
 */
public class WindowsTileTool extends JFrame implements ActionListener, FocusListener, DocumentListener {

	private JPanel contentPane, colorPreviewField;
	private WindowsTilePreviewPanel tilePreviewPanel;
	private JButton btnChangeSize, btnApply, btnRestoreDefault, btnOpenShortcutLocation, btnAbout, btnImageSelect150,
			btnImageSelect70;
	private JLabel lblApplicationName;
	private JComboBox<String> programComboBox, labelColorComboBox;
	private JTextField backgroundColorTextField;
	private JRadioButton showLabelRadio, customImageRadio;
	private Canvas previewCanvas;
	private ImageIcon minIcon, minRollIcon, maxIcon, maxRollIcon;

	private ArrayList<WindowsTile> tiles;
	private int currentTileIndex;

	public WindowsTileTool() {

		loadTiles();
		setUpGUI();
		changeCurrentTile();

	}

	/**
	 * Sets up the contentPanel. Designed using WindowBuilder
	 */
	private void setUpGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		setResizable(false);
		setTitle("Windows Tile Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 460, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JPanel displayPanel = new JPanel();
		displayPanel.setBounds(10, 10, 150, 265);
		contentPane.add(displayPanel);
		GridBagLayout gbl_displayPanel = new GridBagLayout();
		gbl_displayPanel.columnWidths = new int[] { 8, 4, 0 };
		gbl_displayPanel.rowHeights = new int[] { 25, 0, 0, 0, 23, 0 };
		gbl_displayPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
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
		btnChangeSize.addActionListener(this);

		GridBagConstraints gbc_btnChangeSize = new GridBagConstraints();
		gbc_btnChangeSize.fill = GridBagConstraints.VERTICAL;
		gbc_btnChangeSize.insets = new Insets(0, 0, 5, 5);
		gbc_btnChangeSize.gridx = 0;
		gbc_btnChangeSize.gridy = 0;
		displayPanel.add(btnChangeSize, gbc_btnChangeSize);

		lblApplicationName = new JLabel("");
		GridBagConstraints gbc_lblApplicationName = new GridBagConstraints();
		gbc_lblApplicationName.fill = GridBagConstraints.BOTH;
		gbc_lblApplicationName.insets = new Insets(0, 0, 5, 0);
		gbc_lblApplicationName.gridx = 1;
		gbc_lblApplicationName.gridy = 0;
		displayPanel.add(lblApplicationName, gbc_lblApplicationName);
		lblApplicationName.setFont(new Font("Tahoma", Font.BOLD, 16));

		lblApplicationName.setMinimumSize(new Dimension(70, 20));
		lblApplicationName.setMaximumSize(new Dimension(70, 20));
		lblApplicationName.setPreferredSize(new Dimension(70, 20));

		tilePreviewPanel = new WindowsTilePreviewPanel();
		tilePreviewPanel.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_tilePreviewPanel = new GridBagConstraints();
		gbc_tilePreviewPanel.gridwidth = 2;
		gbc_tilePreviewPanel.insets = new Insets(0, 0, 5, 0);
		gbc_tilePreviewPanel.fill = GridBagConstraints.BOTH;
		gbc_tilePreviewPanel.gridx = 0;
		gbc_tilePreviewPanel.gridy = 1;
		tilePreviewPanel.setPreferredSize(new Dimension(150, 150));
		displayPanel.add(tilePreviewPanel, gbc_tilePreviewPanel);

		tilePreviewPanel.setMinimumSize(new Dimension(150, 150));
		tilePreviewPanel.setMaximumSize(new Dimension(150, 150));
		tilePreviewPanel.setPreferredSize(new Dimension(150, 150));

		btnApply = new JButton("Apply Changes");
		GridBagConstraints gbc_btnApply = new GridBagConstraints();
		gbc_btnApply.gridwidth = 2;
		gbc_btnApply.fill = GridBagConstraints.BOTH;
		gbc_btnApply.insets = new Insets(0, 0, 5, 0);
		gbc_btnApply.gridx = 0;
		gbc_btnApply.gridy = 2;
		displayPanel.add(btnApply, gbc_btnApply);
		btnApply.addActionListener(this);

		btnRestoreDefault = new JButton("Restore Default");
		GridBagConstraints gbc_btnRestoreDefault = new GridBagConstraints();
		gbc_btnRestoreDefault.gridwidth = 2;
		gbc_btnRestoreDefault.insets = new Insets(0, 0, 5, 0);
		gbc_btnRestoreDefault.fill = GridBagConstraints.BOTH;
		gbc_btnRestoreDefault.gridx = 0;
		gbc_btnRestoreDefault.gridy = 3;
		displayPanel.add(btnRestoreDefault, gbc_btnRestoreDefault);
		btnRestoreDefault.addActionListener(this);

		btnOpenShortcutLocation = new JButton("Shortcut Location");
		GridBagConstraints gbc_btnOpenShortcutLocation = new GridBagConstraints();
		gbc_btnOpenShortcutLocation.gridwidth = 2;
		gbc_btnOpenShortcutLocation.anchor = GridBagConstraints.NORTH;
		gbc_btnOpenShortcutLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnOpenShortcutLocation.gridx = 0;
		gbc_btnOpenShortcutLocation.gridy = 4;
		displayPanel.add(btnOpenShortcutLocation, gbc_btnOpenShortcutLocation);
		btnOpenShortcutLocation.addActionListener(this);

		JPanel settingsPanel = new JPanel();
		settingsPanel.setBounds(160, 10, 285, 265);
		contentPane.add(settingsPanel);
		GridBagLayout gbl_settingsPanel = new GridBagLayout();
		gbl_settingsPanel.columnWidths = new int[] { 98, 78, 0 };
		gbl_settingsPanel.rowHeights = new int[] { 23, 27, 27, 0, 0, 0, 0, 0, 0, 0 };
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
		for (WindowsTile t : tiles) {
			programComboBox.addItem(t.getName());
		}
		programComboBox.setPrototypeDisplayValue("");
		settingsPanel.add(programComboBox, gbc_programComboBox);
		programComboBox.addActionListener(this);

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
		backgroundColorTextField.addActionListener(this);
		backgroundColorTextField.addFocusListener(this);
		backgroundColorTextField.getDocument().putProperty("owner", backgroundColorTextField);
		backgroundColorTextField.getDocument().addDocumentListener(this);

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
		labelColorComboBox.addActionListener(this);

		showLabelRadio = new JRadioButton("Show Label");
		GridBagConstraints gbc_showLabelRadio = new GridBagConstraints();
		gbc_showLabelRadio.insets = new Insets(0, 0, 5, 0);
		gbc_showLabelRadio.fill = GridBagConstraints.HORIZONTAL;
		gbc_showLabelRadio.gridx = 2;
		gbc_showLabelRadio.gridy = 3;
		settingsPanel.add(showLabelRadio, gbc_showLabelRadio);
		showLabelRadio.addActionListener(this);

		btnAbout = new JButton("About");
		btnAbout.addActionListener(this);

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
		customImageRadio.addActionListener(this);

		JLabel lblImage150 = new JLabel("Icon:");
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
		btnImageSelect150.addActionListener(this);

		JLabel lblImage70 = new JLabel("Small Icon:");
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
		btnImageSelect70.addActionListener(this);

		GridBagConstraints gbc_btnAbout = new GridBagConstraints();
		gbc_btnAbout.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAbout.anchor = GridBagConstraints.SOUTH;
		gbc_btnAbout.gridx = 2;
		gbc_btnAbout.gridy = 8;
		settingsPanel.add(btnAbout, gbc_btnAbout);

		// TODO: Set Application Icon

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

		currentTileIndex = 0;
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
			changeCurrentTile();
		} else if (source.equals(backgroundColorTextField)) {
			updateTileColor();
		} else if (source.equals(btnOpenShortcutLocation)) {
			openShortcutLocation();
		} else if (source.equals(labelColorComboBox)) {
			changeLabelColor();
		} else if (source.equals(showLabelRadio)) {
			changeShowLabel();
		} else if (source.equals(btnApply)) {
			tiles.get(currentTileIndex).exportVisualElements();
		} else if (source.equals(btnRestoreDefault)) {
			tiles.get(currentTileIndex).removeVisualElements();
			changeCurrentTile();
		} else if (source.equals(btnChangeSize)) {
			changeIconSize();
		} else if (source.equals(btnImageSelect70)) {
			changeImage70();
		} else if (source.equals(btnImageSelect150)) {
			changeImage150();
		} else if (source.equals(customImageRadio)) {
			changeCustomImage();
		}
	}

	private void changeIconSize() {
		tilePreviewPanel.switchIconSize();
		if (tilePreviewPanel.isSmallIcon()) {
			btnChangeSize.setIcon(maxIcon);
			btnChangeSize.setRolloverIcon(maxRollIcon);
		} else {
			btnChangeSize.setIcon(minIcon);
			btnChangeSize.setRolloverIcon(minRollIcon);
		}
	}

	private void changeImage150() {
		try {
			JFileChooser fc = new JFileChooser();

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg");
			fc.setFileFilter(filter);

			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				BufferedImage image150 = ImageIO.read(fc.getSelectedFile());
				tiles.get(currentTileIndex).setImage150(image150);
			}
		} catch (Exception e) {
			// TODO: Handle image upload error
		}
		tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));
	}

	private void changeImage70() {
		try {
			JFileChooser fc = new JFileChooser();

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png", "gif", "jpeg");
			fc.setFileFilter(filter);

			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				BufferedImage image70 = ImageIO.read(fc.getSelectedFile());
				tiles.get(currentTileIndex).setImage70(image70);
			}
		} catch (Exception e) {
			// TODO: Handle image upload error
		}
		tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));
	}

	private void changeCustomImage() {
		boolean customImage = customImageRadio.isSelected();
		btnImageSelect150.setEnabled(customImage);
		btnImageSelect70.setEnabled(customImage);
		tiles.get(currentTileIndex).setCustomImage(customImage);
		tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));

	}

	private void changeShowLabel() {
		boolean showLabel = showLabelRadio.isSelected();
		labelColorComboBox.setEnabled(showLabel);
		tiles.get(currentTileIndex).setShowLabel(showLabel);
		tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));
	}

	private void changeLabelColor() {
		boolean isLabelLight = labelColorComboBox.getSelectedItem().equals("light");
		tiles.get(currentTileIndex).setIsLabelLight(isLabelLight);
		tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));
	}

	private void openShortcutLocation() {
		try {
			String shortcutPath = tiles.get(currentTileIndex).getShorcut().getAbsolutePath();
			Runtime.getRuntime().exec("explorer.exe /select," + shortcutPath);
		} catch (Exception e) {
			// TODO: Handle IOException
		}
	}

	private void changeCurrentTile() {
		currentTileIndex = programComboBox.getSelectedIndex();

		WindowsTile t = tiles.get(currentTileIndex);

		lblApplicationName.setText(t.getName());

		Color c = t.getBackgroundColor();
		String colorStr = String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
		backgroundColorTextField.setText(colorStr);

		if (t.isLabelLight()) {
			labelColorComboBox.setSelectedIndex(0);
		} else {
			labelColorComboBox.setSelectedIndex(1);
		}
		labelColorComboBox.setEnabled(t.getShowLabel());

		showLabelRadio.setSelected(t.getShowLabel());

		tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));

		customImageRadio.setSelected(t.getCustomImage());
		btnImageSelect150.setEnabled(t.getCustomImage());
		btnImageSelect70.setEnabled(t.getCustomImage());
	}

	private void updateTileColor() {
		String colorStr = backgroundColorTextField.getText();
		if (!colorStr.startsWith("#")) {
			colorStr = "#" + colorStr;
		}
		try {
			Color backgroundColor = Color.decode(colorStr);
			colorPreviewField.setBackground(backgroundColor);
			backgroundColorTextField.setBackground(Color.WHITE);
			tiles.get(currentTileIndex).setBackgroundColor(backgroundColor);
			tilePreviewPanel.renderPreview(tiles.get(currentTileIndex));
		} catch (Exception e) {
			backgroundColorTextField.setBackground(Color.PINK);
		}

	}

	@Override
	public void focusGained(FocusEvent e) {
		Object source = e.getSource();
		if (source.equals(backgroundColorTextField)) {
			backgroundColorTextField.selectAll();
		}

	}

	@Override
	public void focusLost(FocusEvent e) {
		Object source = e.getSource();
		if (source.equals(backgroundColorTextField)) {
			updateTileColor();
		}

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		Object source = e.getDocument().getProperty("owner");
		if (source.equals(backgroundColorTextField)) {
			updateTileColor();
		}

	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		Object source = e.getDocument().getProperty("owner");
		if (source.equals(backgroundColorTextField)) {
			updateTileColor();
		}

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		Object source = e.getDocument().getProperty("owner");
		if (source.equals(backgroundColorTextField)) {
			updateTileColor();
		}

	}
}
