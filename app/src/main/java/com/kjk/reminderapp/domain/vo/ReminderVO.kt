package com.kjk.reminderapp.domain.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 *  domain object VO이다.
 *  UI Controller에서 사용하기 위한 VO
 */
@Parcelize
data class ReminderVO(
    var id: Long,
    var title: String,
    var settingTime: Long,
    var ringTonePath: String,
    var ringToneTitle: String,
    var isActivate: Boolean = false
) : Parcelable