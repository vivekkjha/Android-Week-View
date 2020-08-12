package com.alamkanak.weekview.sample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.sample.data.model.Event;
import com.alamkanak.weekview.sample.util.ToolbarUtils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import kotlin.Unit;

import static android.widget.Toast.LENGTH_SHORT;
import static java.time.format.FormatStyle.LONG;
import static java.time.format.FormatStyle.SHORT;

public class LegacyActivity extends AppCompatActivity {

    private EventsFetcher eventsFetcher;
    WeekViewAdapter weekViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        WeekView weekView = findViewById(R.id.weekView);
        ToolbarUtils.setupWithWeekView(toolbar, weekView);

        weekViewAdapter = new WeekViewAdapter(this::onLoadMore);
        eventsFetcher = new EventsFetcher(this);

        weekView.setAdapter(weekViewAdapter);
    }

    private void onLoadMore(@NotNull LocalDate startDate, @NotNull LocalDate endDate) {
        eventsFetcher.fetch(startDate, endDate, weekViewDisplayables -> {
            weekViewAdapter.submit(weekViewDisplayables);
            return Unit.INSTANCE;
        });
    }

    interface OnLoadMoreNotifier {
        void onLoadMore(LocalDate startDate, LocalDate endDate);
    }

    private static class WeekViewAdapter extends WeekView.PagingAdapter<Event> {

        @NonNull
        private OnLoadMoreNotifier notifier;

        public WeekViewAdapter(@NonNull OnLoadMoreNotifier notifier) {
            super();
            this.notifier = notifier;
        }

        @Override
        public void onEventClick(Event data) {
            Toast.makeText(getContext(), "Clicked " + data.getTitle(), LENGTH_SHORT).show();
        }

        @Override
        public void onEmptyViewClick(@NotNull LocalDateTime time) {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(LONG, SHORT);
            String formattedTime = formatter.format(time);
            Toast.makeText(getContext(), "Empty view clicked: " + formattedTime, LENGTH_SHORT).show();
        }

        @Override
        public void onEventLongClick(Event data) {
            Toast.makeText(getContext(), "Long-clicked event: " + data.getTitle(), LENGTH_SHORT).show();
        }

        @Override
        public void onEmptyViewLongClick(@NotNull LocalDateTime time) {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(LONG, SHORT);
            String formattedTime = formatter.format(time);
            Toast.makeText(getContext(), "Empty view long-clicked: " + formattedTime, LENGTH_SHORT).show();
        }

        @Override
        public void onLoadMore(@NotNull LocalDate startDate, @NotNull LocalDate endDate) {
            notifier.onLoadMore(startDate, endDate);
        }
    }
}
