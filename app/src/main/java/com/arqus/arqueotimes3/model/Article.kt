package com.arqus.arqueotimes3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: Int,
    val title: Rendered,
    val excerpt: Rendered,
    val content: Rendered,
    val authors: List<Author>,
    val featuredMedia: Int,
    val featured_img: String,
    val description: String? // Puede ser nulo
) : Parcelable {
    @Parcelize
    data class Rendered(val rendered: String) : Parcelable

    @Parcelize
    data class Author(
        val display_name: String
    ) : Parcelable

    // Propiedad calculada para obtener el nombre del primer autor
    val authorName: String
        get() = if (authors.isNotEmpty()) authors[0].display_name else "Autor Desconocido"
}
