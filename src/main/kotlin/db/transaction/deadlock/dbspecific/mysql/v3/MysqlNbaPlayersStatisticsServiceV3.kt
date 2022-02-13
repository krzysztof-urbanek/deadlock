package db.transaction.deadlock.dbspecific.mysql.v3

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV1
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MysqlNbaPlayersStatisticsServiceV3(
    private val mysqlNbaPlayerRepository: MysqlNbaPlayerRepositoryV3,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV1 {

    @Transactional("mysqlJpaTransactionManager")
    override fun publishYoungestPlayer() {
        val youngest = mysqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        mysqlNbaPlayerRepository.save(youngest)
    }
}
