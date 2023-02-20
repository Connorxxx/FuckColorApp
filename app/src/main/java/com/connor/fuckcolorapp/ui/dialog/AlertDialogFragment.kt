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
    private val positive: () -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(titleId)
            .setMessage(messageId)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                positive()
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}