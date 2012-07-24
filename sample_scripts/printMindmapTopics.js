//This script writes out a list of text in the current mindmap.
//The format is like a WiKi.
importPackage(com.change_vision.jude.api.inf.model);

var depth = 0;
var INDENT_STR = '  '; //2 spaces
var ITEM_MARKER_STR = '* ';

run();

function run() {
    var diagramViewManager = astah.getViewManager().getDiagramViewManager();
    var diagram = diagramViewManager.getCurrentDiagram();
    if (!(diagram instanceof IMindMapDiagram)) {
        println('Open a mindmap and run again.');
        return;
    }

    var rootTopic = diagram.getRoot();
    depth = 0;
    printTopics(rootTopic);
}

function printTopics(topic) {
    var topicLabel = topic.getLabel().replaceAll('\n', ' ');
    println(getIndent(depth) + ITEM_MARKER_STR + topicLabel);

    var topics = topic.getChildren();
    depth++;
    for (var i in topics) {
        if (topics[i].getType() == 'Topic') { //skip MMBoundary
            printTopics(topics[i]);
        }
    }
    depth--;
}

function getIndent(depth) {
    var indent = '';
    for (var i = 0; i < depth; i++) {
        indent += INDENT_STR;
    }
    return indent;
}
