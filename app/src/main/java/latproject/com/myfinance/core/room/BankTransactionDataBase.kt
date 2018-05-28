package latproject.com.roomtutorial

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(BankTransaction::class)], version = 1, exportSchema = false)
abstract class BankTransactionDataBase : RoomDatabase() {
   abstract fun getPersonDataAccess(): BankTransactionDao
}