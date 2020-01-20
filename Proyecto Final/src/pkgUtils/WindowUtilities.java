package pkgUtils;

import javax.swing.*;
import javax.swing.UIManager.*;
import java.awt.*;   // For Color and Container classes.

/**
 * A few utilities that simplify using windows in Swing. 
 */

public class WindowUtilities {

  /** Tell system to use native look and feel.
   *  Metal (Java) LAF is the default otherwise.
   *  Nimbus is far superior to Metal!
   */

  public static void setNativeLookAndFeel() {
    try {
     UIManager.setLookAndFeel(
       UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting native LAF: " + e);
    }
  }

  public static void setJavaLookAndFeel() {
    try {
     UIManager.setLookAndFeel(
       UIManager.getCrossPlatformLookAndFeelClassName());
    } catch(Exception e) {
      System.out.println("Error setting Java LAF: " + e);
    }
  }

  public static void setNimbusLookAndFeel() {
    try {
      LookAndFeelInfo[] lafs =
        UIManager.getInstalledLookAndFeels();
      for (LookAndFeelInfo laf: lafs) {
        if ("Nimbus".equals(laf.getName())) {
          UIManager.setLookAndFeel(laf.getClassName());
        }
      } 
    } catch(Exception e) {
      System.out.println("Error setting Nimbus LAF: " + e);
    }
  }
  
  public static void setMotifLookAndFeel() {
    try {
      UIManager.setLookAndFeel(
        "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
    } catch(Exception e) {
      System.out.println("Error setting Motif LAF: " + e);
    }
  }

  /** A simplified way to see a JPanel or other Container. Pops
   *  up a JFrame with specified Container as the content pane.
   */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height,
                                    String title,
                                    Color bgColor) {
    JFrame frame = new JFrame(title);
    frame.setBackground(bgColor);
    content.setBackground(bgColor);
    frame.setSize(width, height);
    frame.setContentPane(content);
    frame.addWindowListener(new ExitListener());
    frame.setVisible(true);
    return(frame);
  }

  /** Uses Color.WHITE as the background color. */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height,
                                    String title) {
    return(openInJFrame(content, width, height,
                        title, Color.WHITE));
  }

  /** Uses Color.WHITE as the background color, and the
   *  name of the Container's class as the JFrame title.
   */

  public static JFrame openInJFrame(Container content,
                                    int width,
                                    int height) {
    return(openInJFrame(content, width, height,
                        content.getClass().getName(),
                        Color.WHITE));
  }
}