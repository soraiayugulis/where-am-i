package com.whereami.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class StatusTest {
    @Test
    fun `status only accepts COMPLETED and INCOMPLETE`() {
        val values = Status.values()
        assertEquals(2, values.size)
        assertEquals(Status.COMPLETED, Status.valueOf("COMPLETED"))
        assertEquals(Status.INCOMPLETE, Status.valueOf("INCOMPLETE"))
    }
}
