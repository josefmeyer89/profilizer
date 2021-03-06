package com.profilizer.personalitytest.contracts

import com.profilizer.common.BasePresenter
import com.profilizer.common.BaseView
import com.profilizer.personalitytest.model.PersonalityTest

interface StartTestContract {

    interface View: BaseView {
        fun onCreateFinish(personalityTest: PersonalityTest)
        fun showErrorMessage()
        fun validateFields()
        fun startCreating()
    }

    interface Presenter: BasePresenter {
        fun createPersonalityTest(userName: String)
        fun loadTestQuestions()
    }
}