package net.samystudio.beaver.ext

import org.junit.Assert.assertEquals
import org.junit.Test

class AnyTest {

    @Test
    fun testGetMethodTag() {
        assertEquals("net.samystudio.beaver.ext.AnyTest::testGetMethodTag", getMethodTag())
    }
}