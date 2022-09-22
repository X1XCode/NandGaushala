package com.gaushala.model.dashboard

class DashboardBean(imageMenu: Int, imageUrl: String, textMenu: String) {
    var imageMenu = 0
    var imageUrl = ""
    var textMenu = ""


    init {
        this.imageMenu = imageMenu
        this.imageUrl = imageUrl
        this.textMenu = textMenu
    }
}