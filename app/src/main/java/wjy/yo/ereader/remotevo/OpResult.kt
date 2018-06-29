package wjy.yo.ereader.remotevo

open class OpResult {

    var ok: Int = 0

    var message: String? = null

    val isOk: Boolean
        get() = this.ok == 1
}
