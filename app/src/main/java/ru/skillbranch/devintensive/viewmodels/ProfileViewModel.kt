package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.utils.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository

class ProfileViewModel: ViewModel() {

    private var repository: PreferencesRepository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val isRepositotyValid = MutableLiveData<Boolean>()

    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
        isRepositotyValid.value = true
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun getTheme(): LiveData<Int> = appTheme

    fun getIsRepositoryValid(): LiveData<Boolean> = isRepositotyValid

    fun saveProfileData(profile: Profile) {
        val profileForSave = if(!isRepositotyValid.value!!) profile.copy(repository = "") else profile
        isRepositotyValid.value = true
        repository.saveProfile(profileForSave)
        profileData.value = profileForSave
    }

    fun switchTheme() {
        if(appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        }
        else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun validationRepository(string: String) {
        if(string.isEmpty()) {
            isRepositotyValid.value = true
        }
        else {
            val exceptions = arrayOf(
                "enterprise", "features", "topics", "collections", "trending", "events", "marketplace", "pricing",
                "nonprofit", "customer-stories", "security", "login", "join"
            )
            val strExceptions = exceptions.joinToString("|")
            val regex = Regex("^(https://)?(www\\.)?(github\\.com/)(?!(${strExceptions})(?=/|$))(?![\\W])(?!\\w+[-]{2})[a-zA-Z0-9-]+(?<![-])(/)?$")

            isRepositotyValid.value = regex.matches(string)
        }
    }
}