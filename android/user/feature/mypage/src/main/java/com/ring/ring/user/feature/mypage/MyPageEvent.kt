package com.ring.ring.user.feature.mypage

sealed class MyPageEvent {
    object EditSuccess : MyPageEvent()
    object EditError : MyPageEvent()
    object WithdrawalSuccess : MyPageEvent()
    object WithdrawalError : MyPageEvent()
    object LogoutSuccess : MyPageEvent()
    object LogoutError : MyPageEvent()
    object UnauthorizedError : MyPageEvent()
}