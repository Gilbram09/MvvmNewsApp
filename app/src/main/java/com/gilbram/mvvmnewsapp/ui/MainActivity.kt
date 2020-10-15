package com.gilbram.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.gilbram.mvvmnewsapp.R
import com.gilbram.mvvmnewsapp.database.ArticleDatabase
import com.gilbram.mvvmnewsapp.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewsViewModelProviderFactory(application,newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NewsViewModel::class.java)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
