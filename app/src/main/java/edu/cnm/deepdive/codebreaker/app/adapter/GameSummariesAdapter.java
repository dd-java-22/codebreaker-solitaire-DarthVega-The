package edu.cnm.deepdive.codebreaker.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import dagger.hilt.android.qualifiers.ActivityContext;
import edu.cnm.deepdive.codebreaker.app.databinding.ItemGameSummaryBinding;
import edu.cnm.deepdive.codebreaker.app.model.GameSummary;
import jakarta.inject.Inject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class GameSummariesAdapter extends RecyclerView.Adapter<ViewHolder> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter
      .ofLocalizedDateTime(FormatStyle.SHORT)
      .withZone(ZoneId.systemDefault());

  private final LayoutInflater inflater;
  private final List<GameSummary> gameSummaries;

  @Inject
  public GameSummariesAdapter(@ActivityContext Context context) {
    inflater = LayoutInflater.from(context);
    gameSummaries = new ArrayList<>();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new SummaryHolder(ItemGameSummaryBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((SummaryHolder) holder).bind(position);
  }

  @Override
  public int getItemCount() {
    return gameSummaries.size();
  }

  public List<GameSummary> getGameSummaries() {
    return gameSummaries;
  }

  public void setGameSummaries(List<GameSummary> gameSummaries) {
    this.gameSummaries.clear();
    if (gameSummaries != null) {
      this.gameSummaries.addAll(gameSummaries);
    }
    notifyDataSetChanged();
  }

  private class SummaryHolder extends ViewHolder {

    private final ItemGameSummaryBinding binding;

    private SummaryHolder(@NonNull ItemGameSummaryBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      GameSummary summary = gameSummaries.get(position);
      Instant lastPlayed = summary.getLastPlayed();
      binding.lastPlayed.setText(
          (lastPlayed != null) ? FORMATTER.format(lastPlayed) : "");
      binding.poolSize.setText(String.valueOf(summary.getPoolSize()));
      binding.codeLength.setText(String.valueOf(summary.getCodeLength()));
      binding.guessCount.setText(String.valueOf(summary.getGuessCount()));
    }

  }

}
