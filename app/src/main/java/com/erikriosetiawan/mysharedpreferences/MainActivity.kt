package com.erikriosetiawan.mysharedpreferences

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.erikriosetiawan.FormUserPreferenceActivity
import com.erikriosetiawan.FormUserPreferenceActivity.Companion.EXTRA_RESULT
import com.erikriosetiawan.FormUserPreferenceActivity.Companion.EXTRA_TYPE_FORM
import com.erikriosetiawan.FormUserPreferenceActivity.Companion.RESULT_CODE
import com.erikriosetiawan.FormUserPreferenceActivity.Companion.TYPE_ADD
import com.erikriosetiawan.FormUserPreferenceActivity.Companion.TYPE_EDIT
import com.erikriosetiawan.mysharedpreferences.databinding.ActivityMainBinding
import com.erikriosetiawan.mysharedpreferences.models.UserModel
import com.erikriosetiawan.mysharedpreferences.preferences.UserPreference

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel

    companion object {
        private const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My User Preference"

        mUserPreference = UserPreference(this)

        showExistingPreference()

        binding.btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnSave.id) {
            val intent = Intent(this@MainActivity, FormUserPreferenceActivity::class.java)
            when {
                isPreferenceEmpty -> {
                    intent.putExtra(EXTRA_TYPE_FORM, TYPE_ADD)
                    intent.putExtra("USER", userModel)
                }
                else -> {
                    intent.putExtra(EXTRA_TYPE_FORM, TYPE_EDIT)
                    intent.putExtra("USER", userModel)
                }
            }
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_CODE) {
                userModel = data?.getParcelableExtra<UserModel>(EXTRA_RESULT) as UserModel
                populateView(userModel)
                checkForm(userModel)
            }
        }
    }

    private fun showExistingPreference() {
        userModel = mUserPreference.getUser()
        populateView(userModel)
        checkForm(userModel)
    }

    private fun populateView(userModel: UserModel) {
        binding.apply {
            tvName.text = if (userModel.name.toString().isEmpty()) "Tidak Ada" else userModel.name
            tvAge.text =
                if (userModel.age.toString().isEmpty()) "Tidak Ada" else userModel.age.toString()
            tvIsLoveMu.text = if (userModel.isLove) "Ya" else "Tidak"
            tvEmail.text =
                if (userModel.email.toString().isEmpty()) "Tidak Ada" else userModel.email
            tvPhone.text = if (userModel.phoneNumber.toString()
                    .isEmpty()
            ) "Tidak Ada" else userModel.phoneNumber
        }
    }

    private fun checkForm(userModel: UserModel) {
        when {
            userModel.name.toString().isNotEmpty() -> {
                binding.btnSave.text = getString(R.string.change)
                isPreferenceEmpty = false
            }
            else -> {
                binding.btnSave.text = getString(R.string.save)
                isPreferenceEmpty = true
            }
        }
    }
}