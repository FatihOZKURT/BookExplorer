package com.example.bookexplorer.model


data class BookDetails (
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo,
    val layerInfo: LayerInfo,
    val saleInfo: SaleInfo,
    val accessInfo: AccessInfoDetails
)

data class AccessInfoDetails (
    val country: String,
    val viewability: String,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: String,
    val epub: Epub,
    val pdf: PDF,
    val webReaderLink: String,
    val accessViewStatus: String,
    val quoteSharingAllowed: Boolean
)


data class EpubDetails (
    val isAvailable: Boolean,
    val acsTokenLink: String
)

data class PDF (
    val isAvailable: Boolean
)

data class LayerInfo (
    val layers: List<Layer>
)

data class Layer (
    val layerID: String,
    val volumeAnnotationsVersion: String
)

data class SaleInfoDetails (
    val country: String,
    val saleability: String,
    val isEbook: Boolean,
    val listPrice: SaleInfoListPrice,
    val retailPrice: SaleInfoListPrice,
    val buyLink: String,
    val offers: List<Offer>
)

data class SaleInfoListPrice (
    val amount: Double,
    val currencyCode: String
)

data class Offer (
    val finskyOfferType: Long,
    val listPrice: OfferListPrice,
    val retailPrice: OfferListPrice
)

data class OfferListPrice (
    val amountInMicros: Long,
    val currencyCode: String
)

data class VolumeInfoDetails (
    val title: String,
    val subtitle: String,
    val authors: List<String>,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val industryIdentifiers: List<IndustryIdentifier>,
    val readingModes: ReadingModes,
    val pageCount: Long,
    val printedPageCount: Long,
    val dimensions: Dimensions,
    val printType: String,
    val categories: List<String>,
    val averageRating: Long,
    val ratingsCount: Long,
    val maturityRating: String,
    val allowAnonLogging: Boolean,
    val contentVersion: String,
    val panelizationSummary: PanelizationSummary,
    val imageLinks: ImageLinks,
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String
)

data class Dimensions (
    val height: String
)

data class ImageLinksDetails (
    val smallThumbnail: String,
    val thumbnail: String,
    val small: String,
    val medium: String,
    val large: String
)

data class IndustryIdentifierDetails (
    val type: String,
    val identifier: String
)

data class PanelizationSummaryDetails (
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
)

data class ReadingModesDetails (
    val text: Boolean,
    val image: Boolean
)


