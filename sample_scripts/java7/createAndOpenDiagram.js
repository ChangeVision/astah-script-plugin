//Only for Astah UML and Professional.
//This script creates and opens new class diagram.
importPackage(com.change_vision.jude.api.inf.editor);

var newDiagramName = 'New Class Diagram';

run();

function run() {
    if (!isSupportedAstah()) {
        println('This edition is not supported');
    }

    //Edit the astah model
    TransactionManager.beginTransaction();
    var editor = astah.getDiagramEditorFactory().getClassDiagramEditor();
    var newDgm = editor.createClassDiagram(astah.getProject(), newDiagramName);
    TransactionManager.endTransaction();
    println('New Class Diagram was created!');
    
    //Open the diagram
    var dgmViewManager = astah.getViewManager().getDiagramViewManager();
    dgmViewManager.open(newDgm);
}

function isSupportedAstah() {
    var edition = astah.getAstahEdition();
    println('Your edition is ' + edition);
    if (edition == 'professional' || edition == 'UML') {
        return true;
    } else {
        return false;
    }
}
