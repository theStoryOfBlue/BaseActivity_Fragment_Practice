package com.example.baseactivity_fragment_practice

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.baseactivity_fragment_practice.databinding.ActivityMainBinding
import com.example.baseactivity_fragment_practice.viewModel.base.FetchState
import com.example.baseactivity_fragment_practice.viewModel.MainViewModel
import com.example.baseactivity_fragment_practice.room.Database
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, Database::class.java, "MyDB").build()
        viewModel.initDB(database)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.framLayout) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        viewModel.fetchState.observe(this){
            var message = when(it.second){
                FetchState.BAD_INTERNET -> "BAD_INTERNET 오류"
                FetchState.PARSE_ERROR -> "PARSE_ERROR 오류"
                FetchState.WRONG_CONNECTION -> "WRONG_CONNECTION 오류"
                FetchState.SQLITE_CONSTRAINT_PRIMARYKEY -> "이미 저장되어있습니다!"
                else -> "${it.first.message} 오류"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }


    }

    fun downKeyBoard(){
        if(this.currentFocus != null){
            val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}
