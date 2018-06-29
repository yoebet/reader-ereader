package wjy.yo.ereader.entity

import java.util.Comparator

interface Ordered {

    var no: Long

    companion object {

        val Comparator: Comparator<Ordered> =  Comparator { o1, o2 -> (o1.no-o2.no).toInt() }
    }
}
