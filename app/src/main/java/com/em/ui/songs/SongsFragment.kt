package com.em.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.em.app.ViewModelFactory
import com.em.app.activities.home.HomeActivityViewModel
import com.em.app.databinding.FragmentSongsBinding
import com.em.app.di.components.FragmentComponent
import com.em.repository.Song
import com.em.ui.BaseFragment
import javax.inject.Inject


class SongsFragment : BaseFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SongsViewModel
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewBinding: FragmentSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SongsViewModel::class.java)
        homeActivityViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)

    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.homeRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        viewModel.songsLiveData.observe(viewLifecycleOwner, Observer<List<Song>> {
            viewBinding.homeRecyclerView.adapter = SongsRecyclerAdapter(it) { song ->
                viewModel.play(song)
            }
        })
    }

    companion object {
        fun newInstance() = SongsFragment()
    }
}