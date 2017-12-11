package main.java;

import java.awt.Toolkit;

import javax.swing.JOptionPane;

public class Driver {

	private final static String[] allowedOS = { "Windows 10", "Windows 8" };

	public static void main(String[] args) {
		if (!isAllowedOS(System.getProperty("os.name"))) {
			Toolkit.getDefaultToolkit().beep();
			Object[] options = { "Exit", "Continue" };
			int input = JOptionPane.showOptionDialog(null,
					"This application is not intended for your system. Continuing is not \nrecommended, as it may result in unpredictable behaviour",
					"Windows Tile Tool", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
					options[0]);
			if (input != 1) {
				return;
			}
		}

		WindowsTileTool gui = new WindowsTileTool();
		gui.setVisible(true);
	}

	private static boolean isAllowedOS(String userOS) {
		boolean allowed = false;
		for (String OS : allowedOS) {
			if (OS.equals(userOS)) {
				allowed = true;
			}
		}
		return allowed;
	}
}
