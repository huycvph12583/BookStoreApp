package com.app.bookstoreapp.screens


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.BuildConfig
import com.app.bookstoreapp.R
import com.app.bookstoreapp.ViewModel.MainViewModel
import com.app.bookstoreapp.databinding.FragmentLoginScreenBinding
import com.app.bookstoreapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class LoginScreenFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private val sharedPreference by lazy {
        requireActivity().getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}_sharePreferences",
            Context.MODE_PRIVATE
        )
    }
    private val IMAGE_View = stringPreferencesKey("image")
    private val TAG = "LoginScreen"
    private lateinit var binding: FragmentLoginScreenBinding
    private var auth: FirebaseAuth = Firebase.auth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        lister()
    }

    private fun lister() {
        binding.apply {
            btnLogin.setOnClickListener {
                it.isEnabled = false
                checkValidate()
            }
            binding.tvLoginSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_loginScreenFragment_to_signinScreenFragment)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
        }
    }

    private fun loginWithEmail(emailUser: String, password: String) {
        auth.signInWithEmailAndPassword(emailUser, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                Log.d(TAG, "signInWithEmail:success")
                val user = auth.currentUser
                getData()
            } else {
                binding.btnLogin.isEnabled = true
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Login Fail Check Email Or Pass !", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun loginSucess(user: String) {
        Toast.makeText(context, "Login Sucess $user ", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_loginScreenFragment_to_homeScreenFragment)
    }

    private fun checkValidate() {

        binding.apply {
            if (edtLoginUsername.text.isNullOrEmpty()) {
                edtLoginUsername.error = "Username not empty"
                btnLogin.isEnabled = true
            } else if (edtLoginPasswords.text.isNullOrEmpty()) {
                edtLoginPasswords.error = "Pass not empty"
                btnLogin.isEnabled = true
            } else {
                progressBar.visibility = View.VISIBLE
                if (isOnline(requireContext())) {
                    loginWithEmail(
                        edtLoginUsername.text.toString(),
                        edtLoginPasswords.text.toString()
                    )
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "no internet connection", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun getData(){
        val database = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid.toString())
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)!!
                if (!user.uid.isNullOrEmpty()) {
                    saveUser(user = user)
                    loginSucess(user = user.username!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        )
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun saveUser(user: User) {
        var editor = sharedPreference.edit()
        editor.putString("userId", user.uid)
        editor.putString("username", user.username)
        editor.putString("userImage", user.linkImage)
        editor.putString("userAddress", user.addressUser)
        editor.commit()
    }
}
