package com.baze3.features.costSheet

import com.baze3.common.db.DbResult
import com.baze3.common.db.Repository
import com.baze3.features.costSheet.CostSheetSQL.findDetailsWithItems
import com.baze3.models.CostItemDTO
import com.baze3.models.CostSheetDTO
import com.baze3.models.CostSheetDetailsDTO
import java.sql.ResultSet
import java.sql.Timestamp
import javax.sql.DataSource

class CostRepository(ds: DataSource) : Repository(ds) {

    fun getAllSheets(): DbResult<List<CostSheetDTO>> =
        executeQuery(CostSheetSQL.findAll, mapper = ::mapSheetRow)

    fun getSheetById(id: Long): DbResult<CostSheetDetailsDTO?> {
        return executeTransaction { conn ->
            val items = mutableListOf<CostItemDTO>()
            var sheet: CostSheetDetailsDTO? = null

            conn.prepareStatement(findDetailsWithItems).use { stmt ->
                stmt.setLong(1, id)
                val rs = stmt.executeQuery()

                while (rs.next()) {
                    if (sheet == null) {
                        sheet = CostSheetDetailsDTO(
                            id = rs.getLong(1),
                            date = rs.getTimestamp(2).time,
                            totalAmount = rs.getDouble(3),
                            caseNumber = rs.getString(4),
                            personalId = rs.getString(5),
                            items = emptyList() // Popunićemo kasnije
                        )
                    }

                    val ordinalId = rs.getString(6)
                    if (ordinalId != null) {
                        items.add(
                            CostItemDTO(
                                costSheetId = rs.getLong(1),
                                ordinalId = ordinalId,
                                type = rs.getString(7),
                                quantity = rs.getDouble(8),
                                purpose = rs.getString(9),
                                unitPrice = rs.getDouble(10)
                            )
                        )
                    }
                }
            }
            sheet?.copy(items = items)
        }
    }

    fun insertFullSheet(sheet: CostSheetDetailsDTO): DbResult<Boolean> {
        return executeTransaction { conn ->
            conn.prepareStatement(CostSheetSQL.insert).use { stmt ->
                stmt.setLong(1, sheet.id ?: 0L)
                stmt.setTimestamp(2, Timestamp(sheet.date ?: 0L))
                stmt.setDouble(3, sheet.totalAmount ?: 0.0)
                stmt.setString(4, sheet.caseNumber)
                stmt.setString(5, sheet.personalId)
                stmt.executeUpdate()
            }

            conn.prepareStatement(CostItemSQL.insert).use { stmt ->
                for (item in sheet.items) {
                    stmt.setLong(1, sheet.id ?: 0L)
                    stmt.setString(2, item.ordinalId)
                    stmt.setString(3, item.type)
                    stmt.setDouble(4, item.quantity ?: 0.0)
                    stmt.setString(5, item.purpose)
                    stmt.setDouble(6, item.unitPrice ?: 0.0)
                    stmt.addBatch()
                }
                stmt.executeBatch()
            }
            true
        }
    }

    fun updateSheet(sheet: CostSheetDetailsDTO): DbResult<Boolean> {
        return executeTransaction { conn ->
            conn.prepareStatement(CostSheetSQL.update).use { stmt ->
                stmt.setTimestamp(1, Timestamp(sheet.date ?: 0L))
                stmt.setDouble(2, sheet.totalAmount ?: 0.0)
                stmt.setString(3, sheet.caseNumber)
                stmt.setString(4, sheet.personalId)
                stmt.setLong(5, sheet.id ?: 0L)
                stmt.executeUpdate()
            }

            conn.prepareStatement(CostItemSQL.deleteAllForParent).use { stmt ->
                stmt.setLong(1, sheet.id ?: 0L)
                stmt.executeUpdate()
            }

            conn.prepareStatement(CostItemSQL.insert).use { stmt ->
                sheet.items.forEach { item ->
                    stmt.setLong(1, sheet.id ?: 0L)
                    stmt.setString(2, item.ordinalId)
                    stmt.setString(3, item.type)
                    stmt.setDouble(4, item.quantity ?: 0.0)
                    stmt.setString(5, item.purpose)
                    stmt.setDouble(6, item.unitPrice ?: 0.0)
                    stmt.addBatch()
                }
                stmt.executeBatch()
            }
            true
        }
    }

    fun deleteSheetFully(id: Long): DbResult<Int> {
        return executeTransaction { conn ->
            conn.prepareStatement(CostItemSQL.deleteAllForParent).use { it.setLong(1, id); it.executeUpdate() }
            val affectedRows = conn.prepareStatement(CostSheetSQL.delete).use {
                it.setLong(1, id)
                it.executeUpdate()
            }
            affectedRows
        }
    }

    fun getItemsBySheetId(sheetId: Long): DbResult<List<CostItemDTO>> =
        executeQuery(CostItemSQL.findByParentId, sheetId, mapper = ::mapItemRow)

    fun createItem(item: CostItemDTO): DbResult<Int> =
        executeUpdate(
            CostItemSQL.insert,
            item.costSheetId, item.ordinalId, item.type, item.quantity, item.purpose, item.unitPrice
        )

    fun updateItem(item: CostItemDTO): DbResult<Int> =
        executeUpdate(
            CostItemSQL.update,
            item.type, item.quantity, item.purpose, item.unitPrice, item.costSheetId, item.ordinalId
        )

    fun deleteItem(sheetId: Long, ordinalId: String): DbResult<Int> =
        executeUpdate(CostItemSQL.delete, sheetId, ordinalId)

    private fun mapSheetRow(rs: ResultSet) = CostSheetDTO(
        id = rs.getLong("ID_TROSKOVNIKA"),
        date = rs.getTimestamp("DATUM")?.time,
        totalAmount = rs.getDouble("UKUPNO"),
        caseNumber = rs.getString("BROJ_PREDMETA"),
        personalId = rs.getString("JMBG")
    )

    private fun mapItemRow(rs: ResultSet) = CostItemDTO(
        costSheetId = rs.getLong("ID_TROSKOVNIKA"),
        ordinalId = rs.getString("RB"),
        type = rs.getString("VRSTA"),
        quantity = rs.getDouble("BROJ_JEDINICA"),
        purpose = rs.getString("NAMENA"),
        unitPrice = rs.getDouble("CENA_JEDINICE")
    )
}

fun CostItemEntity.toDTO() = CostItemDTO(
    costSheetId = this.costSheetId,
    ordinalId = this.ordinalId,
    type = this.type,
    quantity = this.quantity,
    purpose = this.purpose,
    unitPrice = this.unitPrice,
)

fun CostSheetEntity.toDetailsDTO(items: List<CostItemEntity>) = CostSheetDetailsDTO(
    id = this.id,
    date = this.date,
    totalAmount = this.totalAmount,
    caseNumber = this.caseNumber,
    personalId = this.personalId,
    items = items.map { it.toDTO() }
)

fun CostSheetDTO.toEntity() = CostSheetEntity(
    id = this.id ?: 0L,
    date = this.date,
    totalAmount = this.totalAmount,
    caseNumber = this.caseNumber,
    personalId = this.personalId
)

fun CostItemDTO.toEntity(parentId: Long) = CostItemEntity(
    costSheetId = parentId,
    ordinalId = this.ordinalId ?: "",
    type = this.type,
    quantity = this.quantity,
    purpose = this.purpose,
    unitPrice = this.unitPrice
)