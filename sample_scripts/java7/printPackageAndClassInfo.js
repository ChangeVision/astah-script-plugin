//This script writes out the information of packages and classes.
//The current Astah project should have packages and classes.
importPackage(com.change_vision.jude.api.inf.model);

run();

function run() {
    var project = astah.getProject();
    printPackageInfo(project);
}

function printPackageInfo(iPackage) {
    println("Package name: " + iPackage.getName());
    println("Package definition: " + iPackage.getDefinition());

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

function printClassInfo(iClass) {
    println("Class name: " + iClass.getName());
    println("Class definition: " + iClass.getDefinition());

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
    println("Operation name: " + iOperation.getName());
    println("Operation returnType: " + iOperation.getReturnTypeExpression());
    println("Operation definition: " + iOperation.getDefinition());
}

function printAttributeInfo(iAttribute) {
    println("Attribute name: " + iAttribute.getName());
    println("Attribute type: " + iAttribute.getTypeExpression());
    println("Attribute definition: " + iAttribute.getDefinition());
}
