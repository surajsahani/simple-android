package org.simple.clinic.home.patients

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.simple.clinic.appupdate.AppUpdateNudgePriority
import org.simple.clinic.appupdate.AppUpdateNudgePriority.MEDIUM
import org.simple.clinic.user.User
import org.simple.clinic.util.toNullable
import java.util.Optional

@Parcelize
data class PatientsTabModel(
    val user: User?,
    val numberOfPatientsRegistered: Int?,
    val appStaleness: Int?,
    val appUpdateNudgePriority: AppUpdateNudgePriority?,
    val isDrugStockReportFilled: Boolean?
) : Parcelable {

  companion object {

    fun create(): PatientsTabModel = PatientsTabModel(
        user = null,
        numberOfPatientsRegistered = null,
        appStaleness = null,
        appUpdateNudgePriority = null,
        isDrugStockReportFilled = null
    )
  }

  val hasLoadedUser: Boolean
    get() = user != null

  val hasLoadedNumberOfPatientsRegistered: Boolean
    get() = numberOfPatientsRegistered != null

  val hasAppStaleness
    get() = appStaleness != null

  val hasAppUpdateNudgePriority
    get() = appUpdateNudgePriority != null

  val appUpdateNudgePriorityIsMedium
    get() = hasAppUpdateNudgePriority && appUpdateNudgePriority == MEDIUM

  fun userLoaded(user: User): PatientsTabModel {
    return copy(user = user)
  }

  fun numberOfPatientsRegisteredUpdated(numberOfPatientsRegistered: Int): PatientsTabModel {
    return copy(numberOfPatientsRegistered = numberOfPatientsRegistered)
  }

  fun updateAppStaleness(appStaleness: Int?): PatientsTabModel {
    return copy(appStaleness = appStaleness)
  }

  fun appUpdateNudgePriorityUpdated(appUpdateNudgePriority: AppUpdateNudgePriority?): PatientsTabModel {
    return copy(appUpdateNudgePriority = appUpdateNudgePriority)
  }

  fun updateIsDrugStockFilled(isDrugStockReportFilled: Optional<Boolean>): PatientsTabModel {
    return copy(isDrugStockReportFilled = isDrugStockReportFilled.toNullable())
  }
}
