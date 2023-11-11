package com.arqus.arqueotimes3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arqus.arqueotimes3.model.Article
import com.arqus.arqueotimes3.R
import com.bumptech.glide.Glide

class ArticlesAdapter(
    private val articles: MutableList<Article?>,
    private val onArticleClicked: (Article) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    fun setData(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return if (articles[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
            ArticleViewHolder(view, onArticleClicked)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ArticleViewHolder) {
            articles[position]?.let { holder.bind(it) }
        }
        // No need to bind anything for LoadingViewHolder
    }

    override fun getItemCount(): Int = articles.size

    fun addItems(newItems: List<Article>) {
        // Remove loading item
        if (articles.isNotEmpty() && articles.last() == null) {
            articles.removeAt(articles.size - 1)
            notifyItemRemoved(articles.size)
        }

        val startInsertPosition = articles.size
        articles.addAll(newItems)
        notifyItemRangeInserted(startInsertPosition, newItems.size)
    }

    fun addLoadingView() {
        // Add loading item
        articles.add(null)
        notifyItemInserted(articles.size - 1)
    }
    fun removeLoadingView() {
        // Si el último elemento es null (spinner de carga), eliminarlo
        if (articles.isNotEmpty() && articles.last() == null) {
            articles.removeAt(articles.size - 1)
            notifyItemRemoved(articles.size)
        }
    }

    class ArticleViewHolder(
        private val view: View,
        private val onArticleClicked: (Article) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.article_image)
        private val titleView: TextView = view.findViewById(R.id.article_title)
        private val authorView: TextView = view.findViewById(R.id.article_author)

        fun bind(article: Article) {
            titleView.text = article.title.rendered
            authorView.text = article.authorName
            imageView.contentDescription = article.description

            Glide.with(view.context)
                .load(article.featured_img)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageView)

            view.setOnClickListener {
                onArticleClicked(article)
            }
        }
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Aquí puedes inicializar elementos de la vista de carga, como un ProgressBar
        // Por ejemplo:
        private val progressBar: ProgressBar = view.findViewById(R.id.progress_circular)
    }
}
