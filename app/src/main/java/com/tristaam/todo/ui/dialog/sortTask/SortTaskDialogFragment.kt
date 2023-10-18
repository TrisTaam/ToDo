package com.tristaam.todo.ui.dialog.sortTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tristaam.todo.databinding.FragmentSortTaskDialogBinding
import com.tristaam.todo.ui.board.BoardViewModel

class SortTaskDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSortTaskDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BoardViewModel by lazy {
        BoardViewModel(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSortTaskDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvSortByDueDateOldNew.setOnClickListener {
                dismiss()
            }
            tvSortByDueDateNewOld.setOnClickListener {
                dismiss()
            }
            tvSortByCreatedOldNew.setOnClickListener {
                dismiss()
            }
            tvSortByCreatedNewOld.setOnClickListener {
                dismiss()
            }
            tvSortByPriorityHighLow.setOnClickListener {
                dismiss()
            }
            tvSortByAlphabeticalAZ.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}