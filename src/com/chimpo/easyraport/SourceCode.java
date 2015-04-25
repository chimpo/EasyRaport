package com.chimpo.easyraport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class SourceCode {

    File directory;

    SourceCode()
    {
        chooseDirectory();
    }

    /**
     * Method enables choosing directory with pictures*/
    private void chooseDirectory()
    {
        JOptionPane.showMessageDialog(null,"You have to choose directory with pictures");
        JFileChooser directoryDialog = new JFileChooser();
        directoryDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = directoryDialog.showDialog(null, "This is my directory!");
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            directory= directoryDialog.getSelectedFile();
            System.out.println(directory.getAbsolutePath());
        }
        else
        {
            System.err.println("No directory to go on..");
        }
    }


    public static void main(String[] args) {

        new SourceCode();
    }
}
