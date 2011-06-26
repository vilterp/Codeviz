package org.codeviz;

import javax.swing.SwingUtilities;

import org.codeviz.model.*;

import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PythonRunner implements Runnable {
    
    private String fileName;
    private String source;
    
    private CallTreeModel callTree;
    private ObjectGraphModel objectGraph;
    private IOModel ioModel;
    private long step;
    
    private StringBuffer printBuf;
    private boolean printing;
    private IOStream stream;
    
    private List<PythonRunnerListener> listenerList;
    
    public PythonRunner(String fileName, String source) {
        this.fileName = fileName;
        this.source = source;
        callTree = new CallTreeModel();
        objectGraph = new ObjectGraphModel();
        ioModel = new IOModel();
        step = 0;
        printBuf = new StringBuffer();
        printing = false;
        listenerList = new ArrayList<PythonRunnerListener>();
    }
    
    public void run() {
    	// CREATE INTERPRETER
        PythonInterpreter interp = new PythonInterpreter();
        // SET UP PLUMBING
        // TODO: stdin?
        // TODO: reduce code duplication...
        // set stdout & stderr
        interp.setOut(new OutputStream() {
        	
			public void write(int b) {
				if(!printing) {
					printing = true;
					stream = IOStream.STDOUT;
				}
				printBuf.append((char) b);
			}
        	
        });
        interp.setErr(new OutputStream() {
        	
        	public void write(int b) {
        		if(!printing) {
        			printing = true;
        			stream = IOStream.STDERR;
        		}
        		printBuf.append((char) b);
        	}
        	
        });
        // set up trace function
        interp.getSystemState().settrace(new PyObject() {
        	
        	public PyObject __call__(PyObject f, PyObject et, PyObject arg) {
        		PyFrame frame = (PyFrame) f;
        		String eventType = ((PyString) et).toString();
                SourceLine line = new SourceLine(frame.f_code.co_filename, frame.f_lineno);
                // update ioModel, if necessary
                if(printing) {
        			// flush print buffer, send event
        			printing = false;
        			String output = printBuf.toString();
        			printBuf = new StringBuffer();
        			ioModel.addEvent(new PrintEvent(step, line, stream, output));
        		}
        		// update callTree, objectGraph
        		if(eventType.equals("call")) {
                    PyCode c = frame.f_code;
                    PyTableCode code = null;
                    if(c instanceof PyTableCode)
                        code = (PyTableCode) c;
                    else
                        throw new IllegalArgumentException(String.format("not PyTableCode: %s", c));
                    String[] argNames = new String[code.co_argcount];
                    for(int i=0; i < argNames.length; i++) {
                        argNames[i] = code.co_varnames[i];
                    }
                    SourceLine defLine = new SourceLine(code.co_filename, code.co_firstlineno);
                    PythonCallable callable = new PythonFunction(code.co_name, argNames, defLine);
                    System.out.format("call %s%n", callable);
                    PyStringMap locals = (PyStringMap) frame.getLocals();
                    PyIterator localsIter = (PyIterator) locals.iteritems();
                    LocalsRecord localsRecord = new LocalsRecord(step);
                    for(Object item: localsIter) {
                        PyTuple tuple = (PyTuple) item;
                        String key = (String) tuple.get(0);
                        if(key.startsWith("__"))
                            continue;
                        ObjectRecord val = objectGraph.getRecord(step, line, tuple.get(1));
                        localsRecord.put(step, key, val);
                    }
                    callTree.pushCall(new CallNode(step, callable, localsRecord));
                } else if(eventType.equals("line")) {
                    List<TimeDictChange<Long, String, ObjectRecord>> changes =
                            new ArrayList<TimeDictChange<Long, String, ObjectRecord>>();
                    // diff local vars
                    PyStringMap locals = (PyStringMap) frame.getLocals();
                    PyIterator localsIter = (PyIterator) locals.iteritems();
                    for(Object item: localsIter) {
                        PyTuple tuple = (PyTuple) item;
                        String key = (String) tuple.get(0);
                        if(key.startsWith("__"))
                            continue;
                        ObjectRecord val = objectGraph.getRecord(step, line, tuple.get(1));
                        TimeDictChange<Long, String, ObjectRecord> change =
                                callTree.getCurrentCall().acceptLocalVarChange(step, key, val);
                        if(change != null)
                            changes.add(change);
                    }
                    LineStep s = new LineStep(step, line, changes);
                    System.out.format("line %s%n", s);
                    callTree.addStepEvent(s);
        		} else if(eventType.equals("return")) {
                    System.out.format("return %s%n", arg);
        			ObjectRecord retVal = objectGraph.getRecord(step, line, arg);
                    callTree.getCurrentCall().setRetVal(step, retVal);
                    callTree.popCall();
        		} else if(eventType.equals("exception")) {
        			PyTuple tuple = (PyTuple) arg;
                    System.out.format("exception %s %s%n", tuple.get(0), tuple.get(1));
        			Object type = tuple.get(0);
        			Object exc = tuple.get(1);
        			ExceptionStep s = new ExceptionStep(step, line, type, exc);
                    callTree.addStepEvent(s);
        		} else {
        			throw new IllegalArgumentException("unknown event type: " + eventType);
        		}
                step++;
        		return this;
        	}
        	
        });
        // GO
        fireStarted();
        // TODO: handle compile-time exceptions (parse errors, etc -- these will be linked to the source editor)
        PyCode code = interp.compile(source, fileName);
        // TODO: handle runtime exceptions (these will be shown in the viz)
        interp.exec(code);
        fireFinished();
    }
    
    public CallTreeModel getCallTreeModel() {
        return callTree;
    }
    
    public ObjectGraphModel getObjectGraphModel() {
        return objectGraph;
    }
    
    public IOModel getIOModel() {
        return ioModel;
    }
    
    public void addPythonRunnerListener(PythonRunnerListener l) {
        listenerList.add(l);
    }
    
    public void removePythonRunnerListener(PythonRunnerListener l) {
        listenerList.remove(l);
    }
    
    private void fireStarted() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(PythonRunnerListener l: listenerList) {
                    l.runnerStarted();
                }
            }
        });
    }
    
    private void fireFinished() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(PythonRunnerListener l: listenerList) {
                    l.runnerFinished();
                }
            }
        });
    }
    
}
