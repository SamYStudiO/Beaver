package net.samystudio.beaver.ui.main.userProfile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.databinding.FragmentUserProfileBinding
import net.samystudio.beaver.ui.base.fragment.BaseViewModelFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener

@AndroidEntryPoint
class UserProfileFragment :
    BaseViewModelFragment<FragmentUserProfileBinding, UserProfileFragmentViewModel>(),
    AlertDialogListener {
    override val binding: FragmentUserProfileBinding by viewBinding { inflater, container ->
        FragmentUserProfileBinding.inflate(inflater, container, false)
    }
    override val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.addUserFlow(binding.disconnect.clicks().map { UserProfileUserFlow.Disconnect })
    }
}