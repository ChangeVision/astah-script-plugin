importPackage(com.change_vision.jude.api.inf.model);

var depth = 0;

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
    expandTopics(rootTopic);
}

function expandTopics(topic) {
    //var mmEditor = astah.getDiagramEditorFactory().getMindmapEditor();
    //mmEditor.setDiagram(topic.getDiagram());
    printPresentationProperties(topic.getChildren()[0]);
    /*
    var topics = topic.getChildren();
    depth++;
    for (var i in topics) {
        if (topics[i].getType() == 'Topic') { //skip MMBoundary
            printTopics(topics[i]);
        }
    }
    depth--;
    */
}

function printPresentationProperties(presentation) {
    var props = presentation.getProperties();
    println(props);
}
