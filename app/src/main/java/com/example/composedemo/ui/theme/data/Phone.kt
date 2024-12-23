package com.example.composedemo.ui.theme.data

class Phone(
    var name: String
){
    var company: String = name.substringBefore(" ")
    var model: String = name.substringAfter(" ")
}