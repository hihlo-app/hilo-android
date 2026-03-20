package com.app.hihlo.utils

import android.text.InputFilter
import android.text.Spanned

class UsernameInputFilter : InputFilter {

    private val allowed = Regex("[a-z0-9._]")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {

        val result = StringBuilder()

        for (i in start until end) {
            val c = source[i].lowercaseChar()

            // block @ always
            if (c == '@') continue

            if (allowed.matches(c.toString())) {
                result.append(c)
            }
        }
        return result.toString()
    }
}



