package me.blueb.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LdContextDefinitions(
    @SerialName("Key")
    val key: String = "sec:Key",

    val sensitive: String = "as:sensitive",
    val manuallyApprovesFollowers: String = "as:manuallyApprovesFollowers",
    val quoteUrl: String = "as:quoteUrl",
    @SerialName("Hashtag")
    val hashtag: String = "as:Hashtag",

    val vcard: String = "http://www.w3.org/2006/vcard/ns#",

    val schema: String = "http://schema.org#",
    @SerialName("PropertyValue")
    val propertyValue: String = "schema:PropertyValue",
    val value: String = "schema:value",

    val toot: String = "http://joinmastodon.org/ns#",
    @SerialName("Emoji")
    val emoji: String = "toot:Emoji",
    val discoverable: String = "toot:discoverable",

    val fedibird: String = "http://fedibird.com/ns#",
    val quoteUri: String = "as:quoteUri",

    val misskey: String = "https://misskey-hub.net/ns#",
    val _misskey_content: String = "misskey:_misskey_content",
    val _misskey_quote: String = "misskey:_misskey_quote",
    val _misskey_reaction: String = "misskey:_misskey_reaction",
    val _misskey_summary: String = "misskey:_misskey_summary",
    val isCat: String = "misskey:isCat",

    val firefish: String = "https://joinfirefish.org/ns#",
    val speakAsCat: String = "firefish:speakAsCat",

    val mia: String = "https://ns.mia.jetzt/as#",
    @SerialName("Bite")
    val bite: String = "mia:Bite",

    val aster: String = "https://blueb.pages.gay/ns#",
    val visibility: String = "aster:visibility"
)
