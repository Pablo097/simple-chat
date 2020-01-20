package pkgFrames;
import pkgControl.SumClientController;

import javax.swing.*;
import java.awt.*;

public class SumClientFrame extends JFrame {
    public final String hostname;
    public final int port;
    private final int posX, posY;
    // Sum Panel
    private JPanel sumPanel;
    private JLabel sumLabel;
    private JTextField num1TextField, num2TextField;
    private JButton sumButton;
    private JTextField resultTextField;
    // Central Panel
    private JTextArea centralTextArea;
    // Button Panel
    private JPanel buttonPanel;
    private JButton clearButton;
    private JButton exitButton;

    public SumClientFrame(String title, String hostname, int port, int posX, int posY) {
        super(title);
        this.hostname = hostname;
        this.port = port;
        this.posX = posX;
        this.posY = posY;
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() { createGUI(); }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        setLayout(new BorderLayout());

        // Sum Panel
        sumPanel = new JPanel(new FlowLayout());
        num1TextField = new JTextField(10);
        num1TextField.setEditable(true);
        num2TextField = new JTextField(10);
        num2TextField.setEditable(true);
        resultTextField = new JTextField(10);
        resultTextField.setEditable(false);
        resultTextField.setBackground(Color.WHITE);
        sumLabel = new JLabel(" + ");
        sumButton = new JButton("=");

        sumPanel.add(num1TextField);
        sumPanel.add(sumLabel);
        sumPanel.add(num2TextField);
        sumPanel.add(sumButton);
        sumPanel.add(resultTextField);
        this.add(sumPanel, BorderLayout.NORTH);

        // Central Area
        centralTextArea = new JTextArea("", 10, 50);
        centralTextArea.setBackground(Color.WHITE);
        // centralTextArea.setFont(new Font("Serif", Font.BOLD, 16));
        JScrollPane scrollPane = new JScrollPane(centralTextArea);
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
        sumButton.setActionCommand("SUM");
        clearButton.setActionCommand("CLEAR");
        exitButton.setActionCommand("EXIT");

        // Associate controller
        SumClientController controller = new SumClientController(this);
        sumButton.addActionListener(controller);
        clearButton.addActionListener(controller);
        exitButton.addActionListener(controller);

        // Show frame
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocation(posX, posY);
        pack();
        setVisible(true);
    }

    public String getNum1Text() {
        return num1TextField.getText();
    }

    public String getNum2Text() {
        return num2TextField.getText();
    }

    public void setResultText(String s) {
        resultTextField.setText(s);
    }

    public void clearAllTexts() {
        num1TextField.setText("");
        num2TextField.setText("");
        resultTextField.setText("");
    }

    public void clearTextArea() {
        centralTextArea.setText(null);
    }

    public void appendTextArea(String s) {
        centralTextArea.append(s + "\n");
    }
}
