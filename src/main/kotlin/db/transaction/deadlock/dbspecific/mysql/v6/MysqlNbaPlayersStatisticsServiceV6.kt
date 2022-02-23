package db.transaction.deadlock.dbspecific.mysql.v6

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV6
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MysqlNbaPlayersStatisticsServiceV6(
    private val mysqlNbaPlayerRepository: MysqlNbaPlayerRepositoryV6,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV6 {

    @Transactional("mysqlJpaTransactionManager")
    override fun publishYoungestPlayers(number: Int) {
        val youngest = mysqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mysqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("mysqlJpaTransactionManager")
    override fun publishOldestPlayers(number: Int) {
        val oldest = mysqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mysqlNbaPlayerRepository.saveAll(oldest)
    }
}
