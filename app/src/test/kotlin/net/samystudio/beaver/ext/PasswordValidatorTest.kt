package net.samystudio.beaver.ext

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun testValidateEmptyPassword() {
        assertFalse(PASSWORD_VALIDATOR.validate(""))
    }

    @Test
    fun testValidateShortPassword() {
        assertFalse(PASSWORD_VALIDATOR.validate("azertyu"))
    }

    @Test
    fun testValidateOkPassword() {
        assertTrue(PASSWORD_VALIDATOR.validate("azertyui"))
    }
}