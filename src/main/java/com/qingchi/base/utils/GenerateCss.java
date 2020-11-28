package com.qingchi.base.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author qinkaiyuan
 * @date 2018-10-30 21:00
 */
public class GenerateCss {

    public static void main(String[] args) throws IOException {
        String fileName = "E:/Users/qinkaiyuan/Desktop/css/width.css";
        Path fpath = Paths.get(fileName);
        BufferedWriter bfw = Files.newBufferedWriter(fpath);
        for (int i = 1; i < 1001; i++) {
            String startStr = "h";
            Integer width = i;
            String attribute = "height";
            String calculation = "px";
            bfw.write("." + startStr + width + calculation + "{");
            bfw.newLine();
            bfw.write("    " + attribute + ": " + width + calculation + ";");
            bfw.newLine();
            bfw.write("}");
            bfw.newLine();
        }
        bfw.flush();
        bfw.close();
    }
}
