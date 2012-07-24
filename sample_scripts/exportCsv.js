//This script generates a CSV file about the classes in the current model.
//CSV format:
// "Name of a class", "Name of the parent model", "Definition of the class"
importPackage(com.change_vision.jude.api.inf.model);
importPackage(java.io);
importPackage(javax.swing);

run();

function run() {
    exportClassesInCsv();
}

function exportClassesInCsv() {
    classes = astah.findElements(IClass);
    
    var csvFile = selectCsvFile();
    if (csvFile == null) {
        println('Canceled');
        return;
    }
    println('Selected file = ' + csvFile.getAbsolutePath());
    if (csvFile.exists()) {
        var msg = "Do you want to overwrite?";
        var ret = JOptionPane.showConfirmDialog(scriptWindow, msg);
        if (ret != JOptionPane.YES_OPTION) {
            println('Canceled');
            return;
        }
    }
    
    var writer = new BufferedWriter(new FileWriter(csvFile));
    
    for(var i in classes) {
        var clazz = classes[i];
        var rowData = [];
        rowData.push(clazz.getName());
        rowData.push(clazz.getOwner().getName());
        rowData.push(clazz.getDefinition());
        writeRow(writer, rowData);
    }
    
    writer.close();
}

function selectCsvFile() {
    var chooser = new JFileChooser();
    var selected = chooser.showSaveDialog(scriptWindow);
    if (selected == JFileChooser.APPROVE_OPTION) {
        var file = chooser.getSelectedFile();
        if (file.getName().toLowerCase().endsWith('.csv')) {
            return file;
        } else {
            return new File(file.getAbsolutePath() + '.csv');
        }
    } else {
        return null;
    }
}

function writeRow(writer, rowData) {
    for (var i in rowData) {
        writeItem(writer, rowData[i]);
        if (i < rowData.length - 1) {
            writer.write(',');
        }
    }
    writer.newLine();
}

function writeItem(writer, data) {
    writer.write('"');
    writer.write(escapeDoubleQuotes(data));
    writer.write('"');
}

function escapeDoubleQuotes(data) {
    return data.replaceAll("\"", "\"\"");
}
