package com.iceteaviet.fastfoodfinder.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatUtilsTest {
    @Test
    fun getTrimmedShortInstruction_normal() {
        assertEquals("some text", getTrimmedShortInstruction("some text"))
    }

    @Test
    fun getTrimmedShortInstruction_empty() {
        assertEquals("", getTrimmedShortInstruction(""))
    }

    @Test
    fun getTrimmedShortInstruction_null() {
        assertEquals("", getTrimmedShortInstruction(null))
    }

    @Test
    fun formatDistance_floating() {
        assertEquals("5.5 Km", formatDistance(5.5))
    }

    @Test
    fun formatDistance_normal() {
        assertEquals("7 Km", formatDistance(7.0))
    }

    @Test
    fun formatDistance_negativeNumb() {
        assertEquals("-2 Km", formatDistance(-2.0))
        assertEquals("-2.3 Km", formatDistance(-2.3))
    }

    @Test
    fun formatDistance_zero() {
        assertEquals("0 Km", formatDistance(0.0))
    }

    @Test
    fun normalizeDistrictQuery_randomText() {
        assertThat(standardizeDistrictQuery(" some text  ")).contains("some text")
        assertThat(standardizeDistrictQuery("some text")).contains("some text")
        assertThat(standardizeDistrictQuery("some address, phuong x, quan y, Saigon")).contains("some address, phuong x, quan y, saigon")
    }

    @Test
    fun normalizeDistrictQuery_vi() {
        assertThat(standardizeDistrictQuery("quan 1")).contains("quận 1", "Quận 1", "quan 1", "Quan 1", "q1", "District 1", "district 1")
        assertThat(standardizeDistrictQuery("quan 2 ")).contains("quận 2", "Quận 2", "quan 2", "Quan 2", "q2", "District 2", "district 2")
        assertThat(standardizeDistrictQuery(" quan tan binh  ")).contains("Tân Bình", "tân bình", "Tan Binh", "tan binh")
        assertThat(standardizeDistrictQuery("tân phú")).contains("Tân Phú", "tân phú", "Tan Phu", "tan phu")
        assertThat(standardizeDistrictQuery("quận gò vấp")).contains("gò vấp", "Gò Vấp", "go vap", "Go Vap")
        assertThat(standardizeDistrictQuery("quan 3")).contains("quận 3", "Quận 3", "quan 3", "Quan 3", "q3", "District 3", "district 3")
        assertThat(standardizeDistrictQuery("quan 5")).contains("quận 5", "Quận 5", "quan 5", "Quan 5", "q5", "District 5", "district 5")
    }

    @Test
    fun normalizeDistrictQuery_en() {
        assertThat(standardizeDistrictQuery("district 8")).contains("quận 8", "Quận 8", "quan 8", "Quan 8", "q8", "District 8", "district 8")
        assertThat(standardizeDistrictQuery("11 district")).contains("quận 11", "Quận 11", "quan 11", "Quan 11", "q11", "District 11", "district 11")
        assertThat(standardizeDistrictQuery(" district Binh Chanh  ")).contains("Bình Chánh", "bình chánh", "Binh Chanh", "binh chanh")
        assertThat(standardizeDistrictQuery("binh Tan district ")).contains("Bình Tân", "bình tân", "Binh Tan", "binh tan")
        assertThat(standardizeDistrictQuery("district thu duc")).contains("Thủ Đức", "thủ đức", "Thu Duc", "thu duc")
    }

    @Test
    fun normalizeDistrictQuery_empty() {
        assertThat(standardizeDistrictQuery("")).isEmpty()
    }

    @Test
    fun getStoreTypeFromQuery_normal() {
        assertEquals(StoreType.TYPE_CIRCLE_K, getStoreTypeFromQuery("circle K"))
        assertEquals(StoreType.TYPE_CIRCLE_K, getStoreTypeFromQuery("circle K  "))
        assertEquals(StoreType.TYPE_CIRCLE_K, getStoreTypeFromQuery(" circleK"))

        assertEquals(StoreType.TYPE_BSMART, getStoreTypeFromQuery("bmart"))
        assertEquals(StoreType.TYPE_BSMART, getStoreTypeFromQuery("bs'mart"))
        assertEquals(StoreType.TYPE_BSMART, getStoreTypeFromQuery("b's mart"))

        assertEquals(StoreType.TYPE_SHOP_N_GO, getStoreTypeFromQuery("shopngo"))
        assertEquals(StoreType.TYPE_SHOP_N_GO, getStoreTypeFromQuery(" shop n go"))
        assertEquals(StoreType.TYPE_SHOP_N_GO, getStoreTypeFromQuery("shopandgo"))
        assertEquals(StoreType.TYPE_SHOP_N_GO, getStoreTypeFromQuery(" shop and go  "))

        assertEquals(StoreType.TYPE_FAMILY_MART, getStoreTypeFromQuery("familymart"))
        assertEquals(StoreType.TYPE_FAMILY_MART, getStoreTypeFromQuery("famima"))
        assertEquals(StoreType.TYPE_FAMILY_MART, getStoreTypeFromQuery("family mart"))
        assertEquals(StoreType.TYPE_FAMILY_MART, getStoreTypeFromQuery(" family mart  "))

        assertEquals(StoreType.TYPE_MINI_STOP, getStoreTypeFromQuery("ministop"))
        assertEquals(StoreType.TYPE_MINI_STOP, getStoreTypeFromQuery("mini stop"))
    }

    @Test
    fun getStoreTypeFromQuery_empty() {
        assertEquals(-1, getStoreTypeFromQuery(""))
    }
}