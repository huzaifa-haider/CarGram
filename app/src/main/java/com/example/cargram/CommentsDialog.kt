package com.example.cargram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth


class CommentsDialog : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.comments_drawer, container, false)
        var postid : String ?= null
        var commenterid : String ?= null
        val webapi = WebAPI(requireContext())
        var userid = FirebaseAuth.getInstance().currentUser?.uid
        arguments?.let {
            postid = it.getString("postid")
            commenterid = it.getString("commenter")
        }


        view.findViewById<ImageView>(R.id.commentupload).setOnClickListener{
            if(view.findViewById<EditText>(R.id.commentedittext).text != null){
                webapi.createComment(postid!!.toInt(), userid!!, view.findViewById<EditText>(R.id.commentedittext).text.toString(),
                    onSuccess = {

                    },
                    onError = {

                    })
            }
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as? BottomSheetDialog
            val bottomSheet = bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                BottomSheetBehavior.from(sheet).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                                dismiss()
                            }
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {
                            bottomSheet.alpha = 1 - slideOffset
                        }
                    })
                }
            }
        }
    }
}
