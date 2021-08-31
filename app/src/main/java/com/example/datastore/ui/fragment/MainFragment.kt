package com.example.datastore.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import com.example.datastore.DataStoreClass.DataStoreRepository
import com.example.datastore.R
import com.example.datastore.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    lateinit var binding : FragmentMainBinding
    lateinit var dataStoreRepository: DataStoreRepository
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreRepository = DataStoreRepository(context = requireActivity())

        // show name after entry from input on textView.
        lifecycle.coroutineScope.launchWhenCreated {
            dataStoreRepository.getUserName().collect {
                binding.tvShowName.text = it
            }
        }

        // show age after entry from input on textView.
        lifecycle.coroutineScope.launchWhenCreated {
            dataStoreRepository.getUserAge().collect {
                binding.tvShowAge.text = it.toString()
            }
        }

        // show name after entry from input on Edit Text again.
        lifecycle.coroutineScope.launchWhenCreated {
            dataStoreRepository.getUserName().collect {
                binding.etEnterName.setText(it)
            }
        }

        // show age after entry from input on Edit Text again.
        lifecycle.coroutineScope.launchWhenCreated {
            dataStoreRepository.getUserAge().collect {
                binding.etEnterAge.setText(it.toString())
            }
        }

        // btn save data after entry in input to DataStore Preference.
        binding.btnSave.setOnClickListener {

            if(binding.etEnterName.text.toString().trim().isEmpty()){
                Toast.makeText(requireActivity(),"Please enter your name",Toast.LENGTH_SHORT).show()
            }else if(binding.etEnterAge.text.toString().trim().isEmpty()){
                Toast.makeText(requireActivity(),"Please enter your age",Toast.LENGTH_SHORT).show()
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    dataStoreRepository.storeUser(
                        binding.etEnterName.text.toString().trim(),
                        binding.etEnterAge.text.toString().trim().toInt()
                    )
                }
            }
        }
    }
}
