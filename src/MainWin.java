import java.util.*;

import java.io.*;
import java.net.URL;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.imageio.ImageIO;

import processing.core.PApplet;

public class MainWin extends JFrame {	
	
	public static LatLonTable table;
	public static JTable jtable;
	
	public static JButton updateButton;
	
	public static ResourceBundle texts;
	
	private Locale language;
	
	private JPanel mainPanel;
	
	private JButton startButton, quitButton, saveButton, addButton, deleteButton, resetButton;
	
	private JLabel distLabel;
	
	private JTextField textLat, textLon;
	
	private JMenuBar menuBar;
			
	private boolean isFirstTimeToStart = true;

	public MainWin(Locale language, boolean isFirstTimeToStart) {
		table = new LatLonTable();
		texts = ResourceBundle.getBundle("TextsBundle", language);
		
		this.language = language;
		this.isFirstTimeToStart = isFirstTimeToStart;
		
		this.setTitle(texts.getString("titleMain"));
		setIconImage(new ImageIcon(getClass().getResource("/icons/Navigation.png")).getImage());
		
		int[] size = calculateSize();
		setSize(size[0], size[1]);
		
		setLocation(0, 0);
		setResizable(true);
		
		// create main panel
		createMainPanel();
		
		// create menus
		createMenus();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}
	
	public static void updateMainWin() {
		jtable.updateUI();
		updateButton.doClick();
	}
	
	private void createMainPanel() {
		// mainPanel
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		// ActionListener for buttons
		ListenForButtons alForButtons = new ListenForButtons();
		
		// part_1: controlPanel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 1));
		
		// part_1_1: welcomeLabel
		addLabel(controlPanel, texts.getString("lWelcome"), 21.0f, JLabel.CENTER);
		
		// part_1_2: controlButtonsPanel - controlButtons (start, quit & save)
		JPanel controlButtonsPanel = new JPanel();
		controlButtonsPanel.setLayout(new GridLayout(1, 3));
		
		String startButtonText = texts.getString("bStart");
		if (!isFirstTimeToStart) {
			startButtonText = texts.getString("bRestart");
		}
		
		startButton = addButton(controlButtonsPanel, startButtonText, alForButtons);
		quitButton = addButton(controlButtonsPanel, texts.getString("bQuit"), alForButtons);
		saveButton = addButton(controlButtonsPanel, texts.getString("bSave"), alForButtons);
		
		controlPanel.add(controlButtonsPanel);
		
		addComp(controlPanel, 0, 0, 1, 1, this.getWidth(), this.getHeight() / 7);
				
		// part_2: tablePanel
		jtable = new JTable(table);
		jtable.setToolTipText(texts.getString("tipTable"));
		
        JScrollPane tablePanel = new JScrollPane(jtable);
        table.setParentPanle(tablePanel);
        
		addComp(tablePanel, 0, 1, 1, 1, this.getWidth(), this.getHeight() / 3 * 2);

		// part_3: distPanel
		JPanel distPanel = new JPanel();
		distPanel.setToolTipText(texts.getString("tipUpdate"));
		
		distLabel = addLabel(distPanel, texts.getString("lDistSum") + table.getDistanceSum() + " (km)");
		updateButton = addButton(distPanel, texts.getString("bUpdate"), alForButtons);
		
		addComp(distPanel, 0, 2, 1, 1, this.getWidth(), this.getHeight() / 8);
	
		// part_4: operatePanel
		JPanel operatePanel = new JPanel();
		operatePanel.setLayout(new GridLayout(3, 1));
		
		// part_4_1: inputPanel
		JPanel inputPanel = new JPanel();
		
		addLabel(inputPanel, texts.getString("lInput"), 14.0f, JLabel.LEFT);
		
		textLat = addTextField(inputPanel, texts.getString("tLan"), 8);
		textLon = addTextField(inputPanel, texts.getString("tLon"), 8);
		
		operatePanel.add(inputPanel);
		
		// part_4_2: remindLabel
		addLabel(operatePanel, "[" + texts.getString("eg") + "23=23°N, -23=23°S, 67=67°E, -67=67°W]", 12.0f, JLabel.CENTER);
		
		// part_4_3: operateButtonsPanel - operateButtons (add, delete & reset)
		JPanel operateButtonsPanel = new JPanel();
		operateButtonsPanel.setLayout(new GridLayout(1, 3));
		
		addButton = addButton(operateButtonsPanel, texts.getString("bAdd"), alForButtons);
		deleteButton = addButton(operateButtonsPanel, texts.getString("bDelete"), alForButtons);
		resetButton = addButton(operateButtonsPanel, texts.getString("bReset"), alForButtons);
		
		operatePanel.add(operateButtonsPanel);
		
		addComp(operatePanel, 0, 3, 1, 1, this.getWidth(), this.getHeight() / 8);
		
		this.add(mainPanel);		
	}

	private JButton addButton(JPanel panel, String text, ActionListener al) {
		JButton button = new JButton(text);
		button.addActionListener(al);
		panel.add(button);
		
		return button;
	}
	
	private void addLabel(JPanel panel, String text, float fontSize, int horizontalAlignment) {
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(fontSize));
		label.setHorizontalAlignment(horizontalAlignment);
		
		panel.add(label);
	}
	
	private JLabel addLabel(JPanel panel, String text) {
		JLabel label = new JLabel(text);
		panel.add(label);
		
		return label;
	}
	
	private JTextField addTextField(JPanel panel, String text, int columns) {
		JTextField tf = new JTextField(text, columns);
		panel.add(tf);
		
		return tf;
	}
	
	private void addComp(JComponent comp, int gx, int gy, int gw, int gh, int ipadX, int ipadY) {
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx = gx;
		gridConstraints.gridy = gy;
		
		gridConstraints.gridwidth = gw;
		gridConstraints.gridheight = gh;
		
		gridConstraints.ipadx = ipadX;
		gridConstraints.ipady = ipadY;
		
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		
		gridConstraints.fill = GridBagConstraints.VERTICAL;
		gridConstraints.anchor = GridBagConstraints.CENTER;
		gridConstraints.insets = new Insets(10, 15, 10, 15);
		
		mainPanel.add(comp, gridConstraints); 
	}
	
	private void createMenus() {
		menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        // main menus
        String[] mainMenuKeys = {"mNaviApp", "mFile", "mTools", "mHelp"};
        
        // menu items
        String[][] menuItemKeys = {{"mIntroduction", "mAboutUS", "mRestart", "mQuit"}, {"mSave", "mOpen", "mRefresh"}, {"mSpeedTime"}, {"mUserGuide", "mContactUs"}};
        
        ListenForMenuItems alForMenuItems = new ListenForMenuItems();
        
        for (int i = 0; i < mainMenuKeys.length; i++) {
        	menuBar.add(new JMenu(texts.getString(mainMenuKeys[i])));
        	addMenuItems(menuBar.getMenu(i), menuItemKeys[i], alForMenuItems);
        }
        
        // add language sub-menu
        addLanguageSubMenu(menuBar.getMenu(2), 0, alForMenuItems);        
        
        menuBar.setVisible(true);
	}
	
	private void addMenuItems(JMenu menu, String[] itemKeys, ListenForMenuItems alForMenuItems) {
		for (String itemKey : itemKeys) {
			String iconName = "/icons/" + itemKey + ".png";
			
			JMenuItem item = new JMenuItem("  " + texts.getString(itemKey), new ImageIcon(getClass().getResource(iconName)));
			item.addActionListener(alForMenuItems);
			
			menu.add(item);
			menu.addSeparator();
		}
		
		menu.remove(menu.getItemCount() - 1);
	}
	
	private void addLanguageSubMenu(JMenu parent, int pos, ListenForMenuItems alForMenuItems) {
		String[] languageKeys = {"mLanEnglish", "mLanChinese"};
		
		JMenu languageMenu = new JMenu("  " + texts.getString("mLanguages"));
        languageMenu.setIcon(new ImageIcon(getClass().getResource("/icons/mLanguages.png")));
                
        for (String lanKey : languageKeys) {
        	JMenuItem item = new JMenuItem(texts.getString(lanKey));
        	item.addActionListener(alForMenuItems);
        	
        	languageMenu.add(item);
        	languageMenu.addSeparator();
        }
        
        languageMenu.remove(languageMenu.getItemCount() - 1);
        
        parent.add(languageMenu, pos);
        parent.insertSeparator(1);
	}
	
	private int[] calculateSize() {
		int[] size = new int[2];
		
		try {
			BufferedImage img = ImageIO.read(getClass().getResource("/images/Mercator_projection_SW.jpg"));
			
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension dm = tk.getScreenSize();
			
			double ratio = Math.min(1.0f * dm.getHeight() / img.getHeight(), 0.8f * dm.getWidth() / img.getWidth());
			
			size[0] = (int) (dm.getWidth() - img.getWidth() * ratio);
			size[1] = (int) (img.getHeight() * ratio) + 1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return size;
	}
	
	class ListenForButtons implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == startButton) {
				if (isFirstTimeToStart) {
					PApplet.main("CourseWin");
					
					isFirstTimeToStart = false;
					startButton.setText(texts.getString("bRestart"));
					
				} else {
					LatLonTable.points.clear();
					
					updateMainWin();
					
					textLat.setText(texts.getString("tLan"));
					textLon.setText(texts.getString("tLon"));
				}
			}
			
			if (e.getSource() == quitButton) {
				System.exit(0);
			}
			
			if (e.getSource() == saveButton) {
				FileDialog fd = new FileDialog(MainWin.this, texts.getString("fSaveTitle"), FileDialog.SAVE);				
				
				fd.setFile(texts.getString("fName"));
				fd.setVisible(true);
				
				if (fd.getFile() != null) {
					String fileName = fd.getDirectory() + fd.getFile();
					
			        if (table.writeToFile(fileName)) {
			        	JOptionPane.showMessageDialog(MainWin.this, texts.getString("fSuccessInfo") + fileName + "\"!", texts.getString("fSuccess"), JOptionPane.INFORMATION_MESSAGE);
			        } else {
			        	JOptionPane.showMessageDialog(MainWin.this, texts.getString("pErrorInfo"), texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
			        }
				}
				
			}
			
			if (e.getSource() == updateButton) {
				LatLonTable.updateDistance();
				distLabel.setText(texts.getString("lDistSum") + table.getDistanceSum() + " (km)");
			}
			
			if (e.getSource() == addButton) {
				double dlat = table.getInputData(textLat.getText(), 0);
				double dlon = table.getInputData(textLon.getText(), 1);

				if (dlat == 999) {
					textLat.requestFocus();
				} else if (dlon == 999) {
					textLon.requestFocus();
				} else {
					table.add(new Point(dlat, dlon, "degree"));
				}
				
				updateButton.doClick();
			}
			
			if (e.getSource() == deleteButton) {
				int[] rows = jtable.getSelectedRows();
				
				for (int i = rows.length - 1; i >= 0; i--) {
					table.remove(rows[i]);
				}
				
				updateButton.doClick();
			}
			
			if (e.getSource() == resetButton) {
				textLat.setText(texts.getString("tLan"));
				textLon.setText(texts.getString("tLon"));
			}
		}
	}
	
	class ListenForMenuItems implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Menu "Navigation App"
			// menuItem Introduction
			if (e.getSource() == menuBar.getMenu(0).getItem(0)) {
				try {
				    Desktop.getDesktop().browse(new URL("https://sites.google.com/view/letsnavigateapp/introduction?authuser=0").toURI());
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(MainWin.this, texts.getString("pErrorInfo"), texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
				}
			}
			
			// menuItem About Us
			if (e.getSource() == menuBar.getMenu(0).getItem(2)) {
				try {
				    Desktop.getDesktop().browse(new URL("https://sites.google.com/view/letsnavigateapp/about-us?authuser=0").toURI());
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(MainWin.this, texts.getString("pErrorInfo"), texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
				}
			}
			
			// menuItem Restart
			if (e.getSource() == menuBar.getMenu(0).getItem(4)) {
				startButton.doClick();
			}
						
			// menuItem Quit
			if (e.getSource() == menuBar.getMenu(0).getItem(6)) {
				quitButton.doClick();
			}
			
			// Menu "File"
			// menuItem Save
			if (e.getSource() == menuBar.getMenu(1).getItem(0)) {
				saveButton.doClick();
			}
			
			// menuItem Open
			if (e.getSource() == menuBar.getMenu(1).getItem(2)) {
				FileDialog fd = new FileDialog(MainWin.this, texts.getString("fOpenTitle"), FileDialog.LOAD);				
				
				fd.setFilenameFilter(new FilenameFilter() {
				    @Override
				    public boolean accept(File dir, String name) {
				        return name.endsWith(".txt");
				    }
				});
				
				fd.setVisible(true);
				
				if (fd.getFile() != null) {
					table.readFromFile(fd.getDirectory() + fd.getFile());    
				}
			      
				updateMainWin();
			}
			
			// menuItem Refresh
			if (e.getSource() == menuBar.getMenu(1).getItem(4)) {
				updateMainWin();
			}
						
			// Menu "Tools"
			// sub-menu Languages - English
			if (e.getSource() == ((JMenu) menuBar.getMenu(2).getItem(0)).getItem(0)) {
				if (!language.toString().equals("en")) {
					setVisible(false);
					dispose();
					
					new MainWin(Locale.ENGLISH, isFirstTimeToStart);
					updateMainWin();
				}
			}
			
			// sub-menu Languages - Chinese
			if (e.getSource() == ((JMenu) menuBar.getMenu(2).getItem(0)).getItem(2)) {
				if (!language.toString().equals("zh")) {
					setVisible(false);
					dispose();

					new MainWin(Locale.CHINESE, isFirstTimeToStart);
					updateMainWin();
				}
			}
			
			// menuItem Speed & Time
			if (e.getSource() == menuBar.getMenu(2).getItem(2)) {
				String inputSpeed = JOptionPane.showInputDialog(MainWin.this, texts.getString("pSpeedInputMes"), texts.getString("pSpeedUnit"));
				
				try {
					double speed = Double.parseDouble(inputSpeed);
					double time = (LatLonTable.distanceSum / 1.852) / speed;
					
					JOptionPane.showMessageDialog(MainWin.this, texts.getString("pTimeMes") + String.format("%.3f", time) + texts.getString("pTimeUnit"), texts.getString("pTime"), JOptionPane.INFORMATION_MESSAGE);
					
				} catch (NumberFormatException exp) {
					JOptionPane.showMessageDialog(MainWin.this, texts.getString("pNumberInfo"), texts.getString("pNumber"), JOptionPane.WARNING_MESSAGE);
				}
			}
			
			// Menu "Help"
			// menuItem User Guide
			if (e.getSource() == menuBar.getMenu(3).getItem(0)) {
				try {
				    Desktop.getDesktop().browse(new URL("https://sites.google.com/view/letsnavigateapp/user-guide?authuser=0").toURI());
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(MainWin.this, texts.getString("pErrorInfo"), texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
				}
			}
			
			// menuItem Contact Us
			if (e.getSource() == menuBar.getMenu(3).getItem(2)) {
				try {
				    Desktop.getDesktop().browse(new URL("https://sites.google.com/view/letsnavigateapp/home?authuser=0").toURI());
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(MainWin.this, texts.getString("pErrorInfo"), texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
				}
			}
		}	
	}

}
