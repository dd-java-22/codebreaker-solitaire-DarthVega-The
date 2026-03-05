package edu.cnm.deepdive.codebreaker.app.controller;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.api.model.Game;
import edu.cnm.deepdive.codebreaker.api.model.Guess;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentGamBinding;
import edu.cnm.deepdive.codebreaker.app.databinding.ItemGuessBinding;
import edu.cnm.deepdive.codebreaker.app.util.SymbolMap;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AndroidEntryPoint
public class GamFragment extends Fragment {

  @Inject
  SymbolMap symbolMap;

  private static final Map<Character, Integer> COLOR_MAP = Map.of(
      'R', Color.RED,
      'O', Color.parseColor("#FF7F00"),
      'Y', Color.YELLOW,
      'G', Color.GREEN,
      'B', Color.BLUE,
      'I', Color.parseColor("#4B0082"),
      'V', Color.parseColor("#9400D3")
  );
  private static final String POOL = "ROYGBIV";
  private static final int LENGTH = 4;

  private FragmentGamBinding binding;
  private GameViewModel viewModel;
  private List<Character> currentGuessChars = new ArrayList<>();
  private List<View> currentGuessViews;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentGamBinding.inflate(inflater, container, false);
    currentGuessViews = List.of(
        binding.currentColor1,
        binding.currentColor2,
        binding.currentColor3,
        binding.currentColor4
    );
    setupPalette();
    binding.send.setOnClickListener((v) -> submitGuess());
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel
        .getGame()
        .observe(getViewLifecycleOwner(), this::updateGame);
    // Start a game if none is active.
    if (viewModel.getGame().getValue() == null) {
      viewModel.startGame(POOL, LENGTH);
    }
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void setupPalette() {
    binding.red.setOnClickListener((v) -> addColor('R'));
    binding.orange.setOnClickListener((v) -> addColor('O'));
    binding.yellow.setOnClickListener((v) -> addColor('Y'));
    binding.green.setOnClickListener((v) -> addColor('G'));
    binding.blue.setOnClickListener((v) -> addColor('B'));
    binding.indigo.setOnClickListener((v) -> addColor('I'));
    binding.violet.setOnClickListener((v) -> addColor('V'));
  }

  private void addColor(char colorCode) {
    if (currentGuessChars.size() < LENGTH) {
      currentGuessChars.add(colorCode);
      updateCurrentGuessDisplay();
    }
  }

  private void updateCurrentGuessDisplay() {
    for (int i = 0; i < LENGTH; i++) {
      View view = currentGuessViews.get(i);
      if (i < currentGuessChars.size()) {
        view.setBackgroundTintList(ColorStateList.valueOf(COLOR_MAP.get(currentGuessChars.get(i))));
      } else {
        view.setBackgroundTintList(null);
      }
    }
    binding.send.setEnabled(currentGuessChars.size() == LENGTH);
  }

  private void submitGuess() {
    String guessText = currentGuessChars.stream()
        .map(String::valueOf)
        .collect(Collectors.joining());
    viewModel.submitGuess(guessText);
    currentGuessChars.clear();
    updateCurrentGuessDisplay();
  }

  private void updateGame(Game game) {
    if (game != null) {
      List<Guess> guesses = new ArrayList<>(game.getGuesses());
      Collections.reverse(guesses); // Latest on top? Wait, description said: 
      // "most recent guess being on the bottom while the latest guess is on the top of the guess screen"
      // This usually means Chronological order, with new ones appended at the end.
      // "previous guesses will be displayed above the current guess reaching to the top of the screen"
      // "most recent guess being on the bottom"
      // If most recent is on bottom, and it grows UP, it's chronological order.
      binding.guesses.setAdapter(new GuessAdapter(game.getGuesses()));
      binding.guesses.scrollToPosition(game.getGuesses().size() - 1);
    }
  }

  private static class GuessAdapter extends RecyclerView.Adapter<GuessHolder> {

    private final List<Guess> guesses;

    private GuessAdapter(List<Guess> guesses) {
      this.guesses = guesses;
    }

    @NonNull
    @Override
    public GuessHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new GuessHolder(ItemGuessBinding.inflate(
          LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GuessHolder holder, int position) {
      holder.bind(guesses.get(position));
    }

    @Override
    public int getItemCount() {
      return guesses.size();
    }

  }

  private static class GuessHolder extends RecyclerView.ViewHolder {

    private final ItemGuessBinding binding;

    private GuessHolder(ItemGuessBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(Guess guess) {
      String text = guess.getText();
      List<View> colorViews = List.of(binding.color1, binding.color2, binding.color3, binding.color4);
      for (int i = 0; i < text.length() && i < colorViews.size(); i++) {
        char c = text.charAt(i);
        colorViews.get(i).setBackgroundTintList(ColorStateList.valueOf(COLOR_MAP.get(c)));
      }
      binding.matches.setText(binding.getRoot().getContext().getString(
          R.string.exact_near_format, guess.getExactMatches(), guess.getNearMatches()));
    }

  }

}
