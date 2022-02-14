package db.transaction.deadlock.dbspecific.postgresql.v8

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV8
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV8(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV8,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV8 {

    @Transactional("postgresqlJpaTransactionManager", isolation = Isolation.SERIALIZABLE)
    override fun publishYoungestPlayers(number: Int) {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("postgresqlJpaTransactionManager", isolation = Isolation.REPEATABLE_READ)
    override fun publishOldestPlayers(number: Int) {
        val oldest = postgresqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(oldest)
    }
}
