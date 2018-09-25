package net.samystudio.beaver.ext

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun testValidateEmptyPassword() {
        assertFalse(PasswordValidator().validate(""))
    }

    @Test
    fun testValidateShortPassword() {
        assertFalse(PasswordValidator().validate("azertyu"))
    }

    @Test
    fun testValidateOkPassword() {
        assertTrue(PasswordValidator().validate("azertyui"))
    }
}