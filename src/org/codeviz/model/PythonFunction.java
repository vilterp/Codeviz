package org.codeviz.model;

import java.util.Arrays;

public class PythonFunction extends PythonCallable {

    public PythonFunction(String name, String[] argNames, SourceLine line) {
        super(name, argNames, line);
    }

    @Override
    public String toString() {
        return "PythonFunction{" +
                "name='" + getName() + '\'' +
                ", argNames=" + (getArgNames() == null ? null : Arrays.asList(getArgNames())) +
                ", loc=" + getSourceLoc() +
                '}';
    }

}
