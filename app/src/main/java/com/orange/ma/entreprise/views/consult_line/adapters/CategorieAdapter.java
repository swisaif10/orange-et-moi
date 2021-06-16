package com.orange.ma.entreprise.views.consult_line.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.models.consult.Categorie;
import com.orange.ma.entreprise.models.consult.Line;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.ForfaitHolder> {

    private Context mContext;
    private List<Categorie> categorieList;
    private SoldeAdapter amountAdapter;

    public CategorieAdapter(Context mContext, List<Categorie> categorieList) {
        this.mContext = mContext;
        this.categorieList = categorieList;
    }

    @NonNull
    @Override
    public ForfaitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.categorie_item, parent, false);
        return new ForfaitHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForfaitHolder holder, int position) {
        Categorie currentoffre = categorieList.get(position);

        holder.name.setText(Html.fromHtml(currentoffre.getName()));

        holder.rv.setLayoutManager(new LinearLayoutManager(mContext));
        holder.rv.setHasFixedSize(true);
        amountAdapter = new SoldeAdapter(mContext, currentoffre.getLine_balance());
        holder.rv.setAdapter(amountAdapter);

    }

    @Override
    public int getItemCount() {
        return categorieList.size();
    }

    public class ForfaitHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.rv)
        RecyclerView rv;

        public ForfaitHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }


    }


}
