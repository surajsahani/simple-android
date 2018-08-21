package org.simple.clinic.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.annotation.StringRes
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io
import org.simple.clinic.ClinicApp
import org.simple.clinic.R
import org.simple.clinic.login.LoginResult
import org.simple.clinic.sync.SyncScheduler
import org.simple.clinic.user.UserSession
import timber.log.Timber
import javax.inject.Inject

private const val OTP_START_INDEX = 4
private const val OTP_LENGTH = 6

class SmsBroadcastReceiver : BroadcastReceiver() {

  @Inject
  lateinit var userSession: UserSession

  @Inject
  lateinit var syncScheduler: SyncScheduler

  init {
    ClinicApp.appComponent.inject(this)
  }

  override fun onReceive(context: Context, intent: Intent) {
    val extras = intent.extras
    val status = extras[SmsRetriever.EXTRA_STATUS] as Status

    when (status.statusCode) {
      CommonStatusCodes.SUCCESS -> {
        val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String

        // I don't like magic numbers, but I am not sure of the best place to put this since the message is hard coded
        val otp = message.substring(OTP_START_INDEX, OTP_START_INDEX + OTP_LENGTH)

        // TODO: Schedule call with worker instead of logging in directly
        userSession.loginWithOtp(otp)
            .subscribeOn(io())
            .flatMap { syncScheduler.syncImmediately().toSingleDefault(it) }
            .observeOn(mainThread())
            .subscribe({
              when (it) {
                is LoginResult.Success -> showToast(context, R.string.loginpin_you_are_now_logged_in)
                is LoginResult.UnexpectedError -> showToast(context, R.string.api_unexpected_error)
                is LoginResult.NetworkError -> showToast(context, R.string.api_network_error)
                is LoginResult.ServerError -> showToast(context, it.error)
              }
            }, {
              Timber.e(it, "Could not login with OTP!")
              showToast(context, R.string.api_unexpected_error)
            })
      }
    }
  }

  private fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
  }

  private fun showToast(context: Context, @StringRes msgResId: Int) {
    Toast.makeText(context, msgResId, Toast.LENGTH_LONG).show()
  }
}
