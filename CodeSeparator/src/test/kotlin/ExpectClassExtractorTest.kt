import com.intellij.psi.PsiElement
import com.spbpu.mppconverter.kootstrap.PSICreator
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ExpectClassExtractorTest {

    private fun findKtClass(psi: KtFile): KtClass {
        return psi.findChildByClass(KtClass::class.java) as KtClass
    }

    private fun assertPsi(expectedPsi: PsiElement, actualPsi: PsiElement) {
        assertEquals(expectedPsi.text.trim(), actualPsi.text.trim())
    }

    private val psiCreator = PSICreator()

    @Test
    fun simpleClassTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClass/simpleClass.kt")
        val expectPsi = psiCreator.getPSIForFile("$res/simpleClass/simpleClassExpect.kt")

        assertPsi(findKtClass(expectPsi), getExpectClass(findKtClass(psi)))
    }

    @Test
    fun simpleClassWithPropertiesTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClassWithProperties/simpleClass.kt")
        val expectPsi = psiCreator.getPSIForFile("$res/simpleClassWithProperties/simpleClassExpect.kt")

        assertPsi(findKtClass(expectPsi), getExpectClass(findKtClass(psi)))
    }

    @Test
    fun simpleClassWithConstructorTest() {
        val psi = psiCreator.getPSIForFile("$res/simpleClassWithConstructor/simpleClass.kt")
        val expectPsi = psiCreator.getPSIForFile("$res/simpleClassWithConstructor/simpleClassExpect.kt")

        assertPsi(findKtClass(expectPsi), getExpectClass(findKtClass(psi)))
    }
}