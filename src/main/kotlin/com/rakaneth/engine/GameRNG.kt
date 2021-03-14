package com.rakaneth.engine

import squidpony.squidmath.GWTRNG
import squidpony.squidmath.IRNG

object GameRNG {
    val mapRNG: IRNG = GWTRNG(0xDEADBEEF)
    val gameRNG: IRNG = GWTRNG(0xDEADBEEF)
}