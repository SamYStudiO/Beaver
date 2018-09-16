package net.samystudio.beaver.ext

import org.junit.Assert.assertEquals
import org.junit.Test

class AnyKtTest {

    @Test
    fun testGetMethodTag() {
        assertEquals("AnyKtTestgetMethodTag", getMethodTag())
    }
}