package db.transaction.deadlock.service

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.stereotype.Service


@Service
class NbaPublisher {
    private val log = logger {}

    fun publish(nbaPlayer: NbaPlayer) {
        log.info("Publishing nba player: $nbaPlayer")
    }
}