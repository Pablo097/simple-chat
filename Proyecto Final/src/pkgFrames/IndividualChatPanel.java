package pkgFrames;

import pkgUtils.SmartScroller;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static pkgFrames.ChatClientFrame.createImageIcon;

public class IndividualChatPanel extends JPanel {
	private String username;
	private JTextPane chatTextPane;
	private JTextField writeMessageTextField;
	private JButton sendButton;
	private SimpleAttributeSet rightStyle, leftStyle;
	private SimpleAttributeSet rightBoldStyle, leftBoldStyle;
	private SimpleAttributeSet centredBoldStyle, centredTitleStyle;

	public IndividualChatPanel(String username) {
		super();
		this.username = username;
		createGUI();
	}

	private void createGUI() {
		setLayout(new BorderLayout(0,5));

		// Chat Panel
		JPanel centralPanel = new JPanel(new GridLayout(1,1));
		chatTextPane = new JTextPane();
		chatTextPane.setEditable(false);
		chatTextPane.setBackground(new Color(230, 230, 230));
		JScrollPane scrollPane = new JScrollPane(chatTextPane);
		scrollPane.setPreferredSize(new Dimension(400,300));
		new SmartScroller(scrollPane);
		centralPanel.add(scrollPane);

		// Write Message panel
		JPanel southPanel = new JPanel(new BorderLayout(5,0));
		writeMessageTextField = new JTextField(30);
		writeMessageTextField.setFont(new Font("Arial", Font.PLAIN, 13));
		writeMessageTextField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.DARK_GRAY, 1,true),
				BorderFactory.createEmptyBorder(0,5,0,5)
		));

		sendButton = new JButton();
		ImageIcon sendIcon = createImageIcon("images/send-button-blue.png");
		if (sendIcon != null) {
			Image img = sendIcon.getImage() ;
			Image newimg = img.getScaledInstance(20,20, Image.SCALE_SMOOTH) ;
			sendIcon = new ImageIcon(newimg);
			sendButton.setIcon(sendIcon);
			sendButton.setContentAreaFilled(false);
			sendButton.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		} else {
			sendButton.setText("SEND");
		}

		sendButton.setActionCommand("SEND");
		writeMessageTextField.addActionListener(e -> sendButton.doClick());

		southPanel.add(writeMessageTextField, BorderLayout.CENTER);
		southPanel.add(sendButton, BorderLayout.EAST);
		createStyles();

		StyledDocument doc = chatTextPane.getStyledDocument();
		try {
			int length = doc.getLength();
			doc.insertString(length, "Chat with " + username + "\n", centredTitleStyle);
			doc.setParagraphAttributes(length, doc.getLength()-length, centredTitleStyle, true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		add(centralPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void createStyles() {
		int fontSize = 12;
		int indent = 80;
		String fontFamily = "Arial";
		// Create a random accent color (trying not to be bluish) for this chat
		int r = (int)(Math.random()*250);
		int g = (int)(Math.random()*200);
		int b = (int)(Math.random()*60);

		rightStyle = new SimpleAttributeSet();
		StyleConstants.setAlignment(rightStyle, StyleConstants.ALIGN_RIGHT);
		StyleConstants.setLeftIndent(rightStyle, indent);
		StyleConstants.setFontSize(rightStyle,fontSize);
		StyleConstants.setSpaceAbove(rightStyle, 1);
		StyleConstants.setSpaceBelow(rightStyle, 4);
		StyleConstants.setFontFamily(rightStyle, fontFamily);
		rightBoldStyle = new SimpleAttributeSet(rightStyle);
		StyleConstants.setBold(rightBoldStyle, true);
		StyleConstants.setSpaceAbove(rightBoldStyle, 4);
		StyleConstants.setSpaceBelow(rightBoldStyle, 1);
		StyleConstants.setForeground(rightBoldStyle, new Color(30, 50, 140));

		leftStyle = new SimpleAttributeSet();
		StyleConstants.setAlignment(leftStyle, StyleConstants.ALIGN_LEFT);
		StyleConstants.setRightIndent(leftStyle, indent);
		StyleConstants.setFontSize(leftStyle, fontSize);
		StyleConstants.setSpaceAbove(leftStyle, 1);
		StyleConstants.setSpaceBelow(leftStyle, 4);
		StyleConstants.setFontFamily(leftStyle, fontFamily);
		leftBoldStyle = new SimpleAttributeSet(leftStyle);
		StyleConstants.setBold(leftBoldStyle, true);
		StyleConstants.setSpaceAbove(leftBoldStyle, 4);
		StyleConstants.setSpaceBelow(leftBoldStyle, 1);
		StyleConstants.setForeground(leftBoldStyle, new Color(r, g, b));

		centredBoldStyle = new SimpleAttributeSet();
		StyleConstants.setAlignment(centredBoldStyle, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontSize(centredBoldStyle, fontSize-1);
		StyleConstants.setSpaceAbove(centredBoldStyle, 4);
		StyleConstants.setSpaceBelow(centredBoldStyle, 4);
		StyleConstants.setFontFamily(centredBoldStyle, fontFamily);
		StyleConstants.setBold(centredBoldStyle, true);
		centredTitleStyle = new SimpleAttributeSet(centredBoldStyle);
		StyleConstants.setFontSize(centredTitleStyle, fontSize+1);
		StyleConstants.setForeground(centredTitleStyle, new Color(r, g, b));
	}

	public void writeOwnMessage(String message) {
		StyledDocument doc = chatTextPane.getStyledDocument();
		try {
			int length = doc.getLength();
			doc.insertString(length, "You wrote:\n", rightBoldStyle);
			doc.setParagraphAttributes(length, doc.getLength()-length, rightBoldStyle, true);
			length = doc.getLength();
			doc.insertString(length, message+"\n", rightStyle);
			doc.setParagraphAttributes(length, doc.getLength()-length, rightStyle, true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void writeOtherMessage(String message) {
		StyledDocument doc = chatTextPane.getStyledDocument();
		try {
			int length = doc.getLength();
			doc.insertString(length, username + " wrote:\n", leftBoldStyle);
			doc.setParagraphAttributes(length, doc.getLength()-length, leftBoldStyle, true);
			length = doc.getLength();
			doc.insertString(length, message+"\n", leftStyle);
			doc.setParagraphAttributes(length, doc.getLength()-length, leftStyle, true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void writeInfoText(String text) {
		StyledDocument doc = chatTextPane.getStyledDocument();
		try {
			int length = doc.getLength();
			doc.insertString(length, text+"\n", centredBoldStyle);
			doc.setParagraphAttributes(length, doc.getLength()-length, centredBoldStyle, true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void disablePanel() {
		sendButton.setEnabled(false);
		writeMessageTextField.setEnabled(false);
	}

	public void enablePanel() {
		sendButton.setEnabled(true);
		writeMessageTextField.setEnabled(true);
	}

	public String getUsername() {
		return username;
	}

	public String getWriteMessageText() {
		return writeMessageTextField.getText();
	}

	public void clearWriteMessageTextField() {
		writeMessageTextField.setText("");
	}

	public void addActionListener(ActionListener controller) {
		sendButton.addActionListener(controller);
	}
}
