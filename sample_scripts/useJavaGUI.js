//This script shows a GUI by using AWT and Swing of Java.

with(new JavaImporter(
        java.awt,
        java.awt.event,
        javax.swing)) {

    var frame = new JFrame("Frame title");
    frame.setLayout(new FlowLayout());

    var goButton = new JButton("Go!");
    goButton.addActionListener(new ActionListener({
        actionPerformed: function(event) {
            JOptionPane.showMessageDialog(frame, "Hello!");
        }
    }));

    var closeButton = new JButton("Close");
    closeButton.addActionListener(new ActionListener({
        actionPerformed: function(event) {
            frame.setVisible(false);
            frame.dispose();
        }
    }));

    frame.add(goButton);
    frame.add(closeButton);
    frame.setSize(150, 100);
    //We must not use JFrame.EXIT_ON_CLOSE
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //We can use 'astahWindow' instead of 'scriptWindow' here.
    frame.setLocationRelativeTo(scriptWindow);
    frame.setVisible(true);
}

