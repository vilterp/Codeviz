package org.codeviz.model;

import java.io.File;

public class SourceLine {

    private String fileName;
    private int lineNo;

    public SourceLine(String fileName, int lineNo) {
        this.fileName = fileName;
        this.lineNo = lineNo;
    }

    public String toString() {
        return String.format("%s:%d", new File(fileName).getName(), lineNo);
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNo() {
        return lineNo;
    }

}
