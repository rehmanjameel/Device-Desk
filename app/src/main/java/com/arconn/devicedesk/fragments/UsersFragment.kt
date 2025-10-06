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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.arconn.devicedesk.R
import com.arconn.devicedesk.adapter.UserAdapter
import com.arconn.devicedesk.databinding.FragmentUsersBinding
import com.arconn.devicedesk.helpers.Resource
import com.arconn.devicedesk.helpers.RetrofitHelper
import com.arconn.devicedesk.iterfaces.ItemClickListener
import com.arconn.devicedesk.model.UsersModel
import com.arconn.devicedesk.repository.UserRepository
import com.arconn.devicedesk.utils.AppGlobals
import com.arconn.devicedesk.viewmodel.UserViewModelFactory
import com.arconn.devicedesk.viewmodel.UsersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class UsersFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentUsersBinding

    private lateinit var userAdapter: UserAdapter
    var swipeRefreshLayout: SwipeRefreshLayout? = null

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

        binding.addFB.setOnClickListener {

            findNavController().navigate(R.id.action_usersFragment_to_addUserFragment)
        }
        swipeRefreshLayout = binding.refreshLayout

        // call the delete observer once
        deleteObserver()

        return binding.root
    }

    fun fetchUsers() {
        // call retrofit api here
        usersViewModel.users.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading<*> -> Log.e("loading", "some loading")

                is Resource.Success -> {
                    userAdapter = UserAdapter(AppGlobals.applicationContext(), state.data)
                    userAdapter.listener = this
                    binding.usersRV.adapter = userAdapter
                    binding.tv.text = getString(R.string.app_name)

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

    override fun onDeleteClick(item: UsersModel) {

        showDialog(item)
    }

    override fun onEditClick(item: UsersModel) {

        val action = UsersFragmentDirections.actionUsersFragmentToEditUserFragment(item.id,
            item.name, item.email)
        findNavController().navigate(action)
    }

    private fun showDialog(item: UsersModel) {

        val materialDialog = MaterialAlertDialogBuilder(requireActivity())

        materialDialog.setTitle("Delete User")
        materialDialog.setMessage("Are you sure you want to delete this user?")

        materialDialog.setPositiveButton("Yes") { dialog, which ->

            usersViewModel.deleteUser(item.id)

            dialog.dismiss()
        }

        materialDialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        materialDialog.show()
    }

    private fun deleteObserver() {
        usersViewModel.userDeletion.observe(viewLifecycleOwner) {state ->

            when(state) {
                is Resource.Loading<*> -> Log.e("loading", "delete loading $state")

                is Resource.Success -> {
                    Log.e("loading", "data deleted successfully $state")
                    fetchUsers()

                }

                is Resource.Error -> {
                    Log.e("error", "some error $state")
                }
            }
        }
    }
}
