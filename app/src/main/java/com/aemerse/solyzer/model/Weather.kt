package com.aemerse.solyzer.model

class Weather {
    @JvmField
    var location: Location? = null
    @JvmField
    var currentCondition: Condition = Condition()
    @JvmField
    var temperature = Temperature()
    @JvmField
    var wind = Wind()
    var rain = Rain()
    var snow = Snow()
    @JvmField
    var clouds = Clouds()
    @JvmField
    var iconData: ByteArray? = null

    inner class Condition {
        var weatherId = 0
        var condition: String? = null
        var descr: String? = null
        var icon: String? = null
        var pressure = 0f
        var humidity = 0f
    }

    inner class Temperature {
        var temp = 0f
        var minTemp = 0f
        var maxTemp = 0f
    }

    inner class Wind {
        var speed = 0f
        var deg = 0f
    }

    inner class Rain {
        var time: String? = null
        var ammount = 0f
    }

    inner class Snow {
        var time: String? = null
        var ammount = 0f
    }

    inner class Clouds {
        var perc = 0
    }
}