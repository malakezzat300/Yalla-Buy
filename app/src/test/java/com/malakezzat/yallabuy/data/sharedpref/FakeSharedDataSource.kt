package com.malakezzat.yallabuy.data.sharedpref

class FakeSharedDataSource:GlobalSharedPreferenceDataSource {
    var id : Long = 10L
    override fun getUserId(): Long {
        return id
    }

    override fun setUserId(id: Long) {
        this.id = id
    }

    override fun getUserEmail(): String {
        TODO("Not yet implemented")
    }

    override fun setUserEmail(string: String) {
        TODO("Not yet implemented")
    }
}