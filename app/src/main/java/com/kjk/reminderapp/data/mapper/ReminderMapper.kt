package com.kjk.reminderapp.data.mapper

import com.kjk.reminderapp.data.local.ReminderEntity
import com.kjk.reminderapp.domain.vo.ReminderVO


/**
 *  database로 부터 fetch한 data를
 *  UI Controller에서 사용하기 위해, domain object로 변경한다.
 *
 */
fun List<ReminderEntity>.toDomainListModel(): List<ReminderVO> {
    return map {
        ReminderVO(
            id = it.reminderId,
            title = it.title,
            settingTime = it.settingTime,
            ringTonePath = it.ringTonePath,
            ringToneTitle = it.ringToneTitle,
            isActivate = it.isActivate
        )
    }
}



fun ReminderEntity.toDomainModel(): ReminderVO{
    return ReminderVO(
        reminderId,
        title,
        settingTime,
        ringTonePath,
        ringToneTitle,
        isActivate
    )
}



/**
 *  domain object를 room에 저장하기 위해
 *  database model object로
 *  변환하는 함수
 */
fun ReminderVO.toDatabaseModel(): ReminderEntity {
    return ReminderEntity(
        id,
        title,
        settingTime,
        ringTonePath,
        ringToneTitle,
        isActivate
    )
}
