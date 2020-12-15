package com.example.easystock.views.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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

        Button btnCloseOrder = view.findViewById(R.id.closeOrderBtn);
        btnCloseOrder.setOnClickListener(v -> {
            /** PRIMER INTENTO
             Intent sendIntent = new Intent(); sendIntent.setAction(Intent.ACTION_SEND);
             sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
             sendIntent.setType("text/plain");
             sendIntent.setPackage("com.whatsapp");
             startActivity(sendIntent);**/

            /**Segundo intento
             try {
             String mobile = "5491164139535";
             String msg = "Its Working";
             Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg));
             sendIntent.setPackage("com.whatsapp");
             startActivity(sendIntent);
             }catch (Exception e){
             //whatsapp app not install
             }**/
            //fixme sendIntent.setPackage("com.whatsapp"); sacando eso, puedo hacer que mande el archivo por otro lado, en el futuro poner un if y hacer un checkbx
            String mobile = "5491164139535";
            String msg = "Factura n° 1000-10324024";
            //Intent sendIntent = new Intent();
            Intent sendIntent = new Intent(Intent.ACTION_SEND, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg));

            //sendIntent.setAction(Intent.ACTION_SEND);
            //sendIntent.putExtra(Intent.EXTRA_TEXT, "Factura n° 1000-10324024");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");

            PdfDocument pmyPdfDocument = new PdfDocument();
            Paint paint = new Paint();
            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
            PdfDocument.Page myPage = pmyPdfDocument.startPage(myPageInfo);
            Canvas canvas = myPage.getCanvas();

            paint.setTextSize(15.5f);
            paint.setColor(Color.rgb(0, 50, 250));

            canvas.drawText("Esto es una prueba del pdf", 20, 20, paint);
            paint.setTextSize(8.5f);
            canvas.drawText("PapaTwist, plotTwist", 20, 35, paint);

            pmyPdfDocument.finishPage(myPage);
            File file = new File(getActivity().getExternalFilesDir("/"), "WhatsappTest" + ".pdf");

            try {
                pmyPdfDocument.writeTo(new FileOutputStream(file));
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error 509", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            pmyPdfDocument.close();

            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri uri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(sendIntent);
        });

/*        Intent sendIntent = new Intent(); sendIntent.setAction(Intent.ACTION_SEND); sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send."); sendIntent.setType("text/plain"); startActivity(sendIntent);
        However, if you prefer to share directly to WhatsApp and bypass the system picker, you can do so by using setPackage in your intent:

        sendIntent.setPackage("com.whatsapp");
        This would simply be set right before you call startActivity(sendIntent);*/


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

}
