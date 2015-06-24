//Only for Astah UML and Professional.
//This script searches classes by a keyword and edits the classes.
//The color of classes will be changed and a new Note will be added
//to them.
//You should change the 'keyword' before running this.
importPackage(com.change_vision.jude.api.inf.model);
importPackage(com.change_vision.jude.api.inf.presentation);
importPackage(com.change_vision.jude.api.inf.editor);
importPackage(java.awt.geom);

var COLOR_PROPERTY_KEY = "fill.color";
var HORIZONTAL_OFFSET = 20.0;
var VERTICAL_OFFSET = -40.0;

var keyword = 'ABC'; //Modify this keyword
var newColor = '#ff0000';
var noteText = 'New Note';

run();

function run() {
    var targets = searchClass(keyword);

    if (targets.length === 0) {
        println('No class found by keyword: ' + keyword + '.');
        println('Change the keyword and try again.');
        return;
    }

    TransactionManager.beginTransaction();
    for (var i in targets) {
        edit(targets[i]);
    }
    TransactionManager.endTransaction();

    println('The color of the classes were changed.');
    println('New Notes were created and added to the classes.');
}

function searchClass(keyword) {
    var targets = [];
    var classes = astah.findElements(IClass);
    for (var i in classes) {
        //Search a class by the name
        var clazz = classes[i];
        var className = clazz.getName();
        if (className != null && className.indexOf(keyword) >= 0) {
            targets.push(clazz);
            println('HIT: ' + className);
        }
    }
    return targets;
}

function edit(target) {
    var classDiagramEditor = astah.getDiagramEditorFactory().getClassDiagramEditor();
    var presentations = target.getPresentations();
    for (var j in presentations) {
        var classPs = presentations[j];
        if (!(classPs instanceof INodePresentation)) continue;

        classDiagramEditor.setDiagram(classPs.getDiagram());

        // Change color of the class
        classPs.setProperty(COLOR_PROPERTY_KEY, newColor);

        // Add new Note to the class
        var classX = classPs.getLocation().getX();
        var classY = classPs.getLocation().getY();
        var noteX = classX + classPs.getWidth() + HORIZONTAL_OFFSET;
        var noteY = classY + VERTICAL_OFFSET;
        var noteLoc = new Point2D.Double(noteX, noteY);

        var notePs = classDiagramEditor.createNote(noteText, noteLoc);
        var noteAnchorPs = classDiagramEditor.createNoteAnchor(notePs, classPs);
    }
}
