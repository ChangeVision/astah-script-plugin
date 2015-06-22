//This script shows how to check the edition of Astah.
run();

function run() {
    if (!isSupportedAstah()) {
        print('This edition is not supported');
    }

    //Use a special API here.
    //Ex:
    //TransactionManager.beginTransaction();
    //Edit the astah model
    //TransactionManager.endTransaction();
}

function isSupportedAstah() {
    with(new JavaImporter(
        com.change_vision.jude.api.inf.editor)) {
        var edition = astah.getAstahEdition();
        print('Your edition is ' + edition);
        if (edition == 'professional' || edition == 'UML') {
            return true;
        } else {
            return false;
        }
    }
}
