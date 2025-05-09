package me.blueb.model
import kotlinx.serialization.Serializable

@Serializable
class ApType {
    enum class Activity {
        Accept,
        Add,
        Bite,
        Create,
        Delete,
        Reject,
        Block,
        Follow,
    }

    enum class Object {
        Person,
        Service,
        Note,
        Image,
        Key,
    }
}
