//Only for Astah UML and Professional.
//This script searches classes by a keyword and edits the classes.
//The color of classes will be changed and a new Note will be added
//to them.
//You should change the 'keyword' before running this.

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
        print('No class found by keyword: ' + keyword + '.');
        print('Change the keyword and try again.');
        return;
    }

    with(new JavaImporter(
            com.change_vision.jude.api.inf.editor)) {
        TransactionManager.beginTransaction();
        for (var i in targets) {
            edit(targets[i]);
        }
        TransactionManager.endTransaction();
    }

    print('The color of the classes were changed.');
    print('New Notes were created and added to the classes.');
}

function searchClass(keyword) {
    with(new JavaImporter(
            com.change_vision.jude.api.inf.model)) {
        var targets = [];
        var classes = astah.findElements(IClass.class);
        for (var i in classes) {
            //Search a class by the name
            var clazz = classes[i];
            var className = clazz.getName();
            if (className != null && className.indexOf(keyword) >= 0) {
                targets.push(clazz);
                print('HIT: ' + className);
            }
        }
        return targets;
    }
}

function edit(target) {
    with(new JavaImporter(
            com.change_vision.jude.api.inf.presentation,
            java.awt.geom)) {
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
}
