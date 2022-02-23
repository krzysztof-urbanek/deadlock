package db.transaction.deadlock.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table
data class NbaPlayer(
    @Column @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val ordinalId: Long? = null,
    @Column val name: String,
    @Column val birthdate: LocalDate,
    @Column var mentions: Long = 0,
)
