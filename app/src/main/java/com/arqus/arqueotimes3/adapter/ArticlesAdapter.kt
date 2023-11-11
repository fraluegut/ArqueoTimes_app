package com.arqus.arqueotimes3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arqus.arqueotimes3.model.Article
import com.arqus.arqueotimes3.R
import com.bumptech.glide.Glide

class ArticlesAdapter(
    private val articles: MutableList<Article>,
    private val onArticleClicked: (Article) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view, onArticleClicked)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
        holder.itemView.setOnClickListener { onArticleClicked(article) }
    }

    override fun getItemCount(): Int = articles.size
    fun addItems(newItems: List<Article>) {
        articles.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateData(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
    }
    fun setData(newArticles: List<Article>) {
        articles.clear()
        articles.addAll(newArticles)
        notifyDataSetChanged()
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
}
