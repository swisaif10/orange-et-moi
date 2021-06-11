package com.orange.ma.entreprise.views.consult_line.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.models.consult.Line;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SoldeAdapter extends RecyclerView.Adapter<SoldeAdapter.ForfaitHolder> {

    private Context mContext;
    private List<Line> offreList;

    public SoldeAdapter(Context mContext, List<Line> offreList) {
        this.mContext = mContext;
        this.offreList = offreList;
    }

    @NonNull
    @Override
    public ForfaitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.solde_item, parent, false);
        return new ForfaitHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForfaitHolder holder, int position) {
        Line currentoffre = offreList.get(position);

        holder.name.setText(Html.fromHtml(currentoffre.getMessage()));

    }

    @Override
    public int getItemCount() {
        return offreList.size();
    }

    public class ForfaitHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        public ForfaitHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }


    }


}
