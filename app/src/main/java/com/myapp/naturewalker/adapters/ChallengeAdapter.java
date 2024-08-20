package com.myapp.naturewalker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.naturewalker.R;
import com.myapp.naturewalker.callbacks.GeneralCallback;
import com.myapp.naturewalker.objects.Challenge;
import com.myapp.naturewalker.objects.User;
import com.myapp.naturewalker.user_objects.UserChallenge;
import com.myapp.naturewalker.utils.DataManager;
import com.myapp.naturewalker.utils.LocalData;
import com.myapp.naturewalker.utils.Status;

import java.time.Duration;
import java.util.List;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder> {

    private List<Challenge> challenges;
    private Status challengesStatus;
    private GeneralCallback generalCallback;

    public ChallengeAdapter(List<Challenge> challenges, Status challengesStatus, GeneralCallback generalCallback) {
        this.challenges = challenges;
        this.challengesStatus = challengesStatus;
        this.generalCallback = generalCallback;
    }

    public static class ChallengeViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView reward;
        AppCompatButton accept;
        TextView number;

        public ChallengeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.challenge_title);
            description = itemView.findViewById(R.id.challenge_description);
            reward = itemView.findViewById(R.id.challenge_reward);
            accept = itemView.findViewById(R.id.challenge_accept);
            number = itemView.findViewById(R.id.challenge_number);
        }
    }

    @NonNull
    @Override
    public ChallengeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.challenge_item, parent, false);
        return new ChallengeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        holder.title.setText(challenge.getTitle());
        holder.description.setText(challenge.generateDescription());
        holder.reward.setText("Reward: " + challenge.getReward() + " coins");
        holder.number.setText(String.valueOf(Duration.ofMillis(challenge.getDuration()).toDays()));

        switch (challengesStatus) {
            case DONE:
                holder.accept.setVisibility(View.GONE);
                break;
            case ACTIVE:
                holder.accept.setVisibility(View.GONE);
                break;
            default:
                holder.accept.setVisibility(View.VISIBLE);
                break;
        }

        holder.accept.setOnClickListener(v -> {
            if (LocalData.activeChallenges.isEmpty()) {
                DataManager.addUserChallenge(new UserChallenge(challenge.getTitle(), Status.ACTIVE));
                generalCallback.success(null);
            }
            else
                generalCallback.failed(null);
        });
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }
}
