package com.mediaplayer.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediaplayer.app.ViewModelFactory
import com.mediaplayer.app.activities.home.HomeActivityViewModel
import com.mediaplayer.app.databinding.FragmentHomeBinding
import com.mediaplayer.app.di.components.FragmentComponent
import com.mediaplayer.repository.Song
import com.mediaplayer.ui.BaseFragment
import javax.inject.Inject


class HomeFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeViewModel
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)

    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.homeRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        viewModel.songsLiveData.observe(viewLifecycleOwner, Observer<List<Song>> {
            viewBinding.homeRecyclerView.adapter = HomeRecyclerAdapter(it) { song ->
                viewModel.play(song)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}