package com.orange.orangeetmoipro.views.authentication.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.models.cgu.CGUData;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.AuthenticationVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CGUFragment extends Fragment {


    @BindView(R.id.description)
    WebView webview;
    private AuthenticationVM authenticationVM;
    private Connectivity connectivity;

    public CGUFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationVM = ViewModelProviders.of(this).get(AuthenticationVM.class);
        connectivity = new Connectivity(getContext(), this);

        authenticationVM.getCguMutableLiveData().observe(this, this::handleCGULoginResponse);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cgu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCGU();
    }

    @OnClick({R.id.accept_btn, R.id.refuse_btn})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.accept_btn:
                intent.putExtra("cgu", true);
                break;
            case R.id.refuse_btn:
                intent.putExtra("cgu", false);
                break;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void init(String data) {
        webview.setOnLongClickListener(v -> true);
        webview.setLongClickable(false);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        String body = "<div style=\\\"font-family: Arial,Helvetica,sans-serif; margin:10px; /*text-align: justify;*/  font-size: 14px;\\\">\\r\\n<p><span style=\\\"color:#ff6600\\\"><strong>CONDITIONS GÉNÉRALES DE MEDI TELECOM RECHARGE ORANGE VIA QR CODE\\r\\n</strong></span></p>\\r\\n<p> </p>\\r\\n<p>LABEL’VIE SA, société anonyme de droit marocain, au capital social de 283.896.200 dirhams, immatriculée au Registre de Commerce de Rabat sous le numéro 27433, dont le siège social est sis à Rabat-Soussi, Km 3.5, Angle Rue Rif et Route de Zaers, et HYPERMARCHE LV, société anonyme simplifiée, au capital de 120.000.000 Dirhams, immatriculée au Registre de Commerce du Rabat sous le numéro 78457, dont le siège social est situé à Rabat-Souissi, Angle Rues Rif et Route de Zaërs, km 3,5, (ci-après dénommées ensemble « Groupe Carrefour ») ont mis en place un programme de récompense de leurs clients à l’achat de produits de sociétés participantes audit programme (ci-après désigné par le « Programme CADI+ »).\\r\\n\\r\\nLe Programme CADI+ est proposé par le Groupe Carrefour, sous sa responsabilité exclusive, dans les enseignes du Groupe Carrefour.\\r\\n\\r\\nDans le cadre du Programme CADI+, le Groupe Carrefour offre notamment à ses Clients, sous sa responsabilité exclusive, des recharges téléphoniques de MEDI TELECOM SA, société anonyme de droit marocain au capital social de 2.373.168.700 dirhams, dont le siège social est sis Lotissement La Colline, Immeuble les Quatre Temps, Sidi Maârouf, Casablanca, Maroc, immatriculée au Registre du Commerce de Casablanca sous le numéro 97815, Identification Fiscale n° 108 6826 (ci-après dénommée « MEDI TELECOM »), donnant lieu à un crédit de communications et ce, par le biais de QR Code imprimé sur le ticket de caisse remis au Client par le Groupe Carrefour.\\r\\n\\r\\nLes présentes conditions générales ont pour objet de définir les conditions dans lesquelles les Clients pourront scanner le QR Code au sein du site internet www.orange .ma ou l’application mobile « Orange&Moi » de MEDI TELECOM pour bénéficier des recharges téléphoniques de MEDI TELECOM (ci-après « Conditions Générales »). \\r\\n</p>\\r\\n<p> </p>\\r\\n<p>1.\\tDEFINITIONS\\r\\n\\r\\n« Client(s) » désigne les clients finaux du Groupe Carrefour ayant effectué des achats au sein des magasins du Groupe Carrefour.\\r\\n\\r\\n« Utilisateur » désigne les Clients s’étant connecté ou inscrit sur le site internet « orange.ma » ou sur l’application mobile « Orange&Moi » et ayant effectué la procédure décrite à l’article 2 ci-dessous.\\r\\n\\r\\n« Qr Code » (Quick Response Code), désigne le tag imprimé sur le ticket de caisse remis au Client par le Groupe Carrefour, lequel est lisible par les téléphones portables et tablettes.\\r\\n\\r\\n« Recharge(s) Orange » désigne les recharges téléphoniques de MEDI TELECOM donnant lieu à un crédit de communications valable 6 (six) mois à partir de la date de la recharge effectuée, lesquelles sont offertes gratuitement par le Groupe Carrefour à ses Clients.\\r\\n \\r\\n2.\\tCONDITIONS DE BENEFICE DE LA RECHARGE ORANGE\\r\\n\\r\\n2.1 Pour bénéficier de la Recharge Orange, le Client doit : \\r\\n\\r\\n-\\tse rendre sur la page Internet suivante : « orange.ma » ou sur l’application mobile « Orange&Moi » ;\\r\\n-\\tune fois que le Client s’est rendu sur la page internet ou l’application susmentionnées, il doit se connecter à l’espace client Orange en cliquant sur le lien prévu à cet effet au sein de la page internet ou l’application mobile. Le Client se connecte à l’espace client avec son login et son mot de passe personnel qui lui a été attribué par Orange lors de son inscription. Si le Client n’est pas déjà inscrit à l’espace client, celui-ci doit s’inscrire en respectant les procédures mise en place par MEDI TELECOM ;\\r\\n-\\tlorsque que le Client s’est identifié et a accédé à son espace client Orange, il doit cliquer sur la bannière relative au Programme CADI+ et renseigner son genre, sa situation familiale et sa tranche d’âge ;\\r\\n-\\tensuite, le Client est invité à scanner le QR Code figurant sur son ticket de caisse. Pour ce faire, le Client devra autoriser l’application à accéder à son appareil photo mobile. En cas d’utilisation d’un terminal ou appareil ne disposant pas d’un appareil photo ou en cas d’illisibilité du QR Code, le Client pourra renseigner le code facture figurant sur le ticket de caisse ;\\r\\n-\\tune fois le QR Code scanné, la Recharge Orange est automatiquement ajoutée à son solde de communications. Pour ce faire, le Client devra disposer d’une connexion internet et d’un terminal compatible.\\r\\n\\r\\n3.2 Le Client doit disposer d’une carte SIM Orange et être client de MEDI TELECOM pour pouvoir bénéficier de la Recharge Orange. \\r\\n\\r\\n3.3 Le montant de la Recharge Orange est déterminé par le Groupe Carrefour exclusivement. MEDI TELECOM ne peut être tenue pour responsable du montant de la Recharge Orange crédité.\\r\\n\\r\\n3.4 MEDI TELECOM décline toute responsabilité quant à l’utilisation qui peut être faite des Recharges Orange. MEDI TELECOM ne peut être tenue pour responsable de toute utilisation frauduleuse des Recharges Orange. \\r\\n\\r\\n3.5 MEDI TELECOM n’intervient pas dans le processus d’attribution des Recharges Orange. MEDI TELECOM n’est pas responsable en cas de réclamations relatives à l’attribution des Recharges Orange. \\r\\n\\r\\n3.\\tDONNES A CARACTERE PERSONNEL\\r\\n\\r\\nLes Utilisateurs reconnaissent et acceptent que les données collectées, dans le cadre des présentes, feront l'objet d'un traitement sous forme de fichiers papiers et/ou informatiques.\\r\\nElles sont utilisées par MEDI TELECOM, ses sociétés mères ou filiales, ou ses prestataires, qu’ils soient situés au Maroc ou à l’étranger, pour la gestion des comptes des clients.\\r\\nMEDI TELECOM peut utiliser les données personnelles des Utilisateurs pour toute opération de marketing direct concernant les produits et services de MEDI TELECOM via courrier électronique et SMS.\\r\\nSous réserve du consentement exprès des Utilisateurs, MEDI TELECOM peut utiliser les données personnelles des Utilisateurs pour toute opération de marketing direct via téléphone, courrier postal etc., réalisée par MEDI TELECOM.\\r\\nMEDI TELECOM pourra communiquer lesdites informations à des tiers aux fins de traitement, notamment à des cabinets d'étude de marché et instituts de sondage et ce, exclusivement à des fins d'étude et d'analyse.\\r\\nConformément à la loi n° 09-08 relative à « la protection des personnes physiques à l’égard du traitement des données à caractère personnel », les Utilisateurs disposent à tout moment d’un droit individuel d’accès ainsi que d'un droit d'information complémentaire, de rectification des données le concernant et, le cas échéant, d'opposition au traitement de leurs données ou à leur transmission par MEDI TELECOM à des tiers.\\r\\nLes Utilisateurs peuvent exercer leurs droits en envoyant un courrier, mentionnant leur : nom, prénom et numéro d'appel, et en y joignant une copie de leur pièce d'identité, à l’adresse de MEDI TELECOM : Lotissement La Colline II, Immeuble les Quatre Temps, Sidi Maârouf, Casablanca, Maroc.\\r\\n\\r\\n4.\\tRENSEIGNEMENT & RECLAMATION \\r\\n\\r\\nTout renseignement ou réclamation relatifs aux Recharges Orange peuvent être effectués :\\r\\n\\r\\n-\\ten appelant le Service client Orange au 121 à partir d’une ligne Orange et au 0663121121 à partir d’une autre ligne téléphonique ;\\r\\n-\\ten adressant un courrier à MEDI TELECOM SA Lotissement La Colline II Immeuble Quatre Temps, Sidi Maârouf, Casablanca, Maroc.\\r\\n\\r\\nToute contestation relative à la Recharge Orange doit être faite par l’Utilisateur dans un délai maximum d’un (1) mois à compter de la date d'émission du ticket de caisse concerné. Au-delà de ce délai, aucune contestation de cet ordre ne pourra être prise en compte. \\r\\n\\r\\n5.\\t APPLICABILITE ET MODIFICATION DES CONDITIONS GENERALES\\r\\n\\r\\nMEDI TELECOM se réserve à tout moment le droit de modifier les présentes Conditions Générales.\\r\\n\\r\\nLes présentes Conditions Générales pourront être modifiées par MEDI TELECOM notamment en cas de :\\r\\n\\r\\n-\\taméliorations, ajouts, remplacement ou suppression des modalités objet des présentes telles que définies dans les présentes ;\\r\\n-\\tmodifications imposées par les dispositions légales ou règlementaires.\\r\\n\\r\\nEn tout état de cause, l’Utilisateur a l’obligation de prendre connaissance des Conditions Générales à jour à date, disponibles sur Orange & Moi. \\r\\n\\r\\n6.\\tSUSPENSION, INTERRUPTION & EXCLUSION\\r\\n\\r\\nMEDI TELECOM se réserve le droit de suspendre ou d’interrompre, à tout moment, l’opération objet des présentes notamment dans les cas suivants :\\r\\n\\r\\n-\\telle reçoit une demande dans ce sens par les autorités marocaines compétentes ;\\r\\n-\\tl’Utilisateur commet un abus d’usage ;\\r\\n-\\ten cas de rupture du partenariat conclu entre MEDI TELECOM et le Groupe Carrefour ;\\r\\n-\\tpour des raisons propres à MEDI TELECOM ;\\r\\n-\\tpour des raisons de Force Majeure.\\r\\n\\r\\n1.\\tACCEPTATION DES CONDITIONS GENERALES\\r\\n\\r\\nLes présentes Conditions Générales sont réputées avoir été acceptées par les Utilisateurs sans aucune réserve. \\r\\n\\r\\n2.\\tLEGISLATION APPLICABLE\\r\\n\\r\\nLes présentes Conditions Générales sont soumises à la loi marocaine.\\r\\n\\r\\nEn cas de désaccord persistant sur l'application ou l'interprétation des présentes conditions générales, et à défaut d'accord amiable, tout litige sera soumis au tribunal compétent.\\r\\n\\r\\nPour MEDI TELECOM\\r\\n\\r\\nYves GAUTHIER\\r\\nDirecteur Général\\r\\n\\r\\n\\r\\n</p>\"";
        webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

    }

    private void getCGU() {
        if (connectivity.isConnected())
            authenticationVM.getCGU("fr");
    }

    private void handleCGULoginResponse(CGUData cguData) {
        if (cguData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error), "");
        } else {
            int code = cguData.getHeader().getCode();
            if (code == 200) {
                init(cguData.getCGUResponse().getCGU().getDescription());
            } else
                Utilities.showErrorPopup(getContext(), cguData.getHeader().getMessage(), "");
        }
    }
}