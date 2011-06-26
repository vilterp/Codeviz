package org.codeviz.model;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CallTreeModel implements TreeModel {
    
    private static final CallNode DUMMY_ROOT = new CallNode(0, new PythonFunction("dummy", new String[]{"foo"}, new SourceLine("dummy.py", 5)), new LocalsRecord(0L));
    
    private Stack<CallNode> callStack;
    private CallNode root;
    
    private List<TreeModelListener> listenerList;
    
    public CallTreeModel() {
        callStack = new Stack<CallNode>();
        root = null;
        listenerList = new ArrayList<TreeModelListener>();
    }
    
    public String treeRepr() {
        if(getRoot() == null)
            return "";
        else
            return getRoot().treeRepr(0);
    }
    
    public CallNode getCurrentCall() {
        return callStack.peek();
    }
    
    public void pushCall(CallNode newCall) {
        CallNode[] oldPath;
        if(callStack.isEmpty()) {
            oldPath = new CallNode[]{ newCall };
            root = newCall;
        } else {
            newCall.setParent(getCurrentCall());
            oldPath = getCurrentPath();
            getCurrentCall().addChildCall(newCall);
        }
        callStack.push(newCall);
        fireInserted(new TreeModelEvent(this, oldPath));
    }
    
    public void popCall() {
        callStack.pop();
    }
    
    public void addStepEvent(StepEvent e) {
        getCurrentCall().addStep(e);
        fireChanged(new TreeModelEvent(this, getCurrentPath()));
    }
    
    public int getCurrentStackSize() {
        return callStack.size();
    }
    
    private CallNode[] getCurrentPath() {
        CallNode[] retVal = new CallNode[getCurrentStackSize()];
        return callStack.toArray(retVal);
    }
    
    private void fireInserted(final TreeModelEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(TreeModelListener l: listenerList) {
                    l.treeNodesInserted(evt);
                }
            }
        });
    }
    
    private void fireChanged(final TreeModelEvent evt) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for(TreeModelListener l: listenerList) {
                    l.treeNodesChanged(evt);
                }
            }
        });
    }
    
    // private CallNode getNodeAtPath(TreePath path) {
    //     Object[] path = path.getPath();
    //     CallNode cursor = getRoot();
    //     for(int i=1; i < path.length; i++) {
    //         cursor = cursor.getChildren().get();
    //     }
    // }
    
    // TreeModel stuff
    
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(l);
    }
    
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(l);
    }
    
    public CallNode getChild(Object parent, int index) {
        return ((CallNode) parent).getChildren().get(index);
    }
    
    public int getChildCount(Object parent) {
        return ((CallNode) parent).getChildren().size();
    }
    
    public int getIndexOfChild(Object parent, Object child) {
        return ((CallNode) parent).getChildren().indexOf(child);
    }
    
    public boolean isLeaf(Object node) {
        return ((CallNode) node).getChildren().isEmpty();
    }
    
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException();
        // CallNode oldValue = path.getPath()[path.getPathCount()-1];
        // if(!oldValue.equals(newValue))
        //     fireNodesChanged(new TreeModelEvent(this, path));
    }
    
    public CallNode getRoot() {
        return root;
    }
    
}
