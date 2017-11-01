package com.profilizer.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.profilizer.ProfilizerApplication
import com.profilizer.R
import com.profilizer.activities.PersonalityTestActivity
import com.profilizer.common.ValidationException
import com.profilizer.common.hide
import com.profilizer.common.visible
import com.profilizer.personalitytest.contracts.StartTestContract
import com.profilizer.personalitytest.di.components.DaggerStartTestComponent
import com.profilizer.personalitytest.di.modules.PersonalityTestModule
import com.profilizer.personalitytest.di.modules.StartTestModule
import com.profilizer.personalitytest.model.PersonalityTest
import com.profilizer.ui.TextWatcherAdapter
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_start_test.btn_start_test as btnStartTest
import kotlinx.android.synthetic.main.fragment_start_test.edit_text_user_name as editTestUserName
import kotlinx.android.synthetic.main.fragment_start_test.progress_bar as progressBar

class StartTestFragment : Fragment(), StartTestContract.View {

    @Inject
    lateinit var presenter: StartTestContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_start_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpInjection()
        setUpView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onAttach()
    }

    override fun onPause() {
        super.onPause()
        presenter.onDetach()
    }

    private fun setUpInjection() {
        DaggerStartTestComponent.builder()
                .applicationComponent(ProfilizerApplication.applicationComponent)
                .personalityTestModule(PersonalityTestModule())
                .startTestModule(StartTestModule(this))
                .build()
                .inject(this)
    }

    private fun setUpView() {
        btnStartTest.setOnClickListener {
            presenter.createPersonalityTest(editTestUserName.text.toString())
        }
        editTestUserName.addTextChangedListener(object: TextWatcherAdapter() {
            override fun afterTextChanged(edit: Editable?) {
                btnStartTest.isEnabled = edit.toString().isNotBlank()
            }
        })
    }

    override fun onCreateFinish(personalityTest: PersonalityTest) {
        progressBar.hide()
        val activity = activity as PersonalityTestActivity
        activity.loadQuestionFragment()
    }

    override fun showErrorMessage() {
        progressBar.hide()
        Toast.makeText(context, R.string.load_error_message, Toast.LENGTH_LONG).show()
    }

    override fun validateFields() {
        if (editTestUserName.text.isBlank()) {
            throw ValidationException(context?.getString(R.string.user_name_required))
        }
    }

    override fun startCreating() {
        progressBar.visible()
    }
}