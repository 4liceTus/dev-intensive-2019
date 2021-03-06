package ru.skillbranch.devintensive.ui.profile

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.utils.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
        viewModel.getIsRepositoryValid().observe(this, Observer { updateRepository(it) })
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }
        updateAvatar(profile)
    }

    private fun updateRepository(isValid: Boolean) {
        if(isValid) {
            wr_repository.isErrorEnabled = false
            wr_repository.error = null
        }
        else {
            wr_repository.isErrorEnabled = true
            wr_repository.error = getString(R.string.repository_error_message)
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if(isKeyboardOpen()) hideKeyboard()
            if(isEditMode) saveProfileData()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        et_repository.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validationRepository(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {
                //if(isKeyboardOpen()) hideKeyboard()
            }
        } )
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if(isEdit) 255 else 0
        }

        ic_eye.visibility = if(isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if(isEdit) {
                PorterDuffColorFilter(
                    getColorFromTheme(R.attr.colorAccent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            }
            else null

            val icon = if(isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            }
            else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }
            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun getColorFromTheme(resID: Int, theme: Resources.Theme): Int {
        val color = TypedValue()
        theme.resolveAttribute(resID, color, true)
        return color.data
    }

    private fun saveProfileData() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun updateAvatar(profile: Profile) {
        /*
        Utils.toInitials(profile.firstName, profile.lastName)?.let {
                val avatar = genAvatar(it)
                iv_avatar.setImageBitmap(avatar)
        } ?: iv_avatar.setImageResource(R.drawable.avatar_default)
        */

        val initials = Utils.toInitials(profile?.firstName, profile?.lastName)
        val drawable = if (initials==null) {
            resources.getDrawable(R.drawable.ic_avatar, theme)
        } else {
            val color = TypedValue()
            theme.resolveAttribute(R.attr.colorAccent, color, true)
            ColorDrawable(color.data)
        }

        iv_avatar.setImageDrawable(drawable)
        iv_avatar.setText(initials)
    }

    /*
    private fun genAvatar(text: String):Bitmap {
        val color = getColorFromTheme(R.attr.colorAccent, theme)
        return TextImageBuilder(iv_avatar.layoutParams.width, iv_avatar.layoutParams.height)
            .setBackgroundColor(color)
            .setText(text)
            .setTextSize(72)
            .build()
    }
    */
}



