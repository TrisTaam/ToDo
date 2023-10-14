package com.tristaam.todo.ui.dialog.selectPriority

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tristaam.todo.databinding.FragmentSelectPriorityDialogBinding
import com.tristaam.todo.model.Priority

class SelectPriorityDialogFragment(private val listener: ISelectPriorityListener) :
    BottomSheetDialogFragment() {
    private var _binding: FragmentSelectPriorityDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPriorityDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            clUrgent.setOnClickListener { onSelect(Priority.URGENT) }
            clHigh.setOnClickListener { onSelect(Priority.HIGH) }
            clMedium.setOnClickListener { onSelect(Priority.MEDIUM) }
            clLow.setOnClickListener { onSelect(Priority.LOW) }
        }
    }

    private fun onSelect(priority: Priority) {
        listener.onSelectPriority(priority)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}