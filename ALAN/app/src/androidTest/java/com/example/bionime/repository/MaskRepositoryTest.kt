package com.example.bionime.repository

import com.example.bionime.data.Mask
import com.example.bionime.data.MaskDao
import com.example.bionime.network.MaskApi
import com.example.bionime.network.MaskResponse
import com.example.bionime.network.Feature
import com.example.bionime.network.Properties
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class MaskRepositoryTest {

    private lateinit var maskDao: MaskDao
    private lateinit var maskRepository: MaskRepository
    private lateinit var maskApi: MaskApi

    @Before
    fun setUp() {
        mockkStatic(Jsoup::class)

        maskDao = mockk(relaxed = true)
        maskApi = mockk()
        maskRepository = MaskRepository(maskDao)
    }

    @After
    fun tearDown() {
        // Clean up any resources if necessary
        unmockkAll()

    }

    @Test
    fun testRefreshMasks() = runTest {
        // Given
        val mockResponse = MaskResponse(
            features = listOf(
                Feature(
                    properties = Properties(
                        id = "1",
                        name = "Mask Shop 1",
                        address = "123 Mask Street",
                        mask_adult = 100,
                        mask_child = 50,
                        county = "臺中市",
                        town = "Some Town"
                    )
                ),
                Feature(
                    properties = Properties(
                        id = "2",
                        name = "Mask Shop 2",
                        address = "456 Mask Avenue",
                        mask_adult = 200,
                        mask_child = 150,
                        county = "臺中市",
                        town = "Another Town"
                    )
                )
            )
        )

        coEvery { maskApi.getMasks() } returns mockResponse

        // When
        maskRepository.refreshMasks()

        // Then
        coVerify { maskDao.insertMasks(any()) }
    }


    @Test
    fun testFetchQuote() = runTest {
        // Given
        val mockHtml = "<html><body><h2 class='text-2xl font-medium'>Test Quote</h2></body></html>"
        val mockDocument: Document = Jsoup.parse(mockHtml)
        coEvery {
            Jsoup.connect("https://www.managertoday.com.tw/quotes?page=1").get()
        } returns mockDocument

        // When
        val quote = maskRepository.fetchQuote()

        // Then
        assertEquals("Test Quote", quote)
    }

    @Test
    fun testGetMasksByTown() = runBlockingTest {
        // Given
        val town = "Some Town"
        val mockMasks = listOf(
            Mask("1", "Mask Shop 1", "123 Mask Street", 100, 50, town)
        )
        coEvery { maskDao.getMasksByTown(town) } returns mockMasks

        // When
        val result = maskRepository.getMasksByTown(town)

        // Then
        assertEquals(mockMasks, result)
    }

    @Test
    fun testGetAllMasks() = runBlockingTest {
        // Given
        val mockMasks = listOf(
            Mask("1", "Mask Shop 1", "123 Mask Street", 100, 50, "Some Town"),
            Mask("2", "Mask Shop 2", "456 Mask Avenue", 200, 150, "Another Town")
        )
        coEvery { maskDao.getAllMasks() } returns mockMasks

        // When
        val result = maskRepository.getAllMasks()

        // Then
        assertEquals(mockMasks, result)
    }

    @Test
    fun testGetAllTowns() = runBlockingTest {
        // Given
        val mockTowns = listOf("Some Town", "Another Town")
        coEvery { maskDao.getAllTowns() } returns mockTowns

        // When
        val result = maskRepository.getAllTowns()

        // Then
        assertEquals(mockTowns, result)
    }

    @Test
    fun testDeleteMaskByName() = runBlockingTest {
        // Given
        val maskName = "Mask Shop 1"
        coEvery { maskDao.deleteMaskByName(maskName) } returns Unit

        // When
        maskRepository.deleteMaskByName(maskName)

        // Then
        coVerify { maskDao.deleteMaskByName(maskName) }
    }
}
