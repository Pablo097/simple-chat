package pkgFrames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientInitializer extends JFrame implements ActionListener {
	private String hostname;
	private int port;
	private JTextField textField, hostnameField;
	private boolean disposeAfterStart;

	// If @hostname is null, a panel for introducing the hostname is displayed
	public ClientInitializer(String hostname, int port, boolean disposeAfterStart) {
		super("Client Initializer");
		this.hostname = hostname;
		this.port = port;
		this.disposeAfterStart = disposeAfterStart;
		SwingUtilities.invokeLater(this::createGUI);
	}

	private void createGUI() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Label
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		JLabel label = new JLabel("Insert new client's name (max 30 characters without spaces)");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(label);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		// Text Field
		textField = new JTextField(40);
		textField.setHorizontalAlignment(JTextField.CENTER);
		textField.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(textField);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		// hostname Panel
		if (hostname == null) {
			JPanel hostnamePanel = new JPanel(new FlowLayout());
			JLabel hostnameLabel = new JLabel("hostname: ");
			hostnamePanel.add(hostnameLabel);
			hostnameField = new JTextField(20);
			hostnamePanel.add(hostnameField);
			panel.add(hostnamePanel);
			panel.add(Box.createRigidArea(new Dimension(0,5)));
		}
		// Button
		JButton button = new JButton("Start client");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);
		panel.add(Box.createRigidArea(new Dimension(0,5)));
		// Add listener
		button.addActionListener(this);
		textField.addActionListener(this);

		add(panel);

		// Show frame
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(400, 400);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String name = textField.getText();
		if (!name.isEmpty()) {
			if (!name.contains(" ") && name.length()<30) {
				if (hostname == null) {
					hostname = hostnameField.getText();
				}
				new ChatClientFrame(name, hostname, port, 100, 100);
				if (disposeAfterStart) this.dispose();
			}
			textField.setText("");
		}
	}
}
