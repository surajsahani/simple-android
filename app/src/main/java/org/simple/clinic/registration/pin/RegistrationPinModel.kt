package org.simple.clinic.registration.pin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simple.clinic.registration.pin.RegistrationPinValidationResult.NotValidated
import org.simple.clinic.user.OngoingRegistrationEntry

@Parcelize
data class RegistrationPinModel(
    val ongoingRegistrationEntry: OngoingRegistrationEntry,
    val pinValidationResult: RegistrationPinValidationResult
) : Parcelable {

  companion object {
    fun create(registrationEntry: OngoingRegistrationEntry): RegistrationPinModel = RegistrationPinModel(
        ongoingRegistrationEntry = registrationEntry,
        pinValidationResult = NotValidated
    )
  }

  fun pinChanged(pin: String): RegistrationPinModel {
    return copy(ongoingRegistrationEntry = ongoingRegistrationEntry.withPin(pin))
  }
}
