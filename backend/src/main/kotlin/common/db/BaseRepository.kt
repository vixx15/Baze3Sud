package com.baze3.common.db

import java.sql.Connection
import java.sql.ResultSet
import javax.sql.DataSource

abstract class BaseRepository<T>(private val dataSource: DataSource) {

    abstract fun mapRow(rs: ResultSet): T

    protected fun queryList(sql: String, vararg params: Any): List<T> {
        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                params.forEachIndexed { index, param ->
                    stmt.setObject(index + 1, param)
                }
                val rs = stmt.executeQuery()
                val result = mutableListOf<T>()
                while (rs.next()) {
                    result.add(mapRow(rs))
                }
                result
            }
        }
    }

    protected fun queryOne(sql: String, vararg params: Any): T? {
        return queryList(sql, *params).firstOrNull()
    }

    protected fun execute(sql: String, vararg params: Any): Int {
        return dataSource.connection.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                params.forEachIndexed { index, param ->
                    stmt.setObject(index + 1, param)
                }
                stmt.executeUpdate()
            }
        }
    }
}

abstract class Repository(protected val ds: DataSource) {

    protected fun <T> safeDbCall(block: () -> T): DbResult<T> {
        return try {
            DbResult.Success(block())
        } catch (e: java.sql.SQLException) {
            val errorCode = e.errorCode
            val rawMessage = e.message ?: "Nepoznata greška u bazi"

            val cleanMessage = rawMessage.substringAfter(":").trim()

            DbResult.Error(
                message = cleanMessage,
                code = "ORA-$errorCode",
                isValidationError = errorCode in 20000..20999 || errorCode == 1 || errorCode == 2291
            )
        } catch (e: Exception) {
            DbResult.Error(message = "Sistemska greška: ${e.localizedMessage}")
        }
    }

    protected fun <R> executeQuery(sql: String, vararg params: Any?, mapper: (ResultSet) -> R): DbResult<List<R>> {
        return safeDbCall {
            ds.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    println("DEBUG SQL: $sql")
                    println("DEBUG PARAMS SIZE: ${params.size}")
                    params.forEachIndexed { index, param ->
                        println("DEBUG BINDING: Index ${index + 1} = $param")
                        stmt.setObject(index + 1, param) }

                    val rs = stmt.executeQuery()
                    val results = mutableListOf<R>()
                    while (rs.next()) {
                        results.add(mapper(rs))
                    }
                    results
                }
            }
        }
    }

    protected fun <R> executeQueryOne(sql: String, vararg params: Any?, mapper: (ResultSet) -> R): DbResult<R?> {
        return when (val result = executeQuery(sql, *params, mapper = mapper)) {
            is DbResult.Success -> DbResult.Success(result.data.firstOrNull())
            is DbResult.Error -> result
        }
    }

    protected fun executeUpdate(sql: String, vararg params: Any?): DbResult<Int> {
        return safeDbCall {
            ds.connection.use { conn ->
                conn.prepareStatement(sql).use { stmt ->
                    params.forEachIndexed { index, param ->
                        stmt.setObject(index + 1, param)
                    }
                    stmt.executeUpdate()
                }
            }
        }
    }

    fun <T> executeTransaction(block: (Connection) -> T): DbResult<T> {
        val connection = ds.connection
        return try {
            connection.autoCommit = false
            val result = block(connection)
            connection.commit()
            DbResult.Success(result)
        } catch (e: Exception) {
            connection.rollback()
            DbResult.Error(e.message ?: "Transaction failed")
        } finally {
            connection.autoCommit = true
            connection.close()
        }
    }
}