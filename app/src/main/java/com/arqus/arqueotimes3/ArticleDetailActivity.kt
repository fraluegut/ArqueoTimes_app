package com.arqus.arqueotimes3

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arqus.arqueotimes3.model.Article
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class ArticleDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail) // Asegúrate de que este es el nombre correcto de tu layout

        // Encuentra las vistas por ID
        val articleTitle: TextView = findViewById(R.id.articleTitle)
        val articleContent: WebView = findViewById(R.id.articleWebView)
        val articleAuthor: TextView = findViewById(R.id.articleAuthor)


        // Obtiene el artículo pasado a través del Intent
        val article = intent.getParcelableExtra<Article>("ARTICLE")

        if (article != null) {
            articleTitle.text = article.title.rendered
            articleAuthor.text = article.authorName

            // Utiliza JSoup para analizar el contenido HTML
            val doc: Document = Jsoup.parse(article.content.rendered)

            // Aplicar estilo CSS para justificar el texto
            val body: Element = doc.body()
            body.attr("style", "text-align: justify;")

            // Extraer las etiquetas de imagen <img>
            val imageElements: Elements = doc.select("img")

            // Modificar las etiquetas de imagen para que se ajusten a la pantalla
            for (element: Element in imageElements) {
                element.attr("style", "max-width: 100%; height: auto;")
            }

            // Cargar el contenido HTML modificado en el WebView
            val htmlContent = doc.outerHtml()
            articleContent.settings.javaScriptEnabled = true
            articleContent.webViewClient = WebViewClient()
            articleContent.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

        } else {
            // Manejar el caso en que 'article' sea nulo
            finish()
        }
    }
}