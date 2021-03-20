package net.samystudio.beaver.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AnyTest {

    @Test
    fun testGetMethodTag() {
        assertEquals("net.samystudio.beaver.util.AnyTest::testGetMethodTag", getMethodTag())
    }
}
