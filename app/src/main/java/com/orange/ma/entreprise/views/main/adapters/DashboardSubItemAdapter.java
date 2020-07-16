package com.orange.ma.entreprise.views.main.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.listeners.OnDashboardItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.main.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardSubItemAdapter extends RecyclerView.Adapter<DashboardSubItemAdapter.ViewHolder> {


    private Context context;
    private ArrayList<CompoundElement> arrayList;
    private OnDashboardItemSelectedListener onDashboardItemSelectedListener;
    private int templateKey;
    int max = 1;

    public DashboardSubItemAdapter(Context context, ArrayList<CompoundElement> arrayList, OnDashboardItemSelectedListener onDashboardItemSelectedListener, int templateKey) {
        this.context = context;
        this.arrayList = arrayList;
        this.onDashboardItemSelectedListener = onDashboardItemSelectedListener;
        this.templateKey = templateKey;
        findMaxLength();
    }

    private void findMaxLength() {
        for (CompoundElement e:arrayList)
            if(max<e.getElements().get(0).getValue().length()) max = e.getElements().get(0).getValue().length();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_string_subitem_layout, parent, false));
            case 1:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_button_subitem_layout, parent, false));
            default:
                return null;

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position).getElements().get(0).getType().equalsIgnoreCase("txt"))
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == arrayList.size() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            holder.itemView.setLayoutParams(params);

        }
        if (position % 2 == 0 && holder.separator != null && position != arrayList.size() - 1)
            holder.separator.setVisibility(View.VISIBLE);
        else if (holder.separator != null)
            holder.separator.setVisibility(View.GONE);


        if (arrayList.get(position).getElements().get(0).getType().equalsIgnoreCase("txt")) {

            if (templateKey == Template.TEMPLATE_PARCK) {
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.GONE);
                holder.layout3.setVisibility(View.VISIBLE);
                String text = arrayList.get(position).getElements().get(0).getValue();
                holder.value3pad.setText(Utilities.padLeft("0",max-text.length()));
                holder.value3.setText(text);
                holder.value3.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(0).getColor()));
                holder.name3.setText(arrayList.get(position).getElements().get(1).getValue());
                holder.name3.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            } else if (arrayList.get(position).getElements().get(0).getValue().length() > 3) {
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.VISIBLE);
                holder.value2.setText(arrayList.get(position).getElements().get(0).getValue());
                holder.value2.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(0).getColor()));
                holder.name2.setText(arrayList.get(position).getElements().get(1).getValue());
                holder.name2.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            } else {
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.GONE);
                holder.value1.setText(arrayList.get(position).getElements().get(0).getValue());
                holder.value1.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(0).getColor()));
                holder.name1.setText(arrayList.get(position).getElements().get(1).getValue());
                holder.name1.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            }

        } else {

            if (arrayList.size() > 2)
                setLayoutParams(holder,position);

            int icon = context.getResources().getIdentifier(arrayList.get(position).getElements().get(0).getValue(), "drawable", context.getPackageName());
            holder.icon.setImageResource(icon);
            ImageViewCompat.setImageTintList(holder.icon, ColorStateList.valueOf(Color.parseColor(arrayList.get(position).getElements().get(0).getColor())));
            holder.title.setText(arrayList.get(position).getElements().get(1).getValue());
            holder.title.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            holder.title.setOnTouchListener((v,e)->{
                switch (e.getAction()){
                    case MotionEvent.ACTION_DOWN:holder.title.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getHoverTxtColor()==null?"FE7900":arrayList.get(position).getElements().get(1).getHoverTxtColor()));break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:holder.title.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()==null?"#000000":arrayList.get(position).getElements().get(1).getColor()));break;
                }
                return true;
            });
            if (LocaleManager.getLanguagePref(context).equalsIgnoreCase("ar"))
                holder.arrow.setScaleX(-1);
        }
        holder.itemView.setOnClickListener(v -> onDashboardItemSelectedListener.onDashboardItemSelected(arrayList.get(position)));
    }

    private void setSliderParams(ViewHolder holder, int position) {
        if(templateKey==Template.TEMPLATE_LIST_SLIDER && position<2){
            holder.layout.setPadding(0,0,25,0);
        }
        else if(templateKey==Template.TEMPLATE_LIST_SLIDER && position>=2){
            holder.layout.setPadding(12,0,10,0);
        }
        else if(templateKey==Template.TEMPLATE_LIST_SLIDER)
            holder.layout.setPadding(0,0,5,0);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.value1)
        TextView value1;
        @Nullable
        @BindView(R.id.name1)
        TextView name1;
        @Nullable
        @BindView(R.id.layout_1)
        LinearLayout layout1;
        @Nullable
        @BindView(R.id.value2)
        TextView value2;
        @Nullable
        @BindView(R.id.name2)
        TextView name2;
        @Nullable
        @BindView(R.id.layout_2)
        LinearLayout layout2;
        @Nullable
        @BindView(R.id.icon)
        ImageView icon;
        @Nullable
        @BindView(R.id.title)
        TextView title;
        @Nullable
        @BindView(R.id.separator)
        View separator;
        @Nullable
        @BindView(R.id.arrow)
        ImageView arrow;
        @Nullable
        @BindView(R.id.value3)
        TextView value3;
        @Nullable
        @BindView(R.id.name3)
        TextView name3;
        @Nullable
        @BindView(R.id.layout_3)
        LinearLayout layout3;
        @Nullable
        @BindView(R.id.layout)
        RelativeLayout layout;
        @Nullable
        @BindView(R.id.pad)
        TextView value3pad;
        @Nullable
        @BindView(R.id.layout_item)
        ConstraintLayout layoutItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setLayoutParams(@NonNull ViewHolder holder, int position) {
        // 1.053 est le pourcentage de différence entre la largeur du slider et la largeur du device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int)((displayMetrics.widthPixels/1.053)/2)-(int)context.getResources().getDimension(R.dimen._10sdp),(int)context.getResources().getDimension(R.dimen._30sdp));

        if(position%4==0 || (position-1)%4==0)
            params.setMargins((int)context.getResources().getDimension(R.dimen._4sdp),(int)context.getResources().getDimension(R.dimen._minus1sdp),(int)context.getResources().getDimension(R.dimen._7sdp),(int)context.getResources().getDimension(R.dimen._8sdp));
        else if(((position-3)%4==0 || (position-2)%4==0) && arrayList.size()>6)
            params.setMargins((int)context.getResources().getDimension(R.dimen._10sdp),(int)context.getResources().getDimension(R.dimen._minus1sdp),(int)context.getResources().getDimension(R.dimen._2sdp),(int)context.getResources().getDimension(R.dimen._8sdp));
        else if (position>=2)
            params.setMargins((int)context.getResources().getDimension(R.dimen._6sdp),(int)context.getResources().getDimension(R.dimen._minus1sdp),(int)context.getResources().getDimension(R.dimen._6sdp),(int)context.getResources().getDimension(R.dimen._8sdp));
        holder.itemView.setLayoutParams(params);

//        new Handler(Looper.getMainLooper()).post(()->{
//            Log.d("TAGGWD", "setLayoutParams: "+holder.itemView.getMeasuredWidth());
//
//        });
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//
//        Resources r = context.getResources();
//        int px = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                10,
//                r.getDisplayMetrics()
//        );
//
//        int px2 = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                20,
//                r.getDisplayMetrics()
//        );
//
//        int px3 = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                5,
//                r.getDisplayMetrics()
//        );
//
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(0, 0);
//        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
//        switch (context.getResources().getDisplayMetrics().densityDpi) {
//            case 280:
//            case 420:
//            case 480:
//            case 560:
//                params.width = (int) (displayMetrics.widthPixels * 0.47);
//                params.setMargins(px, 0, 0, px);
//                break;
//            default:
//                params.width = (int) (displayMetrics.widthPixels * 0.44);
//                if (position == (arrayList.size() - 1)) {
//                    params.setMargins(0, 0, px, px);
//                } else {
//                    params.setMargins(0, 0, px2, px);
//                }
//                break;
//        }
//        holder.itemView.setLayoutParams(params);
    }
}