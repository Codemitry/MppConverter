import com.intellij.psi.PsiElement
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ActualClassExtractorTest {
    
    private fun assertPsi(expectedPsi: PsiElement, actualPsi: PsiElement) {
        assertEquals(expectedPsi.text.trim(), actualPsi.text.trim())
    }

    private val psiCreator = PSICreator()

    @Test
    fun simpleClassTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClass/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleClass/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }

    @Test
    fun simpleDataClassTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleDataClass/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleDataClass/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }

    @Test
    fun simpleEnumClassTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleEnumClass/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleEnumClass/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }

    @Test
    fun simpleClassWithPropertiesTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClassWithProperties/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleClassWithProperties/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }

    @Test
    fun simpleClassWithConstructorTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClassWithConstructor/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleClassWithConstructor/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }

    @Test
    fun simpleClassWithSecondaryConstructorTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClassWithSecondaryConstructor/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleClassWithSecondaryConstructor/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }

    @Test
    fun simpleClassWithNestedClass() {
        val psi = psiCreator.getPSIForFile("$res/simpleClassWithNestedClass/simpleClass.kt")
        val actualPsi = psiCreator.getPSIForFile("$res/simpleClassWithNestedClass/simpleClassActual.kt")

        assertPsi(actualPsi, psi.getActual())
    }
}