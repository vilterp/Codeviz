package org.codeviz.model;

public class PythonClass {

    private String name;
    private SourceLine line;

    public PythonClass(String name, SourceLine line) {
        this.name = name;
        this.line = line;
    }

    @Override
    public String toString() {
        return "PythonClass{" +
                "name='" + name + '\'' +
                ", line=" + line +
                '}';
    }

    public String getName() {
        return name;
    }

    public SourceLine getLine() {
        return line;
    }

}
