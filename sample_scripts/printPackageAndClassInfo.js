//This script writes out the information of packages and classes.
//The current Astah project should have packages and classes.
run();

function run() {
    var project = astah.getProject();
    printPackageInfo(project);
}

function printPackageInfo(iPackage) {
    print("Package name: " + iPackage.getName());
    print("Package definition: " + iPackage.getDefinition());

    with(new JavaImporter(
            com.change_vision.jude.api.inf.model)) {
        // Display packages and classes
        var namedElements = iPackage.getOwnedElements();
        for (var i in namedElements) {
            var namedElement = namedElements[i];
            if (namedElement instanceof IPackage) {
                printPackageInfo(namedElement);
            } else if (namedElement instanceof IClass) {
                printClassInfo(namedElement);
            }
        }
    }
}

function printClassInfo(iClass) {
    print("Class name: " + iClass.getName());
    print("Class definition: " + iClass.getDefinition());

    // Display all attributes
    var iAttributes = iClass.getAttributes();
    for (var i in iAttributes) {
        printAttributeInfo(iAttributes[i]);
    }

    // Display all operations
    var iOperations = iClass.getOperations();
    for (var i in iOperations) {
        printOperationInfo(iOperations[i]);
    }

    // Display inner class information
    var classes = iClass.getNestedClasses();
    for (var i in classes) {
        printClassInfo(classes[i]);
    }
}

function printOperationInfo(iOperation) {
    print("Operation name: " + iOperation.getName());
    print("Operation returnType: " + iOperation.getReturnTypeExpression());
    print("Operation definition: " + iOperation.getDefinition());
}

function printAttributeInfo(iAttribute) {
    print("Attribute name: " + iAttribute.getName());
    print("Attribute type: " + iAttribute.getTypeExpression());
    print("Attribute definition: " + iAttribute.getDefinition());
}
