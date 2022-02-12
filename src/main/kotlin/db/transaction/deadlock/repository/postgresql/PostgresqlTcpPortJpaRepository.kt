package db.transaction.deadlock.repository.postgresql

import db.transaction.deadlock.model.TcpPort
import org.springframework.data.jpa.repository.JpaRepository

interface PostgresqlTcpPortJpaRepository : JpaRepository<TcpPort, Long> {

    fun findByPortNumber(portNumber: Int): TcpPort?
}