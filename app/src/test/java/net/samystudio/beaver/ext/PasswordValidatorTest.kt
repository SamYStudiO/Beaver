package net.samystudio.beaver.ext

import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun testValidateEmptyPassword() {
        assertEquals(false, PasswordValidator().validate(""))
    }

    @Test
    fun testValidateShortPassword() {
        assertEquals(false, PasswordValidator().validate("azertyu"))
    }

    @Test
    fun testValidateOkPassword() {
        assertEquals(true, PasswordValidator().validate("azertyui"))
    }
}