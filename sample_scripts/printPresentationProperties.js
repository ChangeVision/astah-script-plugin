// This script prints the properties of the selected element.
// You should select an element in the DiagramEditor before running this.
importPackage(com.change_vision.jude.api.inf.editor);
importPackage(java.util);

run();

function run() {
    var targets = getSelectedPresentationsInDiagramEditor();

    if (targets.length === 0) {
        println('Please select an element in a diagram');
        return;
    }

    for (var i in targets) {
        printAllProperties(targets[i]);
    }
}

function getSelectedPresentationsInDiagramEditor() {
    var diagramViewManager = astah.getViewManager().getDiagramViewManager();
    return diagramViewManager.getSelectedPresentations();
}

function printAllProperties(presentation) {
    var props = presentation.getProperties();
    var keyList = new ArrayList(props.keySet());
    Collections.sort(keyList);
    println('---------------------------');
    for (var i = 0; i < keyList.size(); i++) {
        var key = keyList.get(i);
        var value = props.get(key);
        println(key + ': ' + value);
    }
    println('---------------------------');
}
