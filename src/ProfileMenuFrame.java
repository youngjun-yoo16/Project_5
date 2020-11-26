import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class ProfileMenuFrame extends JComponent implements Runnable {
    Socket socket;
    String userId;
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    JFrame profileMenuFrame;
    JButton createProfileButton;
    JButton editProfileButton;
    JButton deleteProfileButton;
    JButton backButton;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                SwingUtilities.invokeLater(new LoginFrame(socket));
                profileMenuFrame.dispose();
            }
            if (e.getSource() == editProfileButton) {
                SwingUtilities.invokeLater(new EditProfileFrame(socket,userId));
                profileMenuFrame.dispose();
            }
            if (e.getSource() == deleteProfileButton) {
                int isDelete = JOptionPane.showConfirmDialog(null,
                        "All you sure to delete all your profile?", "Profile delete",
                        JOptionPane.YES_NO_OPTION);
                if (isDelete == JOptionPane.YES_OPTION) {
                    printWriter.println("DeleteOwnProfile");
                    printWriter.println(userId);
                    printWriter.flush();
                    String success = "";
                    try {
                        success = bufferedReader.readLine();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    switch (success) {
                        case "No Profile" -> JOptionPane.showMessageDialog(null,
                                "No profile to delete", "Delete Profile Error", JOptionPane.ERROR_MESSAGE);
                        case "Success" -> JOptionPane.showMessageDialog(null, "Congratulations! " +
                                        "You have successfully delete your profile!",
                                "Profile deletion Successful", JOptionPane.INFORMATION_MESSAGE);
                        case "Failure" -> JOptionPane.showMessageDialog(null, "Oops!" +
                                        "Unsuccessful deletion./n Please retry.",
                                "Delete Profile Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    };

    public ProfileMenuFrame(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to initialize in Menu frame", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        profileMenuFrame = new JFrame("Profile Menu Frame");
        Container profileMenuFrameContentPane = profileMenuFrame.getContentPane();
        profileMenuFrameContentPane.setLayout(null);
        deleteProfileButton = new JButton("Delete Profile");
        editProfileButton = new JButton("Create / Edit Profile");
        backButton = new JButton("Back to Login");
        //Set component location
        deleteProfileButton.setBounds(140, 50 , 150, 30);
        editProfileButton.setBounds(140, 100 , 150, 30);
        backButton.setBounds(140,150, 150, 30);

        //Add actionLister
        deleteProfileButton.addActionListener(actionListener);
        editProfileButton.addActionListener(actionListener);
        backButton.addActionListener(actionListener);

        //Add all components into the Frame;
        profileMenuFrameContentPane.add(editProfileButton);
        profileMenuFrameContentPane.add(deleteProfileButton);
        profileMenuFrameContentPane.add(backButton);
        //Finalize the Frame
        profileMenuFrame.setSize(400, 300);
        profileMenuFrame.setLocationRelativeTo(null);
        profileMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileMenuFrame.setVisible(true);
    }
}
