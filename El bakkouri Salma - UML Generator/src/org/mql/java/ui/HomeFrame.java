package org.mql.java.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import org.mql.java.models.ProjectModel;
import org.mql.java.services.Scanner;

public class HomeFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private String imagePath = "resources/img.png";

	public HomeFrame() {
		setTitle("UML Generator");
		setSize(800, 550);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout(30, 15));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 50));

		JLabel welcomeLabel = new JLabel("Welcome to UML Generator", SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
		welcomeLabel.setForeground(new Color(0x3C2F64));
		centerPanel.add(welcomeLabel, BorderLayout.NORTH);

		JLabel subtitleLabel = new JLabel("Load, Generate & Export", SwingConstants.CENTER);
		subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		subtitleLabel.setForeground(new Color(0x676767));
		centerPanel.add(subtitleLabel, BorderLayout.CENTER);

		JPanel inputButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
		inputButtonPanel.setBackground(Color.WHITE);

		JButton uploadButton = new JButton("UPLOAD");
		uploadButton.setPreferredSize(new Dimension(200, 40));
		uploadButton.setFont(new Font("Arial", Font.BOLD, 14));
		uploadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		uploadButton.setFocusPainted(false);
		inputButtonPanel.add(uploadButton);

		JButton scanButton = new JButton("SCAN PROJECT");
		scanButton.setBackground(Color.BLACK);
		scanButton.setForeground(Color.WHITE);
		scanButton.setFont(new Font("Arial", Font.BOLD, 14));
		scanButton.setPreferredSize(new Dimension(170, 40));
		scanButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		scanButton.setFocusPainted(false);
		inputButtonPanel.add(scanButton);

		centerPanel.add(inputButtonPanel, BorderLayout.SOUTH);

		JPanel imagePanel = new JPanel(new BorderLayout());
		imagePanel.setBackground(Color.WHITE);

		ImageIcon imageIcon = new ImageIcon(imagePath);
		if (imageIcon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
			Image image = imageIcon.getImage();
			Image scaledImage = image.getScaledInstance(400, 200, Image.SCALE_SMOOTH);
			ImageIcon scaledIcon = new ImageIcon(scaledImage);

			JLabel imageLabel = new JLabel(scaledIcon);
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imagePanel.add(imageLabel, BorderLayout.CENTER);
		} else {
			System.err.println("Image not found or failed to load: " + imagePath);
		}

		mainPanel.add(centerPanel, BorderLayout.NORTH);
		mainPanel.add(imagePanel, BorderLayout.CENTER);

		add(mainPanel);

		uploadButton.addActionListener(e -> uploadFolder());
		scanButton.addActionListener(e -> {
			try {
				parseProject();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		});

		setVisible(true);
		setLocationRelativeTo(null);
	}

	String path;

	private void uploadFolder() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFolder = fileChooser.getSelectedFile();
			path = selectedFolder.getAbsolutePath();
		}
	}

	private void parseProject() throws ClassNotFoundException {
	    if (path == null || path.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please select a project folder.", "Error", JOptionPane.ERROR_MESSAGE);
	    } else {
	        int confirm = JOptionPane.showConfirmDialog(
	            this, 
	            "Are you sure you want to parse the project at:\n" + path + "?", 
	            "Confirm Parsing", 
	            JOptionPane.YES_NO_OPTION
	        );

	        if (confirm == JOptionPane.YES_OPTION) {
	            new UMLDisplayFrame(path);
	        }
	    }
	}

	public static void main(String[] args) {
		UIManager.put("Button.focus", UIManager.get("Button.background"));
		new HomeFrame();
	}
}