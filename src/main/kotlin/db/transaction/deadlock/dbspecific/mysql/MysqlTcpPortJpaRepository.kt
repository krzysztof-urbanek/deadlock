package db.transaction.deadlock.dbspecific.mysql

import db.transaction.deadlock.model.TcpPort
import org.springframework.data.jpa.repository.JpaRepository

interface MysqlTcpPortJpaRepository : JpaRepository<TcpPort, Long> {

    fun findByPortNumber(portNumber: Int): TcpPort?
}