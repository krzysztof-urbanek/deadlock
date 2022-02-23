package db.transaction.deadlock.dbspecific.postgresql.v5

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV5
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV5(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV5,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV5 {

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
