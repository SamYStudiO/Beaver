package net.samystudio.beaver.ui.main.userProfile

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.fragment_user_profile.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener

class UserProfileFragment : BaseViewModelFragment<UserProfileFragmentViewModel>(),
    AlertDialogListener {
    override val viewModel by viewModels<UserProfileFragmentViewModel>()
    override val layoutViewRes = R.layout.fragment_user_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addUserFlow(disconnect.clicks().map { UserProfileUserFlow.Disconnect })
    }
}