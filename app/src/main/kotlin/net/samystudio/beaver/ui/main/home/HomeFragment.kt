package net.samystudio.beaver.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import net.samystudio.beaver.R
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.databinding.FragmentHomeBinding
import net.samystudio.beaver.ext.getGenericErrorDialog
import net.samystudio.beaver.ext.getMethodTag
import net.samystudio.beaver.ext.showIfNonExistent
import net.samystudio.beaver.ui.base.fragment.BaseDataFetchFragment
import net.samystudio.beaver.ui.common.dialog.AlertDialogListener

@AndroidEntryPoint
class HomeFragment : BaseDataFetchFragment<FragmentHomeBinding, HomeFragmentViewModel, Home>(),
    AlertDialogListener {
    override val binding: FragmentHomeBinding by viewBinding { inflater, container ->
        FragmentHomeBinding.inflate(inflater, container, false)
    }
    override val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.invalidate.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_home_to_userProfile))
    }

    override fun dataFetchStart() {
        // TODO Show loader.
    }

    override fun dataFetchSuccess(data: Home) {
        binding.textView.text = data.content
    }

    override fun dataFetchError(throwable: Throwable) {
        getGenericErrorDialog().showIfNonExistent(
            parentFragmentManager,
            getMethodTag()
        )
    }

    override fun dataFetchTerminate() {
        // TODO Hide loader.
    }
}