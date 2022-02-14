package db.transaction.deadlock.dbspecific.mysql.v0

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV0
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service


@Service
class MysqlNbaPlayersStatisticsServiceV0(
    private val mysqlNbaPlayerRepository: MysqlNbaPlayerRepositoryV0,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV0 {

    override fun publishYoungestPlayer() {
        val youngest = mysqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        mysqlNbaPlayerRepository.save(youngest)
    }
}
