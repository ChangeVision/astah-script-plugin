//This script shows how to check the edition of Astah.
importPackage(com.change_vision.jude.api.inf.editor);

run();

function run() {
    if (!isSupportedAstah()) {
        println('This edition is not supported');
    }

    //Use a special API here.
    //Ex:
    //TransactionManager.beginTransaction();
    //Edit the astah model
    //TransactionManager.endTransaction();
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
