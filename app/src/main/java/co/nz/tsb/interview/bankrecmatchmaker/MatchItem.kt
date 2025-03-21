package co.nz.tsb.interview.bankrecmatchmaker

data class MatchItem(
    val paidTo: String,
    val transactionDate: String,
    val total: Float,
    val docType: String
)