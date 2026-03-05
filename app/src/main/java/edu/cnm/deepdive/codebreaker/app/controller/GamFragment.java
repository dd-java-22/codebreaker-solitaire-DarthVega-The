package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentGamBinding;

@AndroidEntryPoint
public class GamFragment extends Fragment {

  private FragmentGamBinding binding;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentGamBinding.inflate(inflater, container, false);
    // TODO: 3/5/2026 Initialize layout-related components (e.g. listeners).
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // TODO: 3/5/2026 Initialize view-model-related components (e.g. observers).
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}
