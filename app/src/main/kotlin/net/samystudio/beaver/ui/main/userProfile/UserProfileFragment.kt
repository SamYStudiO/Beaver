package net.samystudio.beaver.ui.main.userProfile

import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.fragment_user_profile.*
import net.samystudio.beaver.R
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener

class UserProfileFragment : BaseViewModelFragment<UserProfileFragmentViewModel>(),
    AlertDialogListener {
    override val layoutViewRes: Int = R.layout.fragment_user_profile
    override val viewModelClass: Class<UserProfileFragmentViewModel> =
        UserProfileFragmentViewModel::class.java
    override var title: String? = "UserProfile"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addUserFlow(disconnect.clicks().map { UserProfileUserFlow.Disconnect })
    }
}