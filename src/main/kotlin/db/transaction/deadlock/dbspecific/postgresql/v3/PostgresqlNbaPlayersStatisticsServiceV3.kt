package db.transaction.deadlock.dbspecific.postgresql.v3

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV3
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV3(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV3,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV3 {

    @Transactional("postgresqlJpaTransactionManager")
    override fun publishYoungestPlayer() {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        postgresqlNbaPlayerRepository.save(youngest)
    }
}
