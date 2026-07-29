package dev.paraspatil.recompositionguard.logger

interface LogStrategy {
    fun d(tag: String, message: String)
    fun w(tag: String, message: String)
    fun e(tag: String, message: String)
}

class AndroidLogStrategy : LogStrategy{
    override fun d(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }
    override fun w(tag: String, message: String) {
        android.util.Log.w(tag, message)
    }
    override fun e(tag: String, message: String) {
        android.util.Log.e(tag, message)
    }
}
class SilentLogStrategy : LogStrategy{
    override fun d(tag: String, message: String) {}
    override fun w(tag: String, message: String) {}
    override fun e(tag: String, message: String) {}
}