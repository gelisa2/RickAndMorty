package com.example.rickandmorty.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.EpisodeAdapterForCharacters
import com.example.rickandmorty.adapter.EpisodesAdapter
import com.example.rickandmorty.api.ApiUtils
import com.example.rickandmorty.databinding.FragmentCharacterBinding
import com.example.rickandmorty.db.App
import com.example.rickandmorty.model.CertainEpisodeModel
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.setImage
import com.example.rickandmorty.viewmodel.CharactersViewModel
import com.example.rickandmorty.viewmodel.EpisodeViewModelCharacters
import com.example.rickandmorty.visible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CharacterFragment : Fragment(R.layout.fragment_character) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var binding: FragmentCharacterBinding
    var character = ""
    var adapter = EpisodeAdapterForCharacters()
    private lateinit var navController: NavController
     var episodeViewModelCharacters : EpisodeViewModelCharacters? = null
     var charactersViewModel : CharactersViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        character = arguments?.getString("character", "Rick Sanchez").toString()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        charactersViewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)

        episodeViewModelCharacters = ViewModelProvider(this).get(EpisodeViewModelCharacters::class.java)

        navController = Navigation.findNavController(view)

        coroutineScope.launch {
            val id = App.instance.db.getCharacterDao().getCharacterByName(character)?.id
            var char : Character
                charactersViewModel?.getCharacterByName(character)?.observe(viewLifecycleOwner,{
                    char = it.results[0]
                    binding.image.setImage(char.image!!)
                    binding.name.text = char.name
                    binding.status.text = char.status
                    val charInEpisodesIds = mutableListOf<String>()
                    val episodesList = mutableListOf<CertainEpisodeModel>()

                    char.episode?.forEach { charInEpisodesIds.add(it.substring(40)) }

                    for (episode in charInEpisodesIds) {
                        episodeViewModelCharacters?.getEpisodeById(episode.toInt())?.observe(viewLifecycleOwner, { model ->
                            episodesList.add(model)
                            binding.loadingProgress.visible(false)
                            adapter.setData(episodesList)

                        })
                    }
                })







        }

        adapter.setOnClickListener {
            val action = CharacterFragmentDirections.actionCharacterFragmentToEpisodeDetails(it)
            navController.navigate(action)
        }
    }

    fun initRecyclerView() {
        binding.recyclerviewCharacter.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CharacterFragment.adapter
        }
    }
}