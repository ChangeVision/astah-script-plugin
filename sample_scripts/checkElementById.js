//Only for Astah UML and Professional.
//This script searches classes by a keyword and edits the classes.
//The color of classes will be changed and a new Note will be added
//to them.
//You should change the 'keyword' before running this.
importPackage(com.change_vision.jude.api.inf.model);
importPackage(com.change_vision.jude.api.inf.presentation);
importPackage(com.change_vision.jude.api.inf.editor);
importPackage(java.awt.geom);

var id='tk77-h82isnfo-re1z83-e2kcan-8d15affe08835b11144c86603644a1bc';

run();

function run() {
    var entity = astah.getEntity(id);
    println('Found: ' + entity.getName());
    var presentations = entity.getPresentations();
    if (presentations.length > 0) {
        var diagram = presentations[0].getDiagram();
        println('AAA ');
    } else {
        println('BBBB ');
    }
}
