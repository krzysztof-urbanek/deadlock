package db.transaction.deadlock.dbspecific.postgresql.v9

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV9
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV9(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV9,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV9 {

    @Transactional("postgresqlJpaTransactionManager", isolation = Isolation.SERIALIZABLE)
    override fun publishYoungestPlayers(number: Int) {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("postgresqlJpaTransactionManager", isolation = Isolation.SERIALIZABLE)
    override fun publishOldestPlayers(number: Int) {
        val oldest = postgresqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(oldest)
    }
}
