package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV8 {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}