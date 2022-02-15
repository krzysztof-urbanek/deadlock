package db.transaction.deadlock.dbspecific.mysql.v8

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV8
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class MysqlNbaPlayersStatisticsServiceV8(
    private val mysqlNbaPlayerRepository: MysqlNbaPlayerRepositoryV8,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV8 {

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
