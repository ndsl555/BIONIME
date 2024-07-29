package com.example.bionime.repository

import com.example.bionime.data.Mask
import com.example.bionime.data.MaskDao
import com.example.bionime.network.MaskApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class MaskRepository(private val maskDao: MaskDao) {
    private val api = MaskApiService.api

    suspend fun refreshMasks() {
        try {
            val response = api.getMasks()
            val masks =
                response.features.filter { it.properties.county == "臺中市" }.map { feature ->
                    Mask(
                        id = feature.properties.id,
                        name = feature.properties.name,
                        address = feature.properties.address,
                        maskAdult = feature.properties.mask_adult,
                        maskChild = feature.properties.mask_child,
                        town = feature.properties.town
                    )
                }
            maskDao.insertMasks(masks)
        } catch (e: Exception) {
            // 斷往後繼續使用localdb的數據
        }
    }

    suspend fun fetchQuote(): String = withContext(Dispatchers.IO) {
        try {
            val doc = Jsoup.connect("https://www.managertoday.com.tw/quotes?page=1").get()
            doc.select("h2.text-2xl.font-medium").first()?.text() ?: "No quote found"
        } catch (e: Exception) {
            "Failed to fetch quote"
        }
    }

    suspend fun getMasksByTown(town: String) = maskDao.getMasksByTown(town)
    suspend fun getAllMasks() = maskDao.getAllMasks()
    suspend fun getAllTowns() = maskDao.getAllTowns()
    suspend fun deleteMaskByName(name: String) = maskDao.deleteMaskByName(name)
}