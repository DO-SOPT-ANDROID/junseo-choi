package org.sopt.dosopttemplate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.sopt.dosopttemplate.databinding.FragmentDoandroidBinding

class DoAndroidFragment : Fragment() {
    private var _binding: FragmentDoandroidBinding? = null
    private val binding: FragmentDoandroidBinding
        get() = requireNotNull(_binding) { "바인딩 객체가 생성되지 않았다. 생성하고 불러라 임마!" }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDoandroidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 로직 구현 위치
    }

    override fun onDestroyView() {
        _binding = null // null 처리를 먼저 해줘야 안정성 증가
        super.onDestroyView()
    }
}