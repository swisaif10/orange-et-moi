package com.orange.ma.entreprise.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.models.listmsisdn.Msisdn;

import java.util.ArrayList;

public class ProductSearchAdapter extends ArrayAdapter<Msisdn> {
    private ArrayList<Msisdn> items;
    private ArrayList<Msisdn> itemsAll;
    private ArrayList<Msisdn> suggestions;
    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            String str = ((Msisdn) (resultValue)).getLine();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Msisdn product : itemsAll) {
                    if (product.getLine().toLowerCase()
                            .contains(constraint.toString())) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<Msisdn> filteredList = (ArrayList<Msisdn>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Msisdn c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
    private int viewResourceId;

    @SuppressWarnings("unchecked")
    public ProductSearchAdapter(Context context, int viewResourceId,
                                ArrayList<Msisdn> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<Msisdn>) items.clone();
        this.suggestions = new ArrayList<Msisdn>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.number_item, null);
        }
        Msisdn product = items.get(position);
        if (product != null) {
            TextView productLabel = (TextView) v.findViewById(R.id.num);
            if (productLabel != null) {
                productLabel.setText(product.getLine());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

}
