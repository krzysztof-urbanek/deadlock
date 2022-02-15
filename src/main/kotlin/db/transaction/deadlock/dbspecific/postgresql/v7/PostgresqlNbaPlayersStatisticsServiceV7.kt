package db.transaction.deadlock.dbspecific.postgresql.v7

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV7
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV7(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV7,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV7 {

    @Transactional("postgresqlJpaTransactionManager")
    override fun publishYoungestPlayers(number: Int) {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("postgresqlJpaTransactionManager")
    override fun publishOldestPlayers(number: Int) {
        val oldest = postgresqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(oldest)
    }
}
