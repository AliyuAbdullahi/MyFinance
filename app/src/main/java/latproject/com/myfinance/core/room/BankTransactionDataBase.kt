package latproject.com.myfinance.core.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import latproject.com.roomtutorial.BankTransactionDao

@Database(entities = [BankTransaction::class, User::class, Budget::class], version = 1, exportSchema = false)
abstract class BankTransactionDataBase : RoomDatabase() {
   abstract fun getTransactionsDataAccess(): BankTransactionDao
   abstract fun getUserDataAccess(): UserDao
   abstract fun getBudgetDataAccess(): BudgetDao
}