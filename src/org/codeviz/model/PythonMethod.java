package org.codeviz.model;

import java.util.Arrays;

public class PythonMethod extends PythonCallable {

    private PythonClass klass;

    public PythonMethod(String name, String[] argNames, SourceLine line, PythonClass klass) {
        super(name, argNames, line);
        this.klass = klass;
    }

    @Override
    public String toString() {
        return "PythonMethod{" +
                "name='" + getName() + '\'' +
                ", argNames=" + (getArgNames() == null ? null : Arrays.asList(getArgNames())) +
                ", loc=" + getSourceLoc() +
                ", class=" + klass +
                '}';
    }

}
