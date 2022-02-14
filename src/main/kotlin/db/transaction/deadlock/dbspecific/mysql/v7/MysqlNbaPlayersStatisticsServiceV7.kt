package db.transaction.deadlock.dbspecific.mysql.v7

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV7
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class MysqlNbaPlayersStatisticsServiceV7(
    private val mysqlNbaPlayerRepository: MysqlNbaPlayerRepositoryV7,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV7 {

    @Transactional("mysqlJpaTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun publishYoungestPlayers(number: Int) {
        val youngest = mysqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mysqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("mysqlJpaTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun publishOldestPlayers(number: Int) {
        val oldest = mysqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mysqlNbaPlayerRepository.saveAll(oldest)
    }
}
