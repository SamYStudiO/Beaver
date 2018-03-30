package net.samystudio.beaver.ui.main.home

import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import net.samystudio.beaver.R
import net.samystudio.beaver.data.model.Home
import net.samystudio.beaver.ext.getGenericErrorDialog
import net.samystudio.beaver.ui.base.controller.BaseDataFetchController

class HomeController : BaseDataFetchController<HomeControllerViewModel, Home>()
{
    override val layoutViewRes: Int = R.layout.fragment_home
    override val viewModelClass: Class<HomeControllerViewModel> =
        HomeControllerViewModel::class.java
    override var title: String? = "Home"

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
        getGenericErrorDialog(activity!!).show(router)
    }

    override fun dataFetchTerminate()
    {
        // TODO hide loader
    }

    @OnClick(R.id.invalidate)
    fun onClickInvalidate()
    {

        /*AlertDialog.Builder(activity!!)
        .title("title")
        .items(R.array.api_urls)
        .positiveButton("ok")
        .create(this, 11)
        .show(router)*/

        router.pushController(RouterTransaction.with(this).pushChangeHandler(
            HorizontalChangeHandler()))
        //getGenericErrorDialog(activity!!).show(router)

        //AuthenticatorController().show(router)

        //viewModel.invalidateToken()
    }
}