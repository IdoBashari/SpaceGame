package com.example.spacegamefinal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.spacegamefinal.managers.ScoreManager;
import com.example.spacegamefinal.models.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreListFragment extends Fragment {
    private ListView scoreListView;
    private ScoreManager scoreManager;
    private OnScoreSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_list, container, false);
        scoreListView = view.findViewById(R.id.score_list_view);
        scoreManager = ScoreManager.getInstance(getContext());
        updateScoreList();
        return view;
    }

    private void updateScoreList() {
        List<Score> highScores = scoreManager.getHighScores();
        ArrayAdapter<Score> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, highScores);
        scoreListView.setAdapter(adapter);
        scoreListView.setOnItemClickListener((parent, view, position, id) -> {
            if (listener != null) {
                listener.onScoreSelected(highScores.get(position));
            }
        });
    }

    public void setOnScoreSelectedListener(OnScoreSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnScoreSelectedListener {
        void onScoreSelected(Score score);
    }
}
