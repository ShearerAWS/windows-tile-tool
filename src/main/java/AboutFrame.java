package main.java;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

public class AboutFrame extends JFrame implements ActionListener {

	private final String version = "v0.1.1";

	private JPanel contentPane;
	private JButton btnViewSourceCode, btnReportAnIssue, btnLogo;

	public AboutFrame() {
		setUpGUI();
	}

	private void setUpGUI() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 437, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel headerPanel = new JPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		headerPanel.setLayout(new BorderLayout(0, 0));

		btnLogo = new JButton("");
		btnLogo.setBackground(SystemColor.menu);
		btnLogo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		btnLogo = new JButton("");
		btnLogo.setBorderPainted(false);
		btnLogo.setBorder(null);
		btnLogo.setFocusable(false);
		btnLogo.setMargin(new Insets(0, 0, 0, 0));
		btnLogo.setContentAreaFilled(false);
		btnLogo.setIcon(new ImageIcon(AboutFrame.class.getResource("/main/resources/AboutLogo.png")));
		btnLogo.addActionListener(this);
		headerPanel.add(btnLogo, BorderLayout.WEST);

		JPanel titlePanel = new JPanel();
		headerPanel.add(titlePanel, BorderLayout.CENTER);
		titlePanel.setLayout(new BorderLayout(0, 0));

		JLabel lblTitle = new JLabel("Windows Tile Tool");
		titlePanel.add(lblTitle, BorderLayout.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 24));

		JLabel lblSubtitle = new JLabel(version);
		titlePanel.add(lblSubtitle, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		btnViewSourceCode = new JButton("View Source Code");
		buttonPanel.add(btnViewSourceCode);
		btnViewSourceCode.addActionListener(this);

		btnReportAnIssue = new JButton("Report an Issue");
		buttonPanel.add(btnReportAnIssue);
		btnReportAnIssue.addActionListener(this);

		JScrollPane aboutTextScrollPane = new JScrollPane();
		aboutTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(aboutTextScrollPane, BorderLayout.CENTER);

		String description = "The Windows Tile Tool is a free open source tool that allows users to easily customize "
				+ "their Windows start menu tiles.";

		String text = "<html><font face='Tahoma' size='4'>" + description + "<p>Created by: Alexander Shearer</p>"
				+ "<p>This application was wrapped using Launch4j</p>"
				+ "<p>This uses the WindowsShortcut Class created by codebling, which can be found at "
				+ "<a href='https://github.com/codebling/WindowsShortcuts'>github.com/codebling/WindowsShortcuts</a</p>"
				+ "<p>Current Version: " + version + "</p><p>This software is licenced as follows: </p>"
				+ "<p>The MIT License (MIT)</p>" + "<p>Copyright (c) 2017 Alexander S.</p>"
				+ "<p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and "
				+ "associated documentation files (the \"Software\"), to deal in the Software without restriction, "
				+ "including without limitation the rights to use, copy, modify, merge, publish, distribute, "
				+ "sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is "
				+ "furnished to do so, subject to the following conditions:</p>"
				+ "<p>The above copyright notice and this permission notice shall be included in all copies or "
				+ "substantial portions of the Software.</p>"
				+ "<p>THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING "
				+ "BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND "
				+ "NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, "
				+ "DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT "
				+ "OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.</p>"
				+ "</font></html>";

		JEditorPane aboutText = new JEditorPane(new HTMLEditorKit().getContentType(), text);

		aboutText.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (Exception e1) {
						// TODO Handle Exception
					}
				}
			}
		});

		aboutText.setOpaque(false);
		aboutText.setBorder(null);
		aboutText.setEditable(false);
		aboutText.setCaretPosition(0);

		aboutTextScrollPane.setViewportView(aboutText);

		ImageIcon icon = new ImageIcon("src/main/resources/icon.png");
		setIconImage(icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnViewSourceCode || source == btnLogo) {
			try {
				Desktop.getDesktop().browse(new URL("https://github.com/ShearerAWS/WindowsTileTool").toURI());
			} catch (Exception e1) {
				// TODO Handle Exception
			}
		} else if (source == btnReportAnIssue) {
			try {
				Desktop.getDesktop()
						.browse(new URL("https://github.com/ShearerAWS/WindowsTileTool/issues/new").toURI());
			} catch (Exception e1) {
				// TODO Handle Exception
			}
		}
	}
}
