package pkgFrames;
import pkgControl.ChatClientController;
import pkgControl.ChatClientWorker;
import pkgUtils.SmartScroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatClientFrame extends JFrame {
	private final String hostname;
	private final int port;
	private final String username;
	private final int posX, posY;
	private ChatClientController controller;
	//	private ChatClient client;
	private LinkedBlockingQueue<String> clientQueue;
	private ChatClientWorker chatClientWorker;
	// Central Panel
	private JPanel centralPanel;
	// Tabbed Pane
	private JTabbedPane tabbedPane;
	private JPanel connectedHubPanel, disconnectedHubPanel;
	private JList<String> activeUsersList;
	private JButton refreshButton;
	private JButton startChatButton;
	private JButton disconnectButton;
	private JButton connectButton;
	private List<IndividualChatPanel> chatPanels;
	// Log Area
	private JTextArea logTextArea;
	// Button Panel
	private JPanel buttonPanel;
	private JButton clearButton;
	private JButton exitButton;

	public ChatClientFrame(String username, String hostname, int port, int posX, int posY) {
		super("Bad Designed Chat Client - " + username);
		this.username = username;
		this.hostname = hostname;
		this.port = port;
		this.posX = posX;
		this.posY = posY;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { createGUI(); }
		});
	}

	private void createGUI() {
		setLayout(new BorderLayout());

		// Tabbed Panel
		tabbedPane = new JTabbedPane();
		connectedHubPanel = createConnectedHubPanel();
		disconnectedHubPanel = createDisconnectedHubPanel();
		tabbedPane.addTab("Hub Panel", disconnectedHubPanel);

		chatPanels = new ArrayList<>();
		// Add listener for hiding new notification icon when opening a chat tab
		tabbedPane.addChangeListener(e -> setNotificationIconVisibleForUsername(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),false));

		// Only enable startChat button when there is a user selected on the list
		activeUsersList.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				startChatButton.setEnabled(true);
			}
		});

		// Log Area
		logTextArea = new JTextArea("*** Log Text Area ***\n", 8, 50);
		logTextArea.setBackground(Color.WHITE);
		logTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(logTextArea);
		new SmartScroller(scrollPane);

		// Central Panel
		centralPanel = new JPanel(new BorderLayout());
		centralPanel.add(tabbedPane, BorderLayout.CENTER);
		centralPanel.add(scrollPane, BorderLayout.SOUTH);
		this.add(centralPanel, BorderLayout.CENTER);

		// Buttons Panel
		buttonPanel = new JPanel(new FlowLayout());
		clearButton = new JButton("CLEAR");
		exitButton = new JButton("EXIT");
		buttonPanel.add(clearButton);
		buttonPanel.add(exitButton);
		this.add(buttonPanel, BorderLayout.SOUTH);

		// Commands
		clearButton.setActionCommand("CLEAR");
		exitButton.setActionCommand("EXIT");
		startChatButton.setActionCommand("START");
		refreshButton.setActionCommand("REFRESH");
		disconnectButton.setActionCommand("DISCONNECT");
		connectButton.setActionCommand("CONNECT");

		// Associate controller
		controller = new ChatClientController(this);
		clearButton.addActionListener(controller);
		exitButton.addActionListener(controller);
		startChatButton.addActionListener(controller);
		refreshButton.addActionListener(controller);
		disconnectButton.addActionListener(controller);
		connectButton.addActionListener(controller);

		// Send disconnect command to server before closing, if needed
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (disconnectButton.isEnabled()) {
					try {
						clientQueue.put("DISCONNECT");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				e.getWindow().dispose();
			}
		});

		// Show frame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(posX, posY);
		pack();
		setVisible(true);
	}

	private JPanel createConnectedHubPanel() {
		JPanel panel = new JPanel(new BorderLayout());

		JTextArea title = new JTextArea(username + "'s Chat Session");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		title.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.5;
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(10,0,0,0);
		JPanel aux1 = new JPanel(new GridBagLayout());
		aux1.add(title,c);
		panel.add(aux1, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel(new GridBagLayout());
		panel.add(contentPanel, BorderLayout.CENTER);

		JLabel infoText1 = new JLabel("Select a contact to begin a chat with it:");
		infoText1.setFont(new Font("Arial", Font.PLAIN, 12));
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		c.weightx = 0.5;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.insets = new Insets(10,15,10,0);
		contentPanel.add(infoText1, c);

		startChatButton = new JButton("Start chat!");
		startChatButton.setFont(new Font("Arial", Font.BOLD, 13));
		startChatButton.setBackground(new Color(21, 129, 203));
		startChatButton.setEnabled(false);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weighty = 0.5;
		c.weightx = 0.5;
		c.ipady = 30;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.insets = new Insets(0,8,10,0);
		contentPanel.add(startChatButton, c);

		refreshButton = new JButton("Refresh Users");
		refreshButton.setFont(new Font("Arial", Font.PLAIN, 13));
		refreshButton.setEnabled(false);
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0,0,0,0);
		contentPanel.add(refreshButton, c);

		disconnectButton = new JButton("Disconnect");
		disconnectButton.setFont(new Font("Arial", Font.PLAIN, 13));
		disconnectButton.setBackground(new Color(226, 73, 46));
		disconnectButton.setEnabled(false);
		c.gridy = 3;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.insets = new Insets(10,7,0,0);
		contentPanel.add(disconnectButton, c);

		activeUsersList = new JList<>();
		activeUsersList.setFont(new Font("Arial", Font.PLAIN, 12));
		updateActiveUsersList(new DefaultListModel<String>());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 3;
		c.weighty = 0.5;
		c.weightx = 0.5;
		c.ipadx = 100;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(0,30,10,0);
		contentPanel.add(new JScrollPane(activeUsersList), c);

		panel.setPreferredSize(new Dimension(500, 400));
		return panel;
	}

	private JPanel createDisconnectedHubPanel() {
		JPanel panel = new JPanel(new GridBagLayout());

		connectButton = new JButton("Connect");
		connectButton.setFont(new Font("Arial", Font.PLAIN, 13));
		connectButton.setBackground(new Color(31, 226, 85));
		connectButton.setEnabled(true);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.5;
		c.ipady = 30;
		c.anchor = GridBagConstraints.CENTER;
		panel.add(connectButton, c);

		panel.setPreferredSize(new Dimension(500, 400));
		return panel;
	}

	public void startClient() {
		clientQueue = new LinkedBlockingQueue<>();
		chatClientWorker = new ChatClientWorker(this, username, hostname, port, clientQueue);
		chatClientWorker.execute();
	}

	public boolean isConnected() {
		return disconnectButton.isEnabled();
	}

	public void clearLogTextArea() {
		logTextArea.setText(null);
	}

	public void appendLogText(String s) {
		logTextArea.append(s + "\n");
	}

	public void updateActiveUsersList(DefaultListModel<String> list) {
		if (list.isEmpty()) {
			list.addElement("No users found yet");
			activeUsersList.setEnabled(false);
		} else {
			activeUsersList.setEnabled(true);
		}
		activeUsersList.setModel(list);
		startChatButton.setEnabled(false);
	}

	public void enableHub() {
		tabbedPane.setComponentAt(0, connectedHubPanel);
		connectedHubPanel.updateUI();
		connectButton.setEnabled(false);
		refreshButton.setEnabled(true);
		disconnectButton.setEnabled(true);
		refreshButton.doClick();
		for (IndividualChatPanel panel: chatPanels) {
			panel.enablePanel();
		}
	}

	public void disableHub() {
		tabbedPane.setComponentAt(0, disconnectedHubPanel);
		disconnectedHubPanel.updateUI();
		connectButton.setEnabled(true);
		refreshButton.setEnabled(false);
		startChatButton.setEnabled(false);
		disconnectButton.setEnabled(false);
		activeUsersList.setEnabled(false);
		for (IndividualChatPanel panel : chatPanels) {
			panel.disablePanel();
		}
	}

	public String getUsername() {
		return username;
	}

	public LinkedBlockingQueue<String> getClientQueue() {
		return clientQueue;
	}

	public String getSelectedUsernameOnList() {
		return activeUsersList.getSelectedValue();
	}

	// Returns the chat panel with user "username" if it exists, or creates it if it doesn't exist
	public IndividualChatPanel getChatPanel(String username) {
		IndividualChatPanel chatPanel = null;
		int index = tabbedPane.indexOfTab(username);
		if (index >= 0) {	// The tab already exists
			for(IndividualChatPanel panel: chatPanels) {
				if (panel.getUsername().equals(username)) {
					chatPanel = panel;
					break;
				}
			}
		} else {	// The tab doesn't exist, we need to create it
			chatPanel = new IndividualChatPanel(username);
			chatPanel.addActionListener(controller);
			// Add chatPanel to new tab and to control array
			tabbedPane.addTab(username, chatPanel);
			chatPanels.add(chatPanel);

			// Create tab component for displaying new message icon and close button
			ImageIcon icon = createImageIcon("images/asterisk.png");
			if (icon!=null) {
				icon = new ImageIcon(icon.getImage().getScaledInstance(10,10, Image.SCALE_SMOOTH));
			}
			ButtonTabComponent tabComponent = new ButtonTabComponent(username, icon);
			// Set actionListener for closeButton
			tabComponent.addButtonActionListener(new ActionListener() {
				private String user = username;
				@Override
				public void actionPerformed(ActionEvent e) {
					// Closes tab
					tabbedPane.remove(tabbedPane.indexOfTab(user));
					// Removes chatPanel from control array
					for (int i=0; i<chatPanels.size(); i++) {
						if (chatPanels.get(i).getUsername().equals(user)) {
							chatPanels.remove(i);
							break;
						}
					}
				}
			});
			// Set tab component for this tab
			tabbedPane.setTabComponentAt(tabbedPane.getTabCount()-1, tabComponent);
		}
		return chatPanel;
	}

	// Returns true if tab could be selected
	public boolean setChatTab(String username) {
		int index = tabbedPane.indexOfTab(username);
		if (index >= 0) {
			tabbedPane.setSelectedIndex(index);
			return true;
		}
		return false;
	}

	public String getChatTabTitle() {
		return tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
	}

	// Sets the visibility of the new notification icon in the username's chat tab if the chat exists
	public void setNotificationIconVisibleForUsername(String username, boolean flag){
		int index = tabbedPane.indexOfTab(username);
		if (index > 0) {	// If selected tab is a chat tab that exists
			ButtonTabComponent component = (ButtonTabComponent) tabbedPane.getTabComponentAt(index);
			component.setIconVisible(flag);
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = IndividualChatPanel.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
