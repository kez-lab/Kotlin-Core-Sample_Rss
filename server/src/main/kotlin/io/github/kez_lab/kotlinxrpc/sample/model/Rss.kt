package io.github.kez_lab.kotlinxrpc.sample.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

/**
 * rss 루트. (루트 태그: <rss>)
 */
@Serializable
@XmlSerialName("rss", "", "") // prefix와 namespace 없을 경우 명시적으로 ""로 설정
data class Rss(
    @XmlElement(false)
    val version: String? = null,
    @XmlElement(true)
    val channel: Channel
)

/**
 * channel 태그
 */
@XmlSerialName("channel", "", "")
@Serializable
data class Channel(
    @XmlElement(true)
    val title: String? = null,
    @XmlElement(true)
    val link: String? = null,
    @XmlElement(true)
    val description: String? = null,
    @XmlElement(true)
    val lastBuildDate: String? = null,
    @XmlElement(true)
    val language: String? = null,
    @XmlElement(true)
    val generator: String? = null,
    // 나머지 필요한 필드를 계속 추가하실 수 있습니다.

    // <item>이 여러 개이므로 List로 매핑
    @XmlElement(true)
    val item: List<NewsItem> = emptyList(),
    @XmlElement(true)
    @SerialName("atom:link")
    val atomLink: AtomLink? = null,

    @XmlElement(true)
    @XmlSerialName("updatePeriod", "http://purl.org/rss/1.0/modules/syndication/", "")
    val updatePeriod: String? = null,

    @XmlElement(true)
    @XmlSerialName("updateFrequency", "http://purl.org/rss/1.0/modules/syndication/", "")
    val updateFrequency: String? = null,
)

@XmlSerialName("link", "http://www.w3.org/2005/Atom", "")
@Serializable
data class AtomLink(
    @XmlElement(false)
    @SerialName("href")
    val href: String? = null,
    @XmlElement(false)
    @SerialName("rel")
    val rel: String? = null,
    @XmlElement(false)
    @SerialName("type")
    val type: String? = null
)

/**
 * item 태그
 */
@XmlSerialName("item", "", "")
@Serializable
data class NewsItem(
    @XmlElement(true)
    val title: String? = null,
    @XmlElement(true)
    val link: String? = null,
    @XmlElement(true)
    val comments: String? = null,

    @XmlElement(true)
    @XmlSerialName("comments", "http://purl.org/rss/1.0/modules/slash/", "")
    val slashComments: String? = null,

    @XmlElement(true)
    val pubDate: String? = null,

    // ✅ 네임스페이스 지정
    @XmlElement(true)
    @XmlSerialName("creator", "http://purl.org/dc/elements/1.1/", "")
    val creator: String? = null,

    @XmlElement(true)
    val category: String? = null,
    @XmlElement(true)
    val guid: Guid? = null,
    @XmlElement(true)
    val enclosure: Enclosure? = null,
    @XmlElement(true)
    val description: String? = null,

    // ✅ 네임스페이스가 있는 `content:encoded`도 같은 방식으로 수정
    @XmlElement(true)
    @XmlSerialName("encoded", "http://purl.org/rss/1.0/modules/content/", "")
    val contentEncoded: String? = null
)

/**
 * <guid> 태그
 */
@XmlSerialName("guid", "", "")
@Serializable
data class Guid(
    @XmlElement(false)
    @SerialName("isPermaLink")
    val isPermaLink: String? = null,
    // #PCDATA(텍스트) 파트
    @XmlValue
    val value: String? = null
)

/**
 * <enclosure> 태그
 */
@XmlSerialName("enclosure", "", "")
@Serializable
data class Enclosure(
    @XmlElement(false)
    @SerialName("url")
    val url: String? = null,
    @XmlElement(false)
    @SerialName("type")
    val type: String? = null
)

fun Rss.toNewsList() = channel.item.map { item ->
    News(
        title = item.title ?: "제목 없음",
        link = item.link.orEmpty(),
        pubDate = item.pubDate.orEmpty(),
        creator = item.creator ?: "?",
        imageUrl = item.enclosure?.url,
        description = decodeHtmlEntities(item.description ?: item.contentEncoded).orEmpty()
    )
}

fun decodeHtmlEntities(text: String?): String? {
    return text
        ?.replace("&lt;", "<")
        ?.replace("&gt;", ">")
        ?.replace("&amp;", "&")
        ?.replace("&quot;", "\"")
        ?.replace("&#39;", "'")
}