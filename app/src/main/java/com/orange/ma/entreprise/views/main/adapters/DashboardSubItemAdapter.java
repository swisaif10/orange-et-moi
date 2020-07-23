package com.orange.ma.entreprise.views.main.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnTemplateItemSelectedListener;
import com.orange.ma.entreprise.models.dashboard.CompoundElement;
import com.orange.ma.entreprise.models.dashboard.Template;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardSubItemAdapter extends RecyclerView.Adapter<DashboardSubItemAdapter.ViewHolder> {

    private Context context;
    private List<CompoundElement> arrayList;
    private OnTemplateItemSelectedListener onTemplateItemSelectedListener;
    private int templateKey;
    private PreferenceManager preferenceManager;
    private String lang;
    private int max = 1;

    public DashboardSubItemAdapter(Context context, List<CompoundElement> arrayList, OnTemplateItemSelectedListener onTemplateItemSelectedListener, int templateKey) {
        this.context = context;
        this.arrayList = arrayList;
        this.onTemplateItemSelectedListener = onTemplateItemSelectedListener;
        this.templateKey = templateKey;
        findMaxLength();
        preferenceManager = new PreferenceManager.Builder(context, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (templateKey) {
            case Template.TEMPLATE_BILLING:
            case Template.TEMPLATE_PARCK:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_string_subitem_layout, parent, false));
            case Template.TEMPLATE_SMALL_LIST:
            case Template.TEMPLATE_LIST_SLIDER:
            case Template.TEMPLATE_LIST:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dashboard_button_subitem_layout, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (position == arrayList.size() - 1) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            holder.itemView.setLayoutParams(params);

        }
        if ((position % 2 == 0 && holder.separator != null && position != arrayList.size() - 1)||(templateKey==Template.TEMPLATE_LIST&&position != arrayList.size() - 1))
            holder.separator.setVisibility(View.VISIBLE);
        else if (holder.separator != null)
            holder.separator.setVisibility(View.GONE);

int size = arrayList.get(position).getElements().size();
        if (size>0 && arrayList.get(position).getElements().get(0).getType().equalsIgnoreCase("txt")) {

            if (templateKey == Template.TEMPLATE_PARCK) {
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.GONE);
                holder.layout3.setVisibility(View.VISIBLE);
                if(size>0){
                    String text = arrayList.get(position).getElements().get(0).getValue();
                    holder.value3pad.setText(Utilities.padLeft("0", max - text.length()));

                    setTextsValues(holder.value3,text,arrayList.get(position).getElements().get(0).getColor(),"#FE7900");
                    if(size>1)
                        setTextsValues(holder.name3,arrayList.get(position).getElements().get(1).getValue(),arrayList.get(position).getElements().get(1).getColor(),"#000000");
                    else
                        setTextsValues(holder.name3,"-","#000000","#000000");
                }else{
                    setTextsValues(holder.value3,"-","#FE7900","#FE7900");
                    setTextsValues(holder.name3,"-","#000000","#000000");
                }
            } else if (templateKey==Template.TEMPLATE_LIST||templateKey == Template.TEMPLATE_LIST_SLIDER){//(arrayList.get(position).getElements().get(0).getValue().length() > 3) {
                holder.layout1.setVisibility(View.GONE);
                holder.layout2.setVisibility(View.VISIBLE);
                if(size>0){
                    setTextsValues(holder.value2,arrayList.get(position).getElements().get(0).getValue(),arrayList.get(position).getElements().get(0).getColor(),"#FE7900");
                    if(size>1)
                        setTextsValues(holder.name2,arrayList.get(position).getElements().get(1).getValue(),arrayList.get(position).getElements().get(1).getColor(),"#000000");
                    else
                        setTextsValues(holder.name2,"-","#000000","#000000");
                }else{
                    setTextsValues(holder.value2,"-","#FE7900","#FE7900");
                    setTextsValues(holder.name2,"-","#000000","#000000");
                }
            } else {
                holder.layout1.setVisibility(View.VISIBLE);
                holder.layout2.setVisibility(View.GONE);
                if(size>0){
                    setTextsValues(holder.value1,arrayList.get(position).getElements().get(0).getValue(),arrayList.get(position).getElements().get(0).getColor(),"#FE7900");
                    if(size>1)
                        setTextsValues(holder.name1,arrayList.get(position).getElements().get(1).getValue(),arrayList.get(position).getElements().get(1).getColor(),"#000000");
                    else
                        setTextsValues(holder.name1,"-","#000000","#000000");
                }else{
                    setTextsValues(holder.value1,"-","#FE7900","#FE7900");
                    setTextsValues(holder.name1,"-","#000000","#000000");
                }
            }

        } else if(size >0) {

            if (templateKey == Template.TEMPLATE_LIST_SLIDER)
                setLayoutParams(holder, position);

            int icon = context.getResources().getIdentifier(arrayList.get(position).getElements().get(0).getValue(), "drawable", context.getPackageName());
            holder.icon.setImageResource(icon);
            ImageViewCompat.setImageTintList(holder.icon, ColorStateList.valueOf(Color.parseColor(arrayList.get(position).getElements().get(0).getColor())));
            holder.title.setText(arrayList.get(position).getElements().get(1).getValue());
            holder.title.setTextColor(Color.parseColor(arrayList.get(position).getElements().get(1).getColor()));
            holder.arrow.setVisibility(arrayList.get(position).getActionType().equalsIgnoreCase("none")||arrayList.get(position).getActionType().trim().isEmpty()?View.INVISIBLE:View.VISIBLE);
            ColorStateList mStateDrawableBtn1 = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_pressed},
                    new int[]{android.R.attr.state_pressed},
            },
                    new int[]{
                            Color.parseColor(Utilities.isNullOrEmpty(arrayList.get(position).getElements().get(1).getColor())
                                    ? "#000000" : arrayList.get(position).getElements().get(1).getColor()),
                            Color.parseColor(Utilities.isNullOrEmpty(arrayList.get(position).getElements().get(1).getHoverValueColor())
                                    ? "#FE7900" : arrayList.get(position).getElements().get(1).getHoverValueColor())});
            holder.title.setTextColor(mStateDrawableBtn1);

            if (LocaleManager.getLanguagePref(context).equalsIgnoreCase("ar"))
                holder.arrow.setScaleX(-1);
        }

        holder.itemView.setOnClickListener(v -> onTemplateItemSelectedListener.onTemplateItemSelected(arrayList.get(position)));
    }

    private void setTextsValues(TextView view, String value, String color,String defaultColor) {

        view.setText(Utilities.isNullOrEmpty(value)?"-":value);
        view.setTextColor(Color.parseColor(Utilities.isNullOrEmpty(color)?defaultColor:color.startsWith("#")?color:defaultColor));
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
        displayMetrics = context.getResources().getDisplayMetrics();
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams((int) ((displayMetrics.widthPixels / 1.053) / 2) - (int) context.getResources().getDimension(R.dimen._10sdp), (int) context.getResources().getDimension(R.dimen._30sdp));
        lang = preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr");

        if(position%4==0 || (position-1)%4==0)
            params.setMargins(
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._7sdp:R.dimen._4sdp),
                    (int)context.getResources().getDimension(R.dimen._minus1sdp),
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._4sdp:R.dimen._7sdp),
                    (int)context.getResources().getDimension(R.dimen._8sdp));
        else if(((position-3)%4==0 || (position-2)%4==0) && arrayList.size()>6)
            params.setMargins(
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._2sdp:R.dimen._10sdp),
                    (int)context.getResources().getDimension(R.dimen._minus1sdp),
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._10sdp:R.dimen._2sdp),
                    (int)context.getResources().getDimension(R.dimen._8sdp));
        else if (position>=2)
            params.setMargins(
                    (int)context.getResources().getDimension(R.dimen._6sdp),
                    (int)context.getResources().getDimension(R.dimen._minus1sdp),
                    (int)context.getResources().getDimension(R.dimen._6sdp),
                    (int)context.getResources().getDimension(R.dimen._8sdp));
        if(position>1 && arrayList.size()<=6)
            params.setMargins(
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._2sdp:R.dimen._16sdp),
                    (int)context.getResources().getDimension(R.dimen._minus1sdp),
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._16sdp:R.dimen._2sdp),
                    (int)context.getResources().getDimension(R.dimen._8sdp));
        if(position<=1 && arrayList.size()<=6)
            params.setMargins(
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._5sdp:R.dimen._1sdp),
                    (int)context.getResources().getDimension(R.dimen._minus1sdp),
                    (int)context.getResources().getDimension(lang.equalsIgnoreCase("ar")?R.dimen._4sdp:R.dimen._3sdp),
                    (int)context.getResources().getDimension(R.dimen._8sdp));
        holder.itemView.setLayoutParams(params);

    }

    private void findMaxLength() {
        for (CompoundElement e : arrayList)
            if(e.getElements().size()>0)
                if (max < e.getElements().get(0).getValue().length())
                    max = e.getElements().get(0).getValue().length();
    }
}