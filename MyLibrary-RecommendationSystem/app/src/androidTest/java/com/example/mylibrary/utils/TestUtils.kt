package com.example.mylibrary.utils

import com.example.mylibrary.dataclass.AccountData
import com.example.mylibrary.dataclass.BooksData
import com.example.mylibrary.models.Book


class TestUtils {

    fun readAccountDataFromFile(filename: String): List<AccountData> {
        val accountDataList = mutableListOf<AccountData>()

        val inputStream = javaClass.classLoader?.getResourceAsStream("assets/$filename")
        val lines = inputStream?.bufferedReader()?.readLines()

        if (lines != null) {
            for (line in lines) {
                val values = line.split(",")
                if (values.size == 4) {
                    val accountData = AccountData(values[0], values[1], values[2], values[3])
                    accountDataList.add(accountData)
                }
            }
        }

        return accountDataList
    }

    fun readBooksDataFromFile(filename: String): List<BooksData> {
        val booksDataList = mutableListOf<BooksData>()

        val inputStream = javaClass.classLoader?.getResourceAsStream("assets/$filename")
        val lines = inputStream?.bufferedReader()?.readLines()

        if (lines != null) {
            for (line in lines) {
                val values = line.split(",")
                if (values.size == 2) {
                    val booksData = BooksData(values[0], values[1])
                    booksDataList.add(booksData)
                }
            }
        }

        return booksDataList
    }

}