//This script shows a GUI by using AWT and Swing of Java.
importPackage(java.awt);
importPackage(java.awt.event);
importPackage(javax.swing);

var frame = new JFrame("Frame title");
frame.setLayout(new FlowLayout());

var goButton = new JButton("Go!");
addActionListenerGoButton(frame, goButton);

var closeButton = new JButton("Close");
addActionListenerCloseButton(frame, closeButton);

function addActionListenerGoButton(f, b) {
    b.addActionListener(new ActionListener({
        actionPerformed: function(event) {
            JOptionPane.showMessageDialog(f, "Hello!");
        }
    }));
    return b;
}

function addActionListenerCloseButton(f, b) {
    b.addActionListener(new ActionListener({
        actionPerformed: function(event) {
            f.setVisible(false);
            f.dispose();
        }
    }));
}

frame.add(goButton);
frame.add(closeButton);
frame.setSize(150, 100);
//We must not use JFrame.EXIT_ON_CLOSE
frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//We can use 'astahWindow' instead of 'scriptWindow' here.
frame.setLocationRelativeTo(scriptWindow);
frame.setVisible(true);
