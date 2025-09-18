package com.arconn.devicedesk.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.arconn.devicedesk.R
import com.arconn.devicedesk.adapter.UserAdapter
import com.arconn.devicedesk.databinding.FragmentUsersBinding
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.helpers.RetrofitHelper
import com.arconn.devicedesk.repository.UserRepository
import com.arconn.devicedesk.utils.AppGlobals
import com.arconn.devicedesk.viewmodel.UserViewModelFactory
import com.arconn.devicedesk.viewmodel.UsersViewModel

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding

    private lateinit var userAdapter: UserAdapter

    private val usersViewModel: UsersViewModel by viewModels {
        UserViewModelFactory(UserRepository(RetrofitHelper.apiService))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false)

        binding.usersRV.layoutManager = LinearLayoutManager(AppGlobals.applicationContext())

        fetchUsers()

        binding.addFB.setOnClickListener {

            findNavController().navigate(R.id.action_usersFragment_to_addUserFragment)
        }

        return binding.root
    }

    fun fetchUsers() {
        // call retrofit api here
        usersViewModel.users.observe(viewLifecycleOwner) {state ->
            when(state) {
                is Resource.Loading<*> -> Log.e("loading", "some loading")

                is Resource.Success -> {
                    userAdapter = UserAdapter(AppGlobals.applicationContext(), state.data)
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