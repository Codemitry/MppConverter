package visitor

import copyConstructorPropertiesToBody
import deleteModifiersIncompatibleWithExpect
import org.jetbrains.kotlin.psi.*
import replaceConstructorPropertiesWithParameters

class KtRealizationEraserVisitorVoid : KtTreeVisitorVoid() {
    override fun visitProperty(property: KtProperty) {
        println("VISIT property")

        super.visitProperty(property)

        property.deleteModifiersIncompatibleWithExpect()
        property.delegate?.delete()
        property.initializer?.delete()
        property.equalsToken?.delete()
        property.setter?.delete()
        property.getter?.delete()
    }
    override fun visitNamedFunction(function: KtNamedFunction) {
        println("VISIT function")
        super.visitNamedFunction(function)

        function.deleteModifiersIncompatibleWithExpect()
        function.bodyExpression?.delete()
        function.equalsToken?.delete()
    }

    override fun visitClass(klass: KtClass) {
        println("VISIT class")
        super.visitClass(klass)

        klass.deleteModifiersIncompatibleWithExpect()

        klass.copyConstructorPropertiesToBody()
        klass.replaceConstructorPropertiesWithParameters()
    }
}