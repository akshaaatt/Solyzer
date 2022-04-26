package com.aemerse.solyzer

import java.util.*

object Utility {
    const val GSTINFORMAT_REGEX = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}"
    const val GSTN_CODEPOINT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    @JvmStatic
    fun main(args: Array<String>) {
        val sc = Scanner(System.`in`)
        println("Enter GSTIN Number")
        try {
            if (validGSTIN(sc.next())) {
                println("Valid GSTIN!")
            } else {
                println("Invalid GSTIN")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    @Throws(Exception::class)
    fun validGSTIN(gstin: String): Boolean {
        return if (checkPattern(gstin,
                GSTINFORMAT_REGEX)
        ) {
            verifyCheckDigit(gstin)
        } else false
    }

    @Throws(Exception::class)
    private fun verifyCheckDigit(gstinWCheckDigit: String): Boolean {
        var isCDValid = false
        if (gstinWCheckDigit.trim { it <= ' ' } == getGSTINWithCheckDigit(gstinWCheckDigit.substring(
                0,
                gstinWCheckDigit.length - 1))) {
            isCDValid = true
        }
        return isCDValid
    }

    fun checkPattern(inputval: String, regxpatrn: String?): Boolean {
        return inputval.trim().matches(regxpatrn!!.toRegex())
    }

    @Throws(Exception::class)
    fun getGSTINWithCheckDigit(gstinWOCheckDigit: String?): String? {
        var factor = 2
        var sum = 0
        if (gstinWOCheckDigit == null) {
            try {
                throw Exception("GSTIN supplied for checkdigit calculation is null")
            } catch (th: Throwable) {
            }
        } else {
            val cpChars = GSTN_CODEPOINT_CHARS.toCharArray()
            val inputChars = gstinWOCheckDigit.trim { it <= ' ' }.uppercase(Locale.getDefault()).toCharArray()
            val mod = cpChars.size
            for (i in inputChars.indices.reversed()) {
                var codePoint = -1
                for (j in cpChars.indices) {
                    if (cpChars[j] == inputChars[i]) {
                        codePoint = j
                    }
                }
                val digit = factor * codePoint
                factor = if (factor == 2) {
                    1
                } else {
                    2
                }
                sum += digit / mod + digit % mod
            }
            return gstinWOCheckDigit + cpChars[(mod - sum % mod) % mod]
        }
        return null
    }
}