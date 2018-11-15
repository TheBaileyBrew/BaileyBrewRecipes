package com.thebaileybrew.baileybrewrecipes.utils.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thebaileybrew.baileybrewrecipes.R;
import com.thebaileybrew.baileybrewrecipes.models.Step;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SliderViewHolder extends RecyclerView.Adapter<SliderViewHolder.ViewHolder> {
    private static final String TAG = SliderViewHolder.class.getSimpleName();

    private final LayoutInflater layoutInflater;
    private List<Step> data = new ArrayList<>();

    private SliderClickHandler clickHandler;

    public interface SliderClickHandler {
        void onClick(View view, Step step);
    }

    public SliderViewHolder(Context context, List<Step> allSteps, SliderClickHandler clickHandler) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = allSteps;
        this.clickHandler = clickHandler;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.step_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Step currentStep = data.get(position);
        holder.stepView.setText(String.valueOf(currentStep.getStepId()));
    }

    public void setSteps(List<Step> steps) {
        this.data = steps;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView stepView;

        private ViewHolder(View view) {
            super(view);
            stepView = view.findViewById(R.id.step_id);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step currentStep = data.get(getAdapterPosition());
            clickHandler.onClick(v, currentStep);
        }
    }
}
