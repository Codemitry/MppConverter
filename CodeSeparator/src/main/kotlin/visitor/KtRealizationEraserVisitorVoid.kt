package visitor

import incompatibleWithExpectFunModifiers
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

class KtRealizationEraserVisitorVoid : KtTreeVisitorVoid() {
    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        for (modifier in incompatibleWithExpectFunModifiers) {
            property.removeModifier(modifier)
        }
        property.delegateExpressionOrInitializer?.delete()
        property.equalsToken?.delete()
        property.setter?.delete()
        property.getter?.delete()
    }
    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        for (modifier in incompatibleWithExpectFunModifiers) {
            function.removeModifier(modifier)
        }
        function.bodyExpression?.delete()
        function.equalsToken?.delete()
    }

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        for (modifier in incompatibleWithExpectFunModifiers) {
            klass.removeModifier(modifier)
        }
    }
}