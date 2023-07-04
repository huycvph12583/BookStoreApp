package com.app.bookstoreapp.screens

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.bookstoreapp.R
import com.app.bookstoreapp.databinding.FragmentSigninScreenBinding
import com.app.bookstoreapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class SigninScreenFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    var day = 0
    var month = 0
    var year = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    private val TAG = "ScreenSignUp"
    private lateinit var database: DatabaseReference
    private var auth: FirebaseAuth = Firebase.auth

    private lateinit var binding: FragmentSigninScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("Users")
        lister()
    }

    private fun lister() {
        binding.apply {
            btnSignUp.setOnClickListener {
                checkValidateAndSingUp()
            }
            imgBack.setOnClickListener { findNavController().popBackStack() }
            edtDateOfBirth.setOnClickListener {

                getDateTimeCalender()
                DatePickerDialog(
                    requireContext(),
                    this@SigninScreenFragment,
                    year,
                    month,
                    day
                ).show()
            }
        }
    }

    private fun signUpWithEmail(
        email: String,
        nameUser: String,
        password: String,
        address: String,
        date: String,
    ) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "Creat Account Success")
                val uId: String = task.result.user!!.uid
                database.child(uId).setValue(
                    User(
                        uId,
                        nameUser,
                        "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png",
                        address,
                        email,
                        password, date
                    )
                )
                findNavController().navigate(R.id.loginScreenFragment)
            } else {
                Log.w(TAG, "Creat Account Success", task.exception)
                Toast.makeText(
                    context, "Authentication Failed: Account already exists.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkValidateAndSingUp() {
        binding.apply {
            if (edtSignUpUsername.text.isNullOrEmpty()) {
                edtSignUpUsername.error = "Username not empty"
            } else if (edtSignUpEmail.text.isNullOrEmpty()) {
                edtSignUpEmail.error = "Email not empty"
            } else if (edtSignUpPasswords.text.isNullOrEmpty()) {
                edtSignUpPasswords.error = "Pass not empty"
            } else if (edtAddress.text.isNullOrEmpty()) {
                edtAddress.error = "Address not empty"
            } else if (edtDateOfBirth.text.isNullOrEmpty()) {
                edtDateOfBirth.error = "Date of birth not empty"
            } else {
                signUpWithEmail(
                    edtSignUpEmail.text.toString(),
                    edtSignUpUsername.text.toString(),
                    edtSignUpPasswords.text.toString(),
                    edtAddress.text.toString(),
                    edtDateOfBirth.text.toString()
                )
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDay = dayOfMonth
        saveMonth = month
        saveYear = year
        binding.edtDateOfBirth.setText("$saveDay-$saveMonth-$saveYear")
    }

    private fun getDateTimeCalender() {
        val cal: Calendar = Calendar.getInstance()
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        year = cal.get(Calendar.YEAR)
    }
}
