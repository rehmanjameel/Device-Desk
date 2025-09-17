package com.arconn.devicedesk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.arconn.devicedesk.adapter.UserAdapter
import com.arconn.devicedesk.databinding.ActivityMainBinding
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.helpers.RetrofitHelper
import com.arconn.devicedesk.iterfaces.UsersAPI
import com.arconn.devicedesk.model.UsersModel
import com.arconn.devicedesk.repository.UserRepository
import com.arconn.devicedesk.viewmodel.UserViewModelFactory
import com.arconn.devicedesk.viewmodel.UsersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private val usersViewModel: UsersViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitHelper.apiService))
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.usersRV.layoutManager = LinearLayoutManager(this)

        fetchUsers()

        binding.addFB.setOnClickListener {
            val intent = Intent(this, AddUser::class.java)
            startActivity(intent)
        }
    }

    fun fetchUsers() {
        // call retrofit api here
        usersViewModel.users.observe(this) {state ->
            when(state) {
                is Resource.Loading<*> -> Log.e("loading", "some loading")

                is Resource.Success -> {
//                    binding.tv.text = state.data.toString()
                    userAdapter = UserAdapter(this, state.data)
                    binding.usersRV.adapter = userAdapter

                    Log.e("api call", state.toString())
                }
                is Resource.Error -> {
                    binding.tv.text = "error"
                    Log.e("errors", "some error")
                }

            }
        }

        usersViewModel.loadUsers()
//        val usersAPI = RetrofitHelper.getInstance().create(UsersAPI::class.java)
//
//        // launching a coroutines
//        GlobalScope.launch {
////            val result = usersAPI.getUsers()
//            val call: Call<List<UsersModel>> = usersAPI.getUsers()
//
//            call.enqueue(object : Callback<List<UsersModel>> {
//                override fun onResponse(
//                    call: Call<List<UsersModel>?>,
//                    response: Response<List<UsersModel>?>
//                ) {
//                    if (response.isSuccessful)
//                        Log.e("users list success", response.body().toString())
//                }
//
//                override fun onFailure(
//                    call: Call<List<UsersModel>?>,
//                    t: Throwable
//                ) {
//                    Log.e("users list failure", call.toString())
//                }
//
//            })
//        }
    }

    override fun onResume() {
        super.onResume()
        fetchUsers()
        binding.refreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                // refetch users from api
                fetchUsers()
                // stop refreshing
                binding.refreshLayout.isRefreshing = false
            }, 1500)
        }
    }
}