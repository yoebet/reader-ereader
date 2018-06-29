package wjy.yo.ereader.remotevo

class UserInfo(var name: String) : OpResult() {

    var isLogin: Boolean = false

    var nickName: String? = null

    var accessToken: String? = null
}
