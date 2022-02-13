package db.transaction.deadlock.dbspecific.postgresql.v2

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV1
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV2(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV2,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV1 {

    @Transactional("postgresqlJpaTransactionManager")
    override fun publishYoungestPlayer() {
        val youngest = postgresqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        postgresqlNbaPlayerRepository.save(youngest)
    }
}
