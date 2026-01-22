package com.baze3.features.costSheet

import com.baze3.common.db.DbResult
import com.baze3.models.ApiResponse
import com.baze3.models.CostItemDTO
import com.baze3.models.CostSheetDTO
import com.baze3.models.CostSheetDetailsDTO

class CostService(private val repository: CostRepository) {

    fun getAllSheets(): ApiResponse<List<CostSheetDTO>> {
        return when (val res = repository.getAllSheets()) {
            is DbResult.Success -> ApiResponse(success = true, message = "Success", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun getSheetById(id: Long): ApiResponse<CostSheetDetailsDTO?> {
        return when (val res = repository.getSheetById(id)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Success", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun createSheet(sheet: CostSheetDetailsDTO): ApiResponse<Boolean> {
        return when (val res = repository.insertFullSheet(sheet)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Sheet created", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun updateSheet(sheet: CostSheetDetailsDTO): ApiResponse<Boolean> {
        return when (val res = repository.updateSheet(sheet)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Update successful", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun deleteSheet(id: Long): ApiResponse<Int> {
        return when (val res = repository.deleteSheetFully(id)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Delete successful", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun getItemsBySheetId(sheetId: Long): ApiResponse<List<CostItemDTO>> {
        return when (val res = repository.getItemsBySheetId(sheetId)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Success", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun createItem(item: CostItemDTO): ApiResponse<Int> {
        return when (val res = repository.createItem(item)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Item created", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun updateItem(item: CostItemDTO): ApiResponse<Int> {
        return when (val res = repository.updateItem(item)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Item updated", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }

    fun deleteItem(sheetId: Long, ordinalId: String): ApiResponse<Int> {
        return when (val res = repository.deleteItem(sheetId, ordinalId)) {
            is DbResult.Success -> ApiResponse(success = true, message = "Item deleted", data = res.data)
            is DbResult.Error -> ApiResponse(success = false, message = res.message)
        }
    }
}