package latproject.com.myfinance.core.model.modelparser

import latproject.com.myfinance.core.globals.isACharDigit
import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.room.RealmBankTransaction
import timber.log.Timber

class TransactionParser {
    companion object {
        fun parseToTransaction(bank: String, sms: SmsMessage): RealmBankTransaction? {
            val bankTransaction: RealmBankTransaction?

            if (bank.toLowerCase().contains("stanbic")) {
                bankTransaction = parseForStanbicIBTC(sms)
                return bankTransaction
            }

            if (bank.toLowerCase().contains("union")) {
                bankTransaction = parseForUnion(sms)
                return bankTransaction
            }

            if (bank.toLowerCase().contains("uba")) {
                bankTransaction = parseForUBA(sms)
                return bankTransaction
            }

            if (bank.toLowerCase().contains("gtb") || bank.toLowerCase().contains("gtb")) {
                bankTransaction = parseForGtb(sms)
                return bankTransaction
            }

            return null
        }

        private fun parseForGtb(sms: SmsMessage): RealmBankTransaction {
            val message = sms.body
            val bankTransaction = RealmBankTransaction()
            val date = sms.date
            val crDbIndex = if (message.contains("CR")) "CR" else "DB"
            val type = if (message.contains("CR")) "credit" else "debit"
            if (type != "") {
                val amountStart = message.indexOf("Amt:") + 4
                val amountEndIndex = message.indexOf(crDbIndex)

                val amount = message.substring(amountStart, amountEndIndex).replace(",", "")
                val detailsStartIndex = message.indexOf("Desc") + 4
                val detailsEndIndex = message.indexOf("Avail")
                val details = message.substring(detailsStartIndex, detailsEndIndex)

                val balanceStart = message.indexOf("Bal") + 3
                val balanceAfterTrx = message.substring(balanceStart, message.length).replace(",", "")
                bankTransaction.id = "$type$amount$date"
                bankTransaction.date = date
                bankTransaction.amount = amount.toDouble()
                bankTransaction.details = details
                bankTransaction.bank = sms.from
                bankTransaction.balanceAfterTransaction = balanceAfterTrx.toDouble()

            }

            return bankTransaction
        }

        private fun parseForStanbicIBTC(smsMessage: SmsMessage): RealmBankTransaction {
            val message = smsMessage.body

            Timber.d("MESSAGE $message")

            val bankTransaction = RealmBankTransaction()

            val date = smsMessage.date

            val type = if (message.toLowerCase().contains("debit".toLowerCase())) "debit" else "credit"

            if (type != "") {
                try{
                    val amountStartIndex = message.indexOf("of NGN") + 6
                    val amountEndIndex = message.toLowerCase().indexOf("occurred".toLowerCase())

                    val amount = message.substring(amountStartIndex, amountEndIndex).replace(",", "")

                    val detailsStartIndex = message.toLowerCase().indexOf("Details:".toLowerCase()) + 8
                    val detailsEndIndex = message.toLowerCase().indexOf("Balance:".toLowerCase())
                    val messageDetails = message.substring(detailsStartIndex, detailsEndIndex)

                    val indexForBalanceAfterTransaction = message.indexOf("Balance:NGN") + 11

                    val balanceAfterTransaction = message.substring(indexForBalanceAfterTransaction, message.length).replace(",", "")

                    bankTransaction.details = messageDetails
                    bankTransaction.date = date
                    bankTransaction.amount = amount.toDouble()
                    bankTransaction.id = "$type$amount$date"
                    bankTransaction.type = type
                    bankTransaction.bank = smsMessage.from
                    bankTransaction.balanceAfterTransaction = balanceAfterTransaction.toDouble()
                }catch (t: Throwable) {
                    Timber.e("Error %s", t)
                }

            }

            return bankTransaction
        }

        private fun parseForUBA(sms: SmsMessage): RealmBankTransaction {
            val message = sms.body
            val bankTransaction = RealmBankTransaction()

            val type = if (message.toLowerCase().contains("debit".toLowerCase())) "debit" else "credit"
            if (type != "") {
                try{
                    val date = sms.date
                    val startIndexForAmount = message.toLowerCase().indexOf("Amt:NGN".toLowerCase()) + 7
                    val endIndexForAmount = message.toLowerCase().indexOf("Desc:".toLowerCase())

                    val amount = message.substring(startIndexForAmount, endIndexForAmount)

                    val startIndexForDetails = message.toLowerCase().indexOf("Desc".toLowerCase()) + 4

                    val endIndexForDetails = message.toLowerCase().indexOf("Bal".toLowerCase())
                    val details = message.substring(startIndexForDetails, endIndexForDetails)

                    val startIndexForBalanceAfterTrx = message.toLowerCase().indexOf("Bal".toLowerCase()) + 7
                    val endIndexForBalance = if (message.toLowerCase().contains("http"))
                        message.toLowerCase().indexOf("http") else if (message.toLowerCase().contains("Dial".toLowerCase())) message.indexOf("Dial".toLowerCase()) else -1

                    var balanceAfterTrx = ""
                    if (endIndexForBalance > 0 && (endIndexForBalance - startIndexForBalanceAfterTrx) > 0)
                        balanceAfterTrx = message.substring(startIndexForBalanceAfterTrx, endIndexForBalance)

                    bankTransaction.type = type
                    try {
                        bankTransaction.amount = amount.toDouble()
                    } catch (exception: Throwable) {
                        exception.printStackTrace()
                    }

                    bankTransaction.details = details
                    bankTransaction.date = sms.date
                    bankTransaction.bank = sms.from
                    bankTransaction.id = "$type$amount$date"
                    try {
                        bankTransaction.balanceAfterTransaction = balanceAfterTrx.toDouble()
                    } catch (exception: Throwable) {
                        exception.printStackTrace()
                    }
                }catch (error: Throwable) { Timber.e("ERROR %s", error)}
            }

            return bankTransaction
        }

        private fun parseForUnion(sms: SmsMessage): RealmBankTransaction {
            val message = sms.body
            val bankTransaction = RealmBankTransaction()

            val type = if (message.toLowerCase().contains("debit")) "debit" else if (message.toLowerCase().contains("credit")) "credit" else ""

            if (type != "") {
//                val amount = ""


                val startIndexForAmount = message.indexOf("Amt") + 8

                val endIndexForAmount = message.indexOf("Date")

                val amount = message.substring(startIndexForAmount, endIndexForAmount)

                val startIndexForDetails = message.toLowerCase().indexOf("Desc".toLowerCase()) + 6
                val endIndexForDetails = message.toLowerCase().indexOf("Balance".toLowerCase())

                var details = ""
                if ((endIndexForDetails - startIndexForDetails) > 0)
                    details = message.substring(startIndexForDetails, endIndexForDetails)

                val startIndexForBalanceAfterTrx = message.indexOf("Balance: ") + 9
                val balanceAfterTrx = message.substring(startIndexForBalanceAfterTrx, startIndexForBalanceAfterTrx + 3).filter { it.toString() != "it" }

                bankTransaction.bank = sms.from
                bankTransaction.date = sms.date
                System.out.println("_BALNCE: ${balanceAfterTrx} _AMOUNT: ${amount}")
                val transactionAfter = balanceAfterTrx.replace("it", "")

                if (transactionAfter != "") {
                    bankTransaction.balanceAfterTransaction = transactionAfter.toDouble()
                } else {
                    bankTransaction.balanceAfterTransaction = 0.0
                }

                bankTransaction.details = details
                bankTransaction.type = type
                bankTransaction.amount = 0.0
//                bankTransaction.amount = amount.toDouble()
            }

            return bankTransaction
        }
    }
}