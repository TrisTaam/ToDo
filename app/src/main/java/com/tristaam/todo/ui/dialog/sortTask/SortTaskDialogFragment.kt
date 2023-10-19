package com.tristaam.todo.ui.dialog.sortTask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tristaam.todo.databinding.FragmentSortTaskDialogBinding
import com.tristaam.todo.ui.board.BoardViewModel

class SortTaskDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSortTaskDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BoardViewModel by activityViewModels {
        BoardViewModel.BoardViewModelFactory(requireContext())
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
                viewModel.sortTaskBy(BoardViewModel.TaskSortBy.DUE_DATE_OLD_NEW)
                dismiss()
            }
            tvSortByDueDateNewOld.setOnClickListener {
                viewModel.sortTaskBy(BoardViewModel.TaskSortBy.DUE_DATE_NEW_OLD)
                dismiss()
            }
            tvSortByCreatedOldNew.setOnClickListener {
                viewModel.sortTaskBy(BoardViewModel.TaskSortBy.CREATED_OLD_NEW)
                dismiss()
            }
            tvSortByCreatedNewOld.setOnClickListener {
                viewModel.sortTaskBy(BoardViewModel.TaskSortBy.CREATED_NEW_OLD)
                dismiss()
            }
            tvSortByPriorityHighLow.setOnClickListener {
                viewModel.sortTaskBy(BoardViewModel.TaskSortBy.PRIORITY_HIGH_LOW)
                dismiss()
            }
            tvSortByAlphabeticalAZ.setOnClickListener {
                viewModel.sortTaskBy(BoardViewModel.TaskSortBy.ALPHABETICAL_A_Z)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}