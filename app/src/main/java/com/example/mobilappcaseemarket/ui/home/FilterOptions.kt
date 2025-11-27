package com.example.mobilappcaseemarket.ui.home

data class FilterOptions(
    val searchQuery: String = "",
    val sortType: SortType = SortType.NONE
)
enum class SortType {
    NONE,
    PRICE_ASC,
    PRICE_DESC,
    NAME_ASC,
    NAME_DESC
}
