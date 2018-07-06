package latproject.com.myfinance.views.dialogfragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.databinding.LayoutNotificationAlertBinding
import timber.log.Timber
import java.lang.IllegalArgumentException

class NotificationAlertDialog : DialogFragment() {
    private lateinit var binding: LayoutNotificationAlertBinding
    private var message: String = ""
    private var title: String = ""
    private var onOkayClickedListener: OnOkayClickedListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            onOkayClickedListener = requireNotNull(parentFragment as? OnOkayClickedListener
                    ?: context as? OnOkayClickedListener)
        } catch (t: IllegalArgumentException) {
            Timber.e("$context should implement OnOkayClickedListener")
            throw t
        }
    }

    inner class NotificationAlertHandler {
        fun onOkayClicked(view: View) {
            onOkayClickedListener?.onOkayClicked()
            dismissAllowingStateLoss()
        }

        fun onCancelClicked(view: View) {
            dismissAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val arguments = arguments

            title = arguments?.getString(Constants.EXTRA_DIALOG_TITLE) ?: ""
            message = arguments?.getString(Constants.EXTRA_DIALOG_MESSAGE) ?: ""
        } catch (t: Throwable) {
            Timber.e("[SELECT_WHEN] Error creating fragment: $t")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_notification_alert, container, false)
        binding = LayoutNotificationAlertBinding.bind(view)
        binding.handler = NotificationAlertHandler()
        binding.title.text = title
        binding.message.text = message
        return binding.root
    }

    companion object {

        private fun newInstance(message: String?,
                                title: String?): NotificationAlertDialog {
            val dialogFragment = NotificationAlertDialog()
            val arguments = Bundle()

            if (title != null) {
                arguments.putString(Constants.EXTRA_DIALOG_TITLE, title)
            }

            if (message != null) {
                arguments.putString(Constants.EXTRA_DIALOG_MESSAGE, message)
            }

            dialogFragment.arguments = arguments
            return dialogFragment
        }

        fun show(childFragmentManager: FragmentManager,
                 message: String?,
                 title: String?) {
            val dialogFragment = NotificationAlertDialog.newInstance(message, title)

            val fragmentTransaction = buildAddDialogFragmentFragmentTransaction(
                    childFragmentManager,
                    Constants.TAG_FRAGMENT_NOTIFICATION_DIALOG
            )

            dialogFragment.show(
                    fragmentTransaction,
                    Constants.TAG_FRAGMENT_NOTIFICATION_DIALOG
            )
        }

        private fun buildAddDialogFragmentFragmentTransaction(childFragmentManager: FragmentManager,
                                                              tag: String): FragmentTransaction {

            val fragmentTransaction = childFragmentManager.beginTransaction()
            val previous = childFragmentManager.findFragmentByTag(tag)

            if (previous != null) {
                fragmentTransaction.remove(previous)
            }

            return fragmentTransaction
        }
    }

    interface OnOkayClickedListener {
        fun onOkayClicked()
    }
}
