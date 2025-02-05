package com.example.bionime.ui

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bionime.viewmodel.MaskViewModel
import com.example.bionime.viewmodel.MaskViewModelFactory
import com.example.bionime.R
import com.example.bionime.data.MaskDatabase
import com.example.bionime.databinding.ActivityMainBinding
import com.example.bionime.repository.MaskRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MaskViewModel
    private lateinit var adapter: MaskAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var spinnerAdapter: ArrayAdapter<String>

    //紀錄當前spinner選單選項
    private var currentTown: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = MaskRepository(MaskDatabase.getDatabase(this).maskDao())
        viewModel =
            ViewModelProvider(this, MaskViewModelFactory(repository)).get(MaskViewModel::class.java)
        viewModel.refreshData();
        setupRecyclerView()
        setupSpinner()
        setupFloatingActionButton()
        setupSwipeRefresh()

        observeViewModel()
    }

    private fun setupFloatingActionButton() {
        binding.floatingActionButton.setOnClickListener {
            lifecycleScope.launch {
                val quote = viewModel.fetchQuote()
                Toast.makeText(this@MainActivity, quote, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MaskAdapter { mask ->
            showDeleteDialog(mask.name)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter

            val dividerItemDecoration =
                DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun setupSpinner() {
        spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = spinnerAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedTown = parent.getItemAtPosition(position) as String
                currentTown = if (selectedTown == "全部") null else selectedTown
                viewModel.updateMasks(currentTown)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    private fun observeViewModel() {
        viewModel.masks.observe(this) { masks ->
            adapter.setMasks(masks)
        }

        viewModel.towns.observe(this) { towns ->
            spinnerAdapter.clear()
            spinnerAdapter.addAll(towns)
            spinnerAdapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }
    }

    private fun showDeleteDialog(name: String) {
        AlertDialog.Builder(this).setTitle("刪除確認").setMessage("是否要刪除 $name ?")
            .setPositiveButton("是") { _, _ ->
                viewModel.deleteMask(name)
            }.setNegativeButton("否", null).show()
    }
}