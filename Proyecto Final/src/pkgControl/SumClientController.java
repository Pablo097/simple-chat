package pkgControl;
import pkgClients.SumClient;
import pkgFrames.SumClientFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SumClientController implements ActionListener {
    private SumClientFrame frame;

    public SumClientController(SumClientFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand().toUpperCase()) {
            case "SUM":
                double num1, num2;
                try {
                    num1 = Double.parseDouble(frame.getNum1Text());
                    num2 = Double.parseDouble(frame.getNum2Text());

                    SwingWorker worker = new SwingWorker<Double, Void>() {
                        @Override
                        protected Double doInBackground() throws Exception {
                            SumClient client = new SumClient(frame.hostname, frame.port, num1, num2);
                            client.setFrame(frame);
                            client.connect();
                            return client.getResult();
                        }

                        @Override
                        protected void done() {
                            try {
                                frame.setResultText(get().toString());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    };
                    worker.execute();
                } catch (NumberFormatException nfe) {
                    frame.appendTextArea("Number format error");
                }
                break;
            case "CLEAR":
                frame.clearAllTexts();
                frame.clearTextArea();
                break;
            case "EXIT":
                System.exit(0);
                break;
            default:
                throw new RuntimeException("Unknown command");
        }
    }
}
