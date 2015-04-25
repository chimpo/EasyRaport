package com.chimpo.easyraport;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class SourceCode {

    File directory;
    ArrayList< File > picsList = new ArrayList<File>();

    SourceCode()
    {
        chooseDirectory();
        readImages();
    }

    /**Loads pictures with .BMP, .PNG and .JPG extension into picsList*/
    public boolean loadPictures() {
        for(File singleFile : directory.listFiles())
        {
            if( singleFile.toString().endsWith("bmp")   ||
                singleFile.toString().endsWith("png")   ||
                singleFile.toString().endsWith("jpg")   )
            {
                picsList.add(singleFile);
            }
        }

        if(picsList.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"No pictures in selected folder or wrong extension\n(ONLY BMP,PNG,JPG SUPPORTED)");
            return false;
        }

        return true;
    }
    /**Sorts pictures by creation date*/
    public void sortPicsList() throws IOException
    {
        Path path1, path2;
        BasicFileAttributes fileAttributes1, fileAttributes2;


    }

    /**Puts all files in folder into list, sorted by date of creation*/
    public void readImages()
    {
        if(loadPictures()) {
            try {
                sortPicsList();
            } catch (IOException e) {
                System.err.println("sortPicsList ERROR HANDLED");
            }
        }

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
