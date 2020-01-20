package pkgFrames;
import pkgControl.ServerController;
import pkgServers.MultiThreadServer;
import pkgUtils.SmartScroller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends JFrame{
	private final int posX;
	private final int posY;
	private final MultiThreadServer server;
	// Central Panel
	private JTextArea centralTextArea;
	// Button Panel
	private JPanel buttonPanel;
	private JButton clearButton;
	private JButton exitButton;

	public ServerFrame(String title, MultiThreadServer server, int posX, int posY) {
		super(title);
		this.server = server;
		this.posX = posX;
		this.posY = posY;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					createGUI();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createGUI() {
		setLayout(new BorderLayout());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				server.interrupt();
				e.getWindow().dispose();
			}
		});

		// Central Panel
		centralTextArea = new JTextArea("", 30, 70);
		centralTextArea.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(centralTextArea);
		new SmartScroller(scrollPane);
		centralTextArea.setEditable(false);
		this.add(scrollPane, BorderLayout.CENTER);

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

		// Associate controller
		ServerController controller = new ServerController(this, server);
		clearButton.addActionListener(controller);
		exitButton.addActionListener(controller);

		// Show frame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(posX, posY);
		pack();
		setVisible(true);

	}

	public void clearTextArea() {
		centralTextArea.setText(null);
	}

	public void appendTextArea(String s) {
		centralTextArea.append(s + "\n");
	}
}
