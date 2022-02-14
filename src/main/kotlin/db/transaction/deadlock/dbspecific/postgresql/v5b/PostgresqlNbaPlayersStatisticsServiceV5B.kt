package db.transaction.deadlock.dbspecific.postgresql.v5b

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV5
import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV5B
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV5B(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV5B,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV5B {

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
