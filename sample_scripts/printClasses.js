//This script writes out all the classifiers in the current Astah project.
with(new JavaImporter(
        com.change_vision.jude.api.inf.model)) {
    classes = astah.findElements(IClass.class);
    for(var i in classes) {
        print(classes[i].getName());
    }
}
