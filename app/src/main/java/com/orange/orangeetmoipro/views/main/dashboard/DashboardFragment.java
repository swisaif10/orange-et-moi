package com.orange.orangeetmoipro.views.main.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.dashboard.ItemButton;
import com.orange.orangeetmoipro.models.dashboard.ItemString;
import com.orange.orangeetmoipro.models.dashboard.Template;
import com.orange.orangeetmoipro.views.main.adapters.DashboardAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment {

    @BindView(R.id.dashboard_recycler)
    RecyclerView dashboardRecycler;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initArrays();
    }

    private void initArrays() {

        ArrayList<ItemString> arrayList1 = new ArrayList<ItemString>() {{
            add(new ItemString("02", "Factures à payer"));
            add(new ItemString("13 4599,70", "DHS"));
        }};

        ArrayList<ItemString> arrayList2 = new ArrayList<ItemString>() {{
            add(new ItemString("120", "Produits Mobiles"));
            add(new ItemString("15", "Produits Fixes"));
        }};

        ArrayList<ItemButton> arrayList3 = new ArrayList<ItemButton>() {{
            add(new ItemButton(R.drawable.groupe531, "Changement de N°"));
            add(new ItemButton(R.drawable.suspension, "Suspension"));
            add(new ItemButton(R.drawable.changement_de_sim, "Changement de SIM"));
            add(new ItemButton(R.drawable.e_recharge, "E-Recharche"));
        }};

        ArrayList<ItemButton> arrayList4 = new ArrayList<ItemButton>() {{
            add(new ItemButton(R.drawable.roaming, "Roaming"));
            add(new ItemButton(R.drawable.pass, "PAss"));
        }};

        ArrayList<ItemButton> arrayList5 = new ArrayList<ItemButton>() {{
            add(new ItemButton(R.drawable.tickets, "Tickets"));
            add(new ItemButton(R.drawable.contacts, "Contacts"));
        }};

        ArrayList<Template> arrayList = new ArrayList<Template>() {{
            add(new Template(R.drawable.mes_factures, R.drawable.mon_parc, "Mes Factures", "Mon Parc", "", "", "small1", "small1", null, null, arrayList1, arrayList2));
            add(new Template(R.drawable.mes_operations, -1, "Mes Opérations", null, "", "", "large", null, arrayList3, null, null, null));
            add(new Template(R.drawable.mon_roaming, R.drawable.mon_assistance, "Mon Roaming", "Mon Assistance", "", "", "small2", "small2", arrayList4, arrayList5, null, null));
            add(new Template(R.drawable.mes_operations, -1, "Mes Opérations", null, "", "", "large", null, arrayList3, null, null, null));
            add(new Template(R.drawable.mes_factures, R.drawable.mon_parc, "Mes Factures", "Mon Parc", "", "", "small1", "small1", null, null, arrayList1, arrayList2));
            add(new Template(R.drawable.mes_operations, -1, "Mes Opérations", null, "", "", "large", null, arrayList3, null, null, null));
            add(new Template(R.drawable.mon_roaming, R.drawable.mon_assistance, "Mon Roaming", "Mon Assistance", "", "", "small2", "small2", arrayList4, arrayList5, null, null));
        }};

        dashboardRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DashboardAdapter dashboardAdapter = new DashboardAdapter(getContext(), arrayList);
        dashboardRecycler.setAdapter(dashboardAdapter);


        LinearSnapHelper snapHelper = new LinearSnapHelper();
            /*@Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;


                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));


                int activePosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                return targetPosition;
            }
        };*/
        snapHelper.attachToRecyclerView(dashboardRecycler);


    }

}