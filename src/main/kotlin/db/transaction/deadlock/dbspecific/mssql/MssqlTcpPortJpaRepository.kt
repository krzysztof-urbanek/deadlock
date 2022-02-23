package db.transaction.deadlock.dbspecific.mssql

import db.transaction.deadlock.model.TcpPort
import org.springframework.data.jpa.repository.JpaRepository

interface MssqlTcpPortJpaRepository : JpaRepository<TcpPort, Long> {

    fun findByPortNumber(portNumber: Int): TcpPort?
}