package pkgControl;
import pkgFrames.ChatClientFrame;
import pkgFrames.IndividualChatPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatClientController implements ActionListener {
    private ChatClientFrame frame;

    public ChatClientController(ChatClientFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IndividualChatPanel chatPanel;
        String username;
        StringBuilder sb;
        LinkedBlockingQueue<String> queue = frame.getClientQueue();
        switch (e.getActionCommand()) {
            case "CONNECT":
                frame.startClient();
                break;
            case "REFRESH":
                try {
                    queue.put("GET_USERS");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;
            case "START":
                username = frame.getSelectedUsernameOnList();
                if (!frame.setChatTab(username)) {
                    chatPanel = frame.getChatPanel(username);
                    frame.setChatTab(username);
                    sb = new StringBuilder().append("MESSAGE ");
                    sb.append(username).append(" * ");
                    sb.append(frame.getUsername()).append(" started a chat with you *");
                    try {
                        queue.put(sb.toString());
                        chatPanel.writeInfoText("* Chat started *");
                        frame.appendLogText("Chat started with " + username);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case "SEND":
                username = frame.getChatTabTitle();
                chatPanel = frame.getChatPanel(username);
                String message = chatPanel.getWriteMessageText();
                if (!message.isEmpty()) {
                    sb = new StringBuilder().append("MESSAGE ");
                    sb.append(username).append(" ").append(message);
                    try {
                        queue.put(sb.toString());
                        chatPanel.writeOwnMessage(message);
                        chatPanel.clearWriteMessageTextField();
                        frame.appendLogText("Message sent to " + username);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case "DISCONNECT":
                try {
                    queue.put("DISCONNECT");
                    frame.disableHub();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                break;
            case "CLEAR":
                frame.clearLogTextArea();
                break;
            case "EXIT":
                // In case this program runs on another PC
                if (frame.isConnected()) {
                    try {
                        queue.put("DISCONNECT");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                // Exit program
                System.exit(0);
                break;
            default:
                throw new RuntimeException("Unknown command");
        }
    }
}
