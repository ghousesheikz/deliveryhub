package com.shaikhomes.deliveryhub.ui.ordercalculation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shaikhomes.deliveryhub.R;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;




public class TransactionMessageDialog extends DialogFragment {


    public static final String TAG = "TransactionMessageDialog";
    private Long orderID;
  //  private CreateOrderModel createOrderModel;
    private int flag;

    public interface Click{

        void ok(Dialog dialogFragment);
    }

    private Click click;

    public TransactionMessageDialog() {
        super();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            this.click = (Click) context;

        }
        catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement mainFragmentCallback");

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.click = null;
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

       /* Bundle mArgs = getArguments();
        if (mArgs != null) {
            this.orderID = 0l;*/


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.transaction_message_dialog_layout, container, false);


        LottieAnimationView anime = view.findViewById(R.id.anime);

        LinearLayout linearLayout10 = view.findViewById(R.id.linearLayout10);

        TextView status = view.findViewById(R.id.status);

        TextView order_id = view.findViewById(R.id.order_id);
        order_id.setText(String.valueOf(orderID));

       // if(flag ==1){
            status.setText("SUCCESSFUL");
            anime.setAnimation(R.raw.star_success);
            linearLayout10.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
       /* }else if(flag ==2) {
            status.setText("PAYMENT FAILED");
            anime.setAnimation(R.raw.order_noti);
            linearLayout10.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
            status.setTextColor(view.getContext().getResources().getColor(R.color.red));
        }else if(flag == 3) {
            status.setText("CANCELLED BY USER");
            anime.setAnimation(R.raw.cancel_user);
            linearLayout10.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
            status.setTextColor(view.getContext().getResources().getColor(R.color.red));
        }else {
            status.setText("FAILED");
            anime.setAnimation(R.raw.order_noti);
            linearLayout10.setBackgroundColor(view.getContext().getResources().getColor(R.color.white));
            status.setTextColor(view.getContext().getResources().getColor(R.color.red));
        }*/



        //order_id.setText(String.valueOf(orderID));


        TextView ok_ = view.findViewById(R.id.ok_);
        ok_.setOnClickListener(v -> click.ok(getDialog()));

        TextView max, sell, discount, quantity , shipping, total, title_price, saving;

        max = view.findViewById(R.id.max);
        sell = view.findViewById(R.id.sell);
        discount = view.findViewById(R.id.discount);
        quantity = view.findViewById(R.id.quantity);
        shipping = view.findViewById(R.id.shipping);
        total = view.findViewById(R.id.total);
        title_price = view.findViewById(R.id.title_price);
        saving = view.findViewById(R.id.saving);

      /*  try {
            OrderSummary orderSummary  = createOrderModel.getCartDetails().getOrderSummary();


            max.setText("₹" + orderSummary.getTotSellingPrice());
            sell.setText("₹" + orderSummary.getTotSellingPrice());
            discount.setText("₹" + orderSummary.getTotDiscAmount());
            quantity.setText("₹" + orderSummary.getTotQuantity());
            shipping.setText("₹" + orderSummary.getTotShippingCost());
            total.setText("₹" + orderSummary.getTotalAmount() + "/-");
            title_price.setText("Price (" + orderSummary.getTotQuantity()+ " items)");


            saving.setText("You will save on this purchase- " + "₹" + orderSummary.getTotDiscAmount() +"/-");


        }catch (Exception e){
            e.printStackTrace();
        }*/



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            try {
                Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
