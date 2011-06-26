package org.codeviz.test.jythonplumbingtest;

import org.codeviz.PythonRunner;
import org.codeviz.PythonRunnerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JythonEditor extends JFrame implements PythonRunnerListener {

    JTextArea codeArea;
    JButton execButton;
    JLabel runningStLabel;
    JList ioConsole;
    JTree callTreeView;    
    
    PythonRunner runner;
    
    static final String FILE_NAME = "<editor>";    
    static final Font CODE_FONT = new Font("Monospaced", Font.PLAIN, 14);
    static final int WIDTH = 60;
    
    public static void main(String[] args) {
    	new JythonEditor();
    }
    
    public JythonEditor() {
        super("Jython Test");
        
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        // init code area
        codeArea = new JTextArea(35, WIDTH);
        codeArea.setFont(CODE_FONT);
        codeArea.setText("print 'foo'");
        p.add(new JScrollPane(codeArea));

        // init exec button
        execButton = new JButton("Exec");
        execButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String source = codeArea.getText();
                runner = new PythonRunner(FILE_NAME, source);
                Thread t = new Thread(runner);
                runner.addPythonRunnerListener(JythonEditor.this);
                ioConsole.setModel(runner.getIOModel());
                // callTreeView.setModel(runner.getCallTreeModel());
                t.setName("PythonRunner");
                t.start();
            }
        });
        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(execButton, BorderLayout.WEST);
        runningStLabel = new JLabel("");
        p2.add(runningStLabel);
        p.add(p2);

        // init console
        ioConsole = new JList();
        p.add(new JScrollPane(ioConsole));
        
        // init trace area
        callTreeView = new JTree();
        p.add(new JScrollPane(callTreeView));
        
        // go!
        add(p);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void runnerStarted() {
        runningStLabel.setText("Running...");
    }
    
    public void runnerFinished() {
        runningStLabel.setText("");
        callTreeView.setModel(runner.getCallTreeModel());
        System.out.println(runner.getCallTreeModel().treeRepr());
    }
    
}
