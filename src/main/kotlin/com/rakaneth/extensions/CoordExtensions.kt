package com.rakaneth.extensions

import squidpony.squidmath.Coord

operator fun Coord.component1() = this.x
operator fun Coord.component2() = this.y