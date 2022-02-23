package db.transaction.deadlock.dbspecific.postgresql.v0

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV0
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service


@Service
class PostgresqlNbaPlayersStatisticsServiceV0(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV0,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV0 {

    override fun publishYoungestPlayer() {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        postgresqlNbaPlayerRepository.save(youngest)
    }
}
