package com.erikriosetiawan

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erikriosetiawan.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding
import com.erikriosetiawan.mysharedpreferences.models.UserModel
import com.erikriosetiawan.mysharedpreferences.preferences.UserPreference

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFormUserPreferenceBinding
    private lateinit var userModel: UserModel

    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener(this)

        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when (formType) {
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSave.text = btnTitle
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnSave.id) {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val age = binding.etAge.text.toString()
            val phoneNo = binding.etPhone.text.toString()
            val isLoveMU = binding.rgLoveMu.checkedRadioButtonId == binding.rbYes.id

            if (name.isEmpty()) {
                binding.etName.error = FIELD_REQUIRED
                return
            }

            if (email.isEmpty()) {
                binding.etEmail.error = FIELD_REQUIRED
                return
            }

            if (!isValidEmail(email)) {
                binding.etEmail.error = FIELD_IS_NOT_VALID
                return
            }

            if (age.isEmpty()) {
                binding.etAge.error = FIELD_REQUIRED
                return
            }

            if (phoneNo.isEmpty()) {
                binding.etPhone.error = FIELD_REQUIRED
                return
            }

            if (!TextUtils.isDigitsOnly(phoneNo)) {
                binding.etPhone.error = FIELD_DIGIT_ONLY
                return
            }

            saveUser(name, email, age, phoneNo, isLoveMU)

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)
            setResult(RESULT_CODE, resultIntent)

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveUser(
        name: String,
        email: String,
        age: String,
        phoneNo: String,
        isLoveMu: Boolean
    ) {
        val userPreference = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phoneNo
        userModel.isLove = isLoveMu

        userPreference.setUser(userModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showPreferenceInForm() {
        binding.apply {
            etName.setText(userModel.name)
            etEmail.setText(userModel.email)
            etAge.setText(userModel.age.toString())
            etPhone.setText(userModel.phoneNumber)
            if (userModel.isLove) {
                rbYes.isChecked = true
            } else {
                rbNo.isChecked = true
            }
        }
    }
}