package com.awp.samakaki.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.awp.samakaki.R
import com.otaliastudios.opengl.core.use


object SessionManager {

    const val USER_TOKEN = "user_token"
    const val USER_ID = "user_id"
    const val INVITATION_TOKEN = "invitation_token"
    const val USERNAME = "username"

    fun saveAuthToken(context: Context, token: String) {
        saveString(context, USER_TOKEN, token)
    }

    fun saveIdUser(context: Context, id: Int) {
        saveInt(context, USER_ID, id)
    }

    fun saveInvitation(context: Context, invitationToken: String) {
        saveStringInvite(context, INVITATION_TOKEN, invitationToken)
    }

    fun getInvitation(context: Context): String? {
        return getStringInvite(context, INVITATION_TOKEN)
    }

    fun getIdUser(context: Context): Int? {
        return getInt(context, USER_ID)
    }

    fun getToken(context: Context): String? {
        return getString(context, USER_TOKEN)
    }

    fun saveStringInvite(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringInvite(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(this.INVITATION_TOKEN, null)
    }

    fun saveString(context: Context, key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(this.USER_TOKEN, null)
    }

    fun saveInt(context: Context, key: String, value: Int) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(context: Context, key: String): Int? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getInt(this.USER_ID, 0)
    }

    fun removeInvitationToken(context: Context) {
        val preferences = context.getSharedPreferences(context.getString(R.string.app_name), Activity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove(INVITATION_TOKEN)
        editor.apply()
    }

    fun clearData(context: Context){
        val editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }
}