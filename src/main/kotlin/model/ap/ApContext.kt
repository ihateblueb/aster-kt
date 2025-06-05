package site.remlit.blueb.aster.model.ap

import kotlinx.serialization.json.*

val ApContext: JsonArray = buildJsonArray {
	add("https://www.w3.org/ns/activitystreams")
	add("https://w3id.org/security/v1")
	add(buildJsonObject {
		put("Key", "sec:Key")

		put("sensitive", "as:sensitive")
		put("manuallyApprovesFollowers", "as:manuallyApprovesFollowers")
		put("quoteUrl", "as:quoteUrl")
		put("Hashtag", "as:Hashtag")
		put("vcard", "http://www.w3.org/2006/vcard/ns#")

		put("schema", "http://schema.org#")
		put("PropertyValue", "schema:PropertyValue")
		put("value", "schema:value")

		put("toot", "http://joinmastodon.org/ns#")
		put("Emoji", "toot:Emoji")
		put("discoverable", "toot:discoverable")

		put("fedibird", "http://fedibird.com/ns#")
		put("quoteUri", "fedibird:quoteUri")

		put("misskey", "https://misskey-hub.net/ns#")
		put("_misskey_content", "misskey:_misskey_content")
		put("_misskey_quote", "misskey:_misskey_quote")
		put("_misskey_reaction", "misskey:_misskey_reaction")
		put("_misskey_summary", "misskey:_misskey_summary")
		put("isCat", "misskey:isCat")

		put("firefish", "https://joinfirefish.org/ns#")
		put("speakAsCat", "firefish:speakAsCat")

		put("mia", "https://ns.mia.jetzt/as#")
		put("Bite", "mia:Bite")

		put("aster", "https://blueb.pages.gay/ns#")
		put("visibility", "aster:visibility")
	})
}
