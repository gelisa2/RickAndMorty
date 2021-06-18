package com.example.rickandmorty.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharactersAdapter
import com.example.rickandmorty.api.ApiUtils
import com.example.rickandmorty.databinding.FragmentDetailsEpisodeBinding
import com.example.rickandmorty.db.App
import com.example.rickandmorty.model.CertainEpisodeModel
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.viewmodel.CharactersViewModel
import com.example.rickandmorty.viewmodel.EpisodeViewModelCharacters
import com.example.rickandmorty.visible
import kotlinx.coroutines.*

class EpisodeDetails : Fragment(R.layout.fragment_details_episode) {
    private lateinit var binding: FragmentDetailsEpisodeBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    var episodeId: Int = 0
    lateinit var episode: CertainEpisodeModel
    private val adapter = CharactersAdapter()
    lateinit var navController: NavController
    var idArray = mutableListOf<String>()
    private var episodeViewModelCharacters: EpisodeViewModelCharacters? = null
    private var charactersViewModel : CharactersViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodeId = arguments?.getInt("episode")!!
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        charactersViewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)

        episodeViewModelCharacters =
            ViewModelProvider(this).get(EpisodeViewModelCharacters::class.java)

        navController = Navigation.findNavController(view)



        adapter.setOnClickListener {
            val action = EpisodeDetailsDirections.actionEpisodeDetailsToCharacterFragment(it)
            navController.navigate(action)
        }


        episodeViewModelCharacters?.getEpisodeById(episodeId)?.observe(viewLifecycleOwner, {
            episode = it
            binding.titleTextView.text = it.name
            binding.episodeTextView.text = it.episode
            binding.airDateTextView.text = it.airDate
            for (i in episode.characters) {
                idArray.add(i.substring(42))
            }
            loadEpisodes()
        })




    }

    private fun loadEpisodes() {
        coroutineScope.launch {
            val charactersArray = withContext(Dispatchers.Main.immediate) {
                val data = mutableListOf<Character>()
                for (i in idArray) {
                    val char = App.instance.db.getCharacterDao().getCharacterById(i.toInt())
                    if (char != null) {
                        data.add(char)
                    } else {
                        charactersViewModel?.getCharacterById(i.toInt())?.observe(viewLifecycleOwner,{
                            data.add(it)
                            App.instance.db.getCharacterDao().insert(it)
                        })

                    }
                }
                data
            }
            binding.loadingProgress.visible(false)
            adapter.setData(charactersArray)
        }
    }

    fun initRecycler() {
        binding.recyclerviewDetals.apply {
            adapter = this@EpisodeDetails.adapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

}