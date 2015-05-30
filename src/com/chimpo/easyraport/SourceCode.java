package com.chimpo.easyraport;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

//TODO implement left/right margin = 1,5cm - available only with .doc ( HWPF)
//TODO implement blank cell borders - available- lot of copy/paste work;
//TODO implement picture floating mode( behind text, etc.) - maybe with XWPFPicture(Data) or Paragraph? not sure

public class SourceCode {

    File directory;
    ArrayList< File > picsList = new ArrayList<File>();
    int picsNumber=0;

    XWPFTableRow tableRow;
    XWPFTable table;

    static int textSample=0;

    SourceCode()
    {
        chooseDirectory();
    }
    /** Writes pictures into table in .doc file*/
    public void generateTable()
    {
        if(readImages()) prepareAndWriteToWordTable();
    }

    private void prepareAndWriteToWordTable()
    {
        try {
            XWPFDocument document= new XWPFDocument();
            document.createNumbering();
            FileOutputStream fos= new FileOutputStream( new File("Raport.docx"));

            table = document.createTable();
            table.setWidth(500);

            int rows;
            if(picsNumber % 2 == 0)
            {
                rows = picsNumber/2;
            }else
            {
                rows = picsNumber/2 + 1;
            }

            int width= 250, height= 180;

            setRowWithPics(true, height,width, new FileInputStream(picsList.get(0)),new FileInputStream(picsList.get(1)));

            for(int i=2; i<rows*2; i+=2)
            {
                setRowWithPics(false, height,width, new FileInputStream(picsList.get(i)), new FileInputStream(picsList.get(i+1)));
            }

            document.write(fos);
            fos.close();
        }
        catch(InvalidFormatException e)
        {
            e.getMessage();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /** Prepares one row of table with two pictures. Notice: First call is different from the rest.    */
    private void setRowWithPics(boolean firstCall,int width,int height, InputStream pictureFile1, InputStream pictureFile2) throws InvalidFormatException,IOException
    {
        if(firstCall)
        {
            tableRow= table.getRow(0);
            if(pictureFile1 != null) tableRow.getCell(0).addParagraph().createRun().addPicture(pictureFile1, XWPFDocument.PICTURE_TYPE_BMP, null, Units.toEMU(height), Units.toEMU(width));
            tableRow.getCell(0).addParagraph().createRun().setText(picsList.get(textSample).getName());
            ++textSample;

            if(pictureFile2 != null) tableRow.addNewTableCell().addParagraph().createRun().addPicture(pictureFile2, XWPFDocument.PICTURE_TYPE_BMP, null, Units.toEMU(height), Units.toEMU(width));
            tableRow.getCell(1).addParagraph().createRun().setText(picsList.get(textSample).getName());
            ++textSample;
        }
        else
        {
            tableRow= table.createRow();
            if(pictureFile1 != null) tableRow.getCell(0).addParagraph().createRun().addPicture(pictureFile1, XWPFDocument.PICTURE_TYPE_BMP, null, Units.toEMU(height), Units.toEMU(width));
            tableRow.getCell(0).addParagraph().createRun().setText(picsList.get(textSample).getName());
            ++textSample;

            if(pictureFile2 != null) tableRow.getCell(1).addParagraph().createRun().addPicture(pictureFile2, XWPFDocument.PICTURE_TYPE_BMP, null, Units.toEMU(height), Units.toEMU(width));
            tableRow.getCell(1).addParagraph().createRun().setText(picsList.get(textSample).getName());
            ++textSample;
        }
    }

    /** Puts all files in folder into list, sorted by date of creation*/
    public boolean readImages()
    {
        if(loadPictures())
        {
            try
            {
                sortPicsList();
            }
            catch (IOException e)
            {
                System.err.println("sortPicsList ERROR HANDLED");
            }
        }
        else
        {
            System.err.println("No directory Selected..");
            return false;
        }
        return true;
    }

    /** Loads pictures with .BMP, .PNG and .JPG extension into picsList*/
    private boolean loadPictures() {

        if(directory == null) return false;

        for(File singleFile : directory.listFiles())
        {
            if( singleFile.toString().endsWith("bmp")   ||
                singleFile.toString().endsWith("png")   ||
                singleFile.toString().endsWith("jpg")   )
            {
                picsList.add(singleFile);
                ++picsNumber;
            }
        }

        System.out.println("How many photos? "+ picsNumber+ "\n");

        if(picsList.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"No pictures in selected folder or wrong extension\n(ONLY BMP,PNG,JPG SUPPORTED)");
            return false;
        }

        return true;
    }

    /** Sorts pictures by creation date*/
    private void sortPicsList() throws IOException
    {
        BasicFileAttributes fileAttributes1, fileAttributes2;

        for(int m=0; m< picsNumber; m++)
        {
         for(int n=0; n< picsNumber; n++)
         {
            fileAttributes1 = Files.readAttributes(picsList.get(m).toPath(),BasicFileAttributes.class);
             fileAttributes2 = Files.readAttributes(picsList.get(n).toPath(),BasicFileAttributes.class);

             if(fileAttributes1.lastModifiedTime().toMillis() < fileAttributes2.lastModifiedTime().toMillis())
             {
                 File temp= picsList.get(m);
                 picsList.set(m,picsList.get(n));
                 picsList.set(n,temp);
             }
         }
        }
    }

    /** Method enables choosing directory with pictures*/
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

        SourceCode newRaport= new SourceCode();
        newRaport.generateTable();

    }
}
