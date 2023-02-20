package com.connor.fuckcolorapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.connor.fuckcolorapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlertDialogFragment(
    private val titleId: String,
    private val messageId: String,
    private val positiveId: String,
    private val showNegative: Boolean = false,
    private val positive: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = dialog()

    private fun dialog() = if (showNegative) {
        AlertDialog.Builder(requireContext())
            .setTitle(titleId)
            .setMessage(messageId)
            .setPositiveButton(positiveId) { _, _ ->
                positive()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
    } else {
        AlertDialog.Builder(requireContext())
            .setTitle(titleId)
            .setMessage(messageId)
            .setPositiveButton(positiveId) { _, _ ->
                positive()
            }
            .create()
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}