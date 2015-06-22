//Only for Astah UML and Professional.
//This script creates EREntities.
var entities = [
                'Entity1LogicalName', 'Entity1PhysicalName',
                'Entity2LogicalName', 'Entity2PhysicalName',
                'Entity3LogicalName', 'Entity3PhysicalName'
               ];

run();

function run() {
    //Edit the astah model
    var editor = astah.getModelEditorFactory().getERModelEditor();

    var erModel = getOrCreateERModel(editor);
    var erSchema = erModel.getSchemata()[0];
    with(new JavaImporter(
            com.change_vision.jude.api.inf.editor)) {
        for (var i = 0; i < entities.length; i += 2) {
            var logicalName = entities[i];
            var physicalName = entities[i + 1];
            try {
                TransactionManager.beginTransaction();
                var newEntity = editor.createEREntity(erSchema, logicalName, physicalName);
                print('Created EREntity: ' + logicalName);
                TransactionManager.endTransaction();
            } catch (e) {
                print('Failed to create EREntity: ' + logicalName);
                TransactionManager.abortTransaction();
            }
        }
    }
}

function getOrCreateERModel(editor) {
    with(new JavaImporter(
            com.change_vision.jude.api.inf.model,
            com.change_vision.jude.api.inf.editor)) {
        //Search ERModel
        var elements = astah.findElements(IERModel.class);
        if (elements.length > 0) {
            return elements[0];
        }

        //Create ERModel
        TransactionManager.beginTransaction();
        erModel = editor.createERModel(astah.getProject(), 'ER Model');
        TransactionManager.endTransaction();
    }

    return erModel;
}
