package com.icekredit.pdf.utils;

import java.io.*;

/**
 * Created by icekredit on 5/5/16.
 */
public class LazyUtil {
    public static BufferedReader reader = null;

    public static final void doNothing(){
        ;
    }

    public static void main(String [] args){
        try {
            File file = new File("/home/icekredit/Documents/workplace/IcekreditPdfCore/src/main/java/com/icekredit/pdf");
            System.out.println("OMG!The lines count of the code you have written is " + calcLinesCountInCurrentDir(file,
                    ""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int calcLinesCountInCurrentDir(File dir,String excludeDir) throws IOException {
        File [] files = dir.listFiles();

        int linesCount = 0;

        for(File file:files){
            if(file.isFile()){
                linesCount += getLinesCount(file);
            }else if (file.isDirectory() && !file.getAbsolutePath().equals(excludeDir)){
                linesCount += calcLinesCountInCurrentDir(file,excludeDir);
            }
        }

        return linesCount;
    }

    private static int getLinesCount(File file) throws IOException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int linesCount = 0;

        while (reader.readLine() != null){
            linesCount ++;
        }

        return linesCount;
    }
}
