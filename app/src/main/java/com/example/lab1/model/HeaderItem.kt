package com.example.lab1.model

data class HeaderItem(
    val title: String,
    override val id: Int
) : MyListable