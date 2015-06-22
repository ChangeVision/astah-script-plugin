with (new JavaImporter(
        com.change_vision.jude.api.inf.model)) {
    classes = astah.findElements(IClass.class);
    print('Class # = ' + classes.length);
}