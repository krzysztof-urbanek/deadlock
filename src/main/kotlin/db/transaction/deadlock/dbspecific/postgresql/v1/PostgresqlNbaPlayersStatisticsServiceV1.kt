package db.transaction.deadlock.dbspecific.postgresql.v1

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV1
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostgresqlNbaPlayersStatisticsServiceV1(
    private val postgresqlNbaPlayerRepository: PostgresqlNbaPlayerRepositoryV1,
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
