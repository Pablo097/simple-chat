package pkgFrames;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */ 
public class ButtonTabComponent extends JPanel {
	private final JLabel iconLabel;
    private TabButton closeButton;

    public ButtonTabComponent(String title, Icon icon) {
        //unset default FlowLayout' gaps
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setOpaque(false);
		// Title Label
		JLabel titleLabel = new JLabel(title);
		add(titleLabel);
		// Icon Label
		iconLabel = new JLabel(icon);
		setIconVisible(false);
        add(iconLabel);
        //add more space between the iconLabel and the titleLabel and button
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        // Tab Button
        closeButton = new TabButton();
        add(closeButton);
        //add more space to the top of the component
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    public void addButtonActionListener(ActionListener listener) {
    	closeButton.addActionListener(listener);
	}

	public void setIconVisible(boolean flag) {
    	iconLabel.setVisible(flag);
	}

    private class TabButton extends JButton {
        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("Close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setBorderPainted(true);
                }

                public void mouseExited(MouseEvent e) {
                    setBorderPainted(false);
                }
            });
            setRolloverEnabled(true);
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            if (getModel().isRollover()) {
                g2.setColor(Color.MAGENTA);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }
}


