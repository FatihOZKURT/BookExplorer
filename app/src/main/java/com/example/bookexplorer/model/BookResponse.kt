package com.example.bookexplorer.model


data class BookResponse (
    val kind: String,
    val totalItems: Long,
    val items: List<BookItem>
)

data class BookItem (
    val kind: Kind,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfo,
    val searchInfo: SearchInfo? = null
)

data class AccessInfo (
    val country: Country,
    val viewability: Viewability,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: TextToSpeechPermission,
    val epub: Epub,
    val pdf: Epub,
    val webReaderLink: String,
    val accessViewStatus: AccessViewStatus,
    val quoteSharingAllowed: Boolean
)

enum class AccessViewStatus {
    FullPublicDomain,
    None
}

enum class Country {
    Tr
}

data class Epub (
    val isAvailable: Boolean,
    val downloadLink: String? = null
)

enum class TextToSpeechPermission {
    Allowed
}

enum class Viewability {
    AllPages,
    NoPages
}

enum class Kind {
    BooksVolume
}

data class SaleInfo (
    val country: Country,
    val saleability: Saleability,
    val isEbook: Boolean,
    val buyLink: String? = null
)

enum class Saleability {
    Free,
    NotForSale
}

data class SearchInfo (
    val textSnippet: String
)

data class VolumeInfo (
    val title: String,
    val authors: List<String>,
    val publishedDate: String? = null,
    val industryIdentifiers: List<IndustryIdentifier>,
    val readingModes: ReadingModes,
    val pageCount: Long,
    val printType: PrintType,
    val maturityRating: MaturityRating,
    val allowAnonLogging: Boolean,
    val contentVersion: String,
    val panelizationSummary: PanelizationSummary? = null,
    val language: Language,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String,
    val subtitle: String? = null,
    val categories: List<String>? = null,
    val imageLinks: ImageLinks? = null,
    val description: String? = null,
    val publisher: String? = null
)

data class ImageLinks (
    val smallThumbnail: String,
    var thumbnail: String
)

data class IndustryIdentifier (
    val type: Type,
    val identifier: String
)

enum class Type {
    Isbn10,
    Isbn13,
    Other
}

enum class Language {
    En
}

enum class MaturityRating {
    NotMature
}

data class PanelizationSummary (
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
)

enum class PrintType {
    Book
}

data class ReadingModes (
    val text: Boolean,
    val image: Boolean
)

