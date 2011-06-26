package org.codeviz.model;

public abstract class PythonCallable {

    private String name;
    private String[] argNames;
    private SourceLine line;

    protected PythonCallable(String name, String[] argNames, SourceLine line) {
        this.name = name;
        this.argNames = argNames;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public String[] getArgNames() {
        return argNames;
    }

    public SourceLine getSourceLoc() {
        return line;
    }

}
