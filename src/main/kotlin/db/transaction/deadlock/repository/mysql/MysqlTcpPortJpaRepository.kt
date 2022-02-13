package db.transaction.deadlock.repository.mysql

import db.transaction.deadlock.model.TcpPort
import org.springframework.data.jpa.repository.JpaRepository

interface MysqlTcpPortJpaRepository : JpaRepository<TcpPort, Long> {

    fun findByPortNumber(portNumber: Int): TcpPort?
}