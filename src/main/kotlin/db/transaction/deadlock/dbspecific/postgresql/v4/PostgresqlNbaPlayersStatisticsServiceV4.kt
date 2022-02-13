package db.transaction.deadlock.dbspecific.postgresql.v4

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV4
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service


@Service
class PostgresqlNbaPlayersStatisticsServiceV4(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV4,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV4 {

    override fun publishYoungestPlayers(number: Int) {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(youngest)
    }

    override fun publishOldestPlayers(number: Int) {
        val oldest = postgresqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        postgresqlNbaPlayerRepository.saveAll(oldest)
    }
}
