package com.gilbram.mvvmnewsapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.gilbram.mvvmnewsapp.R
import com.gilbram.mvvmnewsapp.adapter.NewsAdapter
import com.gilbram.mvvmnewsapp.ui.MainActivity
import com.gilbram.mvvmnewsapp.ui.NewsViewModel
import com.gilbram.mvvmnewsapp.util.Constans.Companion.QUERY_PAGE_SIZE
import com.gilbram.mvvmnewsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }

                    }
                }
                is Resource.Error-> {
                    hideProgressBar()
                    response.message.let { message ->
                        Snackbar.make(view," an error $message",Snackbar.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBegining = firstVisibleItemPosition >= 0
            val isTotalMoreThenVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBegining&&
                    isTotalMoreThenVisible && isScrolling

            if (shouldPaginate){
                viewModel.getBreakinNews("id")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

    }
    private fun showProgressBar(){
        paginationProgressBar.visibility= View.VISIBLE
        isLoading = true
    }
    private fun hideProgressBar(){
        paginationProgressBar.visibility= View.INVISIBLE
        isLoading = false
    }

    private fun setupRecyclerView() {
        newsAdapter= NewsAdapter()
        rvBreakingNews.apply {
            adapter= newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}