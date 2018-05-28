package latproject.com.myfinance.core.room

import android.arch.persistence.room.Room
import android.content.Context
import latproject.com.roomtutorial.BankTransactionDataBase
import javax.inject.Inject

class LocalDatabaseManager @Inject constructor(context: Context) {
    var database: BankTransactionDataBase = Room.databaseBuilder(context, BankTransactionDataBase::class.java, "transactions_db").build()
}