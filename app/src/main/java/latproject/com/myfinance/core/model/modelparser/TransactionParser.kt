package latproject.com.myfinance.core.model.modelparser

import latproject.com.myfinance.core.model.SmsMessage
import latproject.com.myfinance.core.room.BankTransaction

class TransactionParser {
    companion object {
        fun parseToTransaction(bank: String, sms: SmsMessage): BankTransaction? {
            var bankTransaction: BankTransaction?

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

            return null
        }

        private fun parseForStanbicIBTC(smsMessage: SmsMessage): BankTransaction {
            val message = smsMessage.body

            val bankTransaction = BankTransaction()

            val date = smsMessage.date

            val type = if (message.toLowerCase().contains("debit".toLowerCase())) "debit" else "credit"

            if (type != "") {
                val amountStartIndex = message.indexOf("of NGN") + 6
                val amountEndIndex = message.toLowerCase().indexOf("occurred".toLowerCase())

                val amount = message.substring(amountStartIndex, amountEndIndex)

                val detailsStartIndex = message.toLowerCase().indexOf("Details:".toLowerCase()) + 8
                val detailsEndIndex = message.toLowerCase().indexOf("Balance:".toLowerCase())
                val messageDetails = message.substring(detailsStartIndex, detailsEndIndex)

                val indexForBalanceAfterTransaction = message.indexOf("Balance:NGN") + 11

                val balanceAfterTransaction = message.substring(indexForBalanceAfterTransaction, message.length)

                bankTransaction.details = messageDetails
                bankTransaction.date = date
                bankTransaction.amount = amount.toDouble()

                bankTransaction.type = type
                bankTransaction.bank = smsMessage.from
                bankTransaction.balanceAfterTransaction = balanceAfterTransaction.toDouble()
            }

            return bankTransaction
        }

        private fun parseForUBA(sms: SmsMessage): BankTransaction {
            val message = sms.body

            val type = if (message.toLowerCase().contains("debit".toLowerCase())) "debit" else "credit"

            val startIndexForAmount = message.toLowerCase().indexOf("Amt:NGN".toLowerCase()) + 7
            val endIndexForAmount = message.toLowerCase().indexOf("Desc".toLowerCase())

            val amount = message.substring(startIndexForAmount, endIndexForAmount)

            val startIndexForDetails = message.toLowerCase().indexOf("Desc".toLowerCase()) + 4

            val endIndexForDetails = message.toLowerCase().indexOf("Bal".toLowerCase())
            val details = message.substring(startIndexForDetails, endIndexForDetails)

            val startIndexForBalanceAfterTrx = message.toLowerCase().indexOf("Bal:NGN".toLowerCase()) + 7
            val endIndexForBalanceAfterTrx = message.toLowerCase().indexOf("http://".toLowerCase())

            val balanceAfterTrx = message.substring(startIndexForBalanceAfterTrx, endIndexForBalanceAfterTrx)

            val bankTransaction = BankTransaction()
            bankTransaction.type = type
            bankTransaction.amount = amount.toDouble()
            bankTransaction.details = details
            bankTransaction.date = sms.date
            bankTransaction.balanceAfterTransaction = balanceAfterTrx.toDouble()

            return bankTransaction
        }

        private fun parseForUnion(sms: SmsMessage): BankTransaction {
            val message = sms.body
            val bankTransaction = BankTransaction()

            val type = if (message.contains("debit")) "debit" else if (message.contains("credit")) "credit" else ""

            if (type != "") {
                val startIndexForAmount = message.indexOf("Amt: NGN") + 8

                val endIndexForAmount = message.indexOf("Date")

                val amount = message.substring(startIndexForAmount, endIndexForAmount)

                val startIndexForDetails = message.indexOf("Desc: ") + 6
                val endIndexForDetails = message.indexOf("Balance: ")

                val details = message.substring(startIndexForDetails, endIndexForDetails)

                val startIndexForBalanceAfterTrx = message.indexOf("Balance: ") + 9
                val balanceAfterTrx = message.substring(startIndexForBalanceAfterTrx)

                bankTransaction.bank = sms.from
                bankTransaction.date = sms.date
                bankTransaction.balanceAfterTransaction = balanceAfterTrx.toDouble()
                bankTransaction.details = details
                bankTransaction.type = type
                bankTransaction.amount = amount.toDouble()
            }

            return bankTransaction
        }
    }
}