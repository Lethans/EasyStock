package com.example.easystock.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.easystock.BuildConfig;
import com.example.easystock.R;
import com.example.easystock.models.Product;
import com.example.easystock.utils.InputFilterMinMax;
import com.example.easystock.utils.viewPagerEfects.StringHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class FinalSellDetailsFragment extends Fragment {

    private EditText editFixDiscount, editPercentDiscount;
    private NotificableFinalOrderDetails mListener;
    private EditText mFixDiscount, mPercentDiscount;


    public static FinalSellDetailsFragment newInstance(List<Product> productList) {
        FinalSellDetailsFragment myFragment = new FinalSellDetailsFragment();

        Bundle args = new Bundle();
        args.putSerializable("products", (Serializable) productList);
        myFragment.setArguments(args);

        return myFragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finalsell_details, container, false);

        editFixDiscount = view.findViewById(R.id.fixDiscount);
        editPercentDiscount = view.findViewById(R.id.percentDiscount);
        editPercentDiscount.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});

        setOnlyOneDiscount();

        CheckBox chkPdf = view.findViewById(R.id.chkCreatePdf);
        CheckBox chkPdfSend = view.findViewById(R.id.chkSendPdfToPhone);
        CheckBox chkWhatsapp = view.findViewById(R.id.chkSendThruWhatsapp);

        EditText editCountry = view.findViewById(R.id.editCountryCode);
        EditText editLocation = view.findViewById(R.id.editLocationCode);
        EditText editPhone = view.findViewById(R.id.editPhone);

        Button btnCloseOrder = view.findViewById(R.id.closeOrderBtn);
        btnCloseOrder.setOnClickListener(v -> {

            if (chkPdf.isChecked()) {
                PdfDocument pmyPdfDocument = new PdfDocument();
                Paint paint = new Paint();
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
                PdfDocument.Page myPage = pmyPdfDocument.startPage(myPageInfo);
                Canvas canvas = myPage.getCanvas();

                paint.setTextSize(15.5f);
                paint.setColor(Color.rgb(0, 50, 250));

                canvas.drawText("Esto me vas a tener que pasar un formato de lo que se manda", 20, 20, paint);
                paint.setTextSize(8.5f);
                canvas.drawText("Un formato para Remito/Factura etc..", 20, 35, paint);

                pmyPdfDocument.finishPage(myPage);
                //fixme aca se tiene que cambiar el nombre del pdf
                File file = new File(getActivity().getExternalFilesDir("/"), "Nuevo" + ".pdf");

                try {
                    pmyPdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Error al generar el PDF", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                pmyPdfDocument.close();

                if (chkPdfSend.isChecked()) {
                    Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", file);

                    if (chkWhatsapp.isChecked()) {
                        if (!whatsappInstalledOrNot("com.whatsapp"))
                            return;
                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        String country = StringHelper.getEdittextValue(editCountry);
                        String location = StringHelper.getEdittextValue(editLocation);
                        String phone = StringHelper.getEdittextValue(editPhone);
                        //"5491164139535";
                        String toNumber = country + location + phone;
                        sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setPackage("com.whatsapp");
                        sendIntent.setType("application/pdf");
                        getContext().startActivity(sendIntent);
                    } else {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("application/pdf");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(share);
                    }

                }
            } else {
                Toast.makeText(getActivity(), "Tilda Generar PDF y Enviar Comprobante", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }


    private void getDiscount() {
        StringHelper.getEdittextValue(editFixDiscount);
        StringHelper.getEdittextValue(editPercentDiscount);
    }

    private void setOnlyOneDiscount() {
        editFixDiscount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                editPercentDiscount.setText("");
        });
        editPercentDiscount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                editFixDiscount.setText("");
        });
    }


    public interface NotificableFinalOrderDetails {
        void discountAvaiable(String discount);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NotificableFinalOrderDetails) context;
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = requireContext().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
            Toast.makeText(getActivity(), "Whatsapp no instalado", Toast.LENGTH_SHORT).show();
        }
        return app_installed;
    }

}
