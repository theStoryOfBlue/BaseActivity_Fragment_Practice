package com.example.baseactivity_fragment_practice.fragment.secondeFragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseactivity_fragment_practice.R
import com.example.baseactivity_fragment_practice.databinding.FragmentSecondeBinding
import com.example.baseactivity_fragment_practice.fragment.base.BaseFragment
import com.example.baseactivity_fragment_practice.fragment.mainFragment.MainAdapter
import com.example.baseactivity_fragment_practice.room.Entity
import com.example.baseactivity_fragment_practice.viewModel.MainViewModel
import com.example.domain.model.DomainData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondeFragment : BaseFragment<FragmentSecondeBinding>(FragmentSecondeBinding::inflate) {

    private val activityViewModel : MainViewModel by activityViewModels()
    private val adapter = SecondAdapter(clickListener = { getDialog(it) })
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.getAllData.observe(viewLifecycleOwner){
            if(it==null) Log.e("TAG", "onViewCreated: no data!", )
            else {
                adapter.dataList = it
            }
            checkDataSize()
        }
        activityViewModel.getAllData()
        initRecyclerView()
        checkDataSize()

    }

    fun initRecyclerView(){
        binding.secondRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.secondRecycler.adapter = adapter
    }

    fun getDialog(list: Entity){
        AlertDialog.Builder(requireContext())
            .setTitle("삭제")
            .setMessage("${list.name}를 삭제 하시겠습니까?")
            .setPositiveButton("확인") { dialog, id ->
                activityViewModel.removeData(list.name)
                activityViewModel.getAllData()
            }
            .setNegativeButton("취소"){ dialog, id -> }.show()
    }

    fun checkDataSize(){
        binding.textView3.isVisible = adapter.dataList.isEmpty()
    }
}