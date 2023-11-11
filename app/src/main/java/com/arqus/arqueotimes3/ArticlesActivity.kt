package com.arqus.arqueotimes3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arqus.arqueotimes3.adapter.ArticlesAdapter
import com.arqus.arqueotimes3.model.Article
import com.arqus.arqueotimes3.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ArticlesActivity : AppCompatActivity() {

    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var articlesAdapter: ArticlesAdapter
    private var currentPage = 1
    private val pageSize = 10 // Ajusta el tamaño de página según tus necesidades
    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articles)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish() // Terminar esta actividad y volver a la anterior
        }

        // Establecer el título del Toolbar aquí
        supportActionBar?.title = "Artículos"

        articlesRecyclerView = findViewById(R.id.articles_recycler_view)
        articlesAdapter = ArticlesAdapter(mutableListOf()) { article ->
            val intent = Intent(this, ArticleDetailActivity::class.java)
            intent.putExtra("ARTICLE", article)
            startActivity(intent)
        }
        articlesRecyclerView.adapter = articlesAdapter
        val layoutManager = LinearLayoutManager(this)
        articlesRecyclerView.layoutManager = layoutManager

        // Agregar un Listener de desplazamiento para manejar la paginación
        articlesRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= pageSize
                    ) {
                        loadMoreArticles()
                    }
                }
            }
        })

        loadArticles()
    }

    private fun loadArticles() {
        // Resto del código para cargar la primera página de artículos
        // ...

        currentPage = 1
        isLoading = true
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.arqueotimes.es/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        apiService.getArticles(currentPage, pageSize).enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                isLoading = false
                if (response.isSuccessful) {
                    println("Cargados: ${response.body()}")
                    val articlesList = response.body() ?: emptyList()
                    articlesAdapter.setData(articlesList)

                    // Incrementa el número de página para la próxima carga
                    currentPage++
                } else {
                    println("Error de respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                isLoading = false
                println("Fallo en la llamada a la API: ${t.message}")
            }
        })
    }

    private fun loadMoreArticles() {
        if (isLoading || isLastPage) {
            return
        }

        // Resto del código para cargar más páginas de artículos
        // ...

        isLoading = true
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.arqueotimes.es/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getArticles(currentPage, pageSize).enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>, response: Response<List<Article>>) {
                isLoading = false
                if (response.isSuccessful) {
                    println("Cargados más artículos: ${response.body()}")
                    val articlesList = response.body() ?: emptyList()
                    articlesAdapter.addItems(articlesList)

                    // Incrementa el número de página para la próxima carga
                    currentPage++

                    // Verifica si es la última página
                    if (articlesList.isEmpty() || articlesList.size < pageSize) {
                        isLastPage = true
                    }
                } else {
                    println("Error de respuesta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Article>>, t: Throwable) {
                isLoading = false
                println("Fallo en la llamada a la API: ${t.message}")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
