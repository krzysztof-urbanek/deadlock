package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV5B {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}