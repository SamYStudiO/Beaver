package net.samystudio.beaver.ui.main.home

import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import net.samystudio.beaver.R
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.ui.base.controller.BaseDataFetchController
import net.samystudio.beaver.ui.main.authenticator.AuthenticatorController
import timber.log.Timber

class HomeController : BaseDataFetchController<HomeControllerViewModel, Home>()
{
    override val layoutViewRes: Int = R.layout.fragment_home
    override val viewModelClass: Class<HomeControllerViewModel> =
        HomeControllerViewModel::class.java

    @BindView(R.id.text_view)
    lateinit var textView: TextView

    override fun dataFetchStart()
    {
        // TODO show loader
    }

    override fun dataFetchSuccess(data: Home)
    {
        textView.text = data.content
    }

    override fun dataFetchError(throwable: Throwable)
    {
        Timber.d("dataFetchError: ")
        // getGenericErrorDialog(context!!).showNow(fragmentManager, AlertDialog::class.java.name)
    }

    override fun dataFetchTerminate()
    {
        // TODO hide loader
    }

    @OnClick(R.id.invalidate)
    fun onClickInvalidate()
    {

        /*AlertDialog.Builder(context!!)
            .title("title")
            .items(R.array.api_urls)
            .positiveButton("ok")
            .create(this, 11)
            .showNow(fragmentManager,
                     AlertDialog::class.java.name)*/

        // viewModel.requestAuthenticator()
        //router.pushController(RouterTransaction.with(AuthenticatorController()))
        //getGenericErrorDialog(activity!!).show(router)

        AuthenticatorController().show(router)
        //  .showNow(supportFragmentManager, AuthenticatorFragment::class.java.name)

        //viewModel.invalidateToken()
    }
}