package com.ogma.dealshaiapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogma.dealshaiapp.R;
import com.ogma.dealshaiapp.dialog.ContactInfo;
import com.ogma.dealshaiapp.dialog.MenuInfo;
import com.ogma.dealshaiapp.dialog.MoreInfoView;
import com.ogma.dealshaiapp.fragment.FragmentDetailsPageBanner;
import com.ogma.dealshaiapp.model.CouponsDetails;
import com.ogma.dealshaiapp.network.NetworkConnection;
import com.ogma.dealshaiapp.network.WebServiceHandler;
import com.ogma.dealshaiapp.network.WebServiceListener;
import com.ogma.dealshaiapp.utility.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;

public class OfferActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoScrollViewPager banner_view_pager;
    private BannerAdapter slidingViewAdapter;
    private CircleIndicator banner_indicator;
    private FragmentManager fragmentManager;
    private JSONArray banner;
    private ArrayList<CouponsDetails> arrayList;
    private TextView title_header;
    private TextView tv_total_like;
    private TextView tv_title;
    private TextView tv_location;
    private TextView tv_outlets;
    private ImageView iv_like;
    private TextView tv_total_amount;
    private String couponId;
    private String merchantName;
    private String description;
    private int totalLike = 0;
    private String likes;
    private String userId;
    private String msg = "";
    private TextView coupon_title;
    private TextView coupon_details;
    private TextView coupon_old_price;
    private TextView coupon_new_preice;
    private TextView coupon_valid_for;
    private TextView coupon_valid_on;
    private TextView coupon_quantity;
    private String offerPrice;
    private String merchantId;
    private String title;
    private String couponDesc;
    private String validFor;
    private String validOn;
    private String price;
    private RelativeLayout parentPanel;
    private String merchant_id;
    private LinearLayout error_screen;
    private FrameLayout no_coupons;
    private RelativeLayout ll_coupon;
    private String shareUrl = "https://www.dealshai.in/";
    private TextView tv_btn_contact;
    private TextView tv_more_info;
    private TextView tv_btn_menu;
    private String content;
    private String contact;
    private static String[] PERMISSIONS_CALL = {Manifest.permission.CALL_PHONE};
    private static final int REQUEST_CALL = 1;
    private AlertDialog alertDialog;
    private String menuStr = "";
    private int maxQuantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        Session session = new Session(OfferActivity.this);
        HashMap<String, String> user = session.getUserDetails();

        userId = user.get(Session.KEY_ID);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            couponId = bundle.getString("coupon_id");
        }

        banner = new JSONArray();
        arrayList = new ArrayList<>();

        ImageView iv_back = findViewById(R.id.iv_back);
        banner_view_pager = findViewById(R.id.banner_view_pager);

        parentPanel = findViewById(R.id.parentPanel);
        error_screen = findViewById(R.id.error_screen);
        title_header = findViewById(R.id.title_header);
        tv_total_like = findViewById(R.id.tv_total_like);
        tv_title = findViewById(R.id.tv_title);
        tv_location = findViewById(R.id.tv_location);
        tv_outlets = findViewById(R.id.tv_outlets);
        tv_btn_contact = findViewById(R.id.tv_btn_contact);
        tv_more_info = findViewById(R.id.tv_more_info);
        iv_like = findViewById(R.id.iv_like);

        tv_btn_menu = findViewById(R.id.tv_btn_menu);
        TextView tv_btn_contact = findViewById(R.id.tv_btn_contact);
        TextView tv_more_info = findViewById(R.id.tv_more_info);
        tv_total_amount = findViewById(R.id.tv_total_amount);
        ImageView iv_share = findViewById(R.id.iv_share);
        ImageView iv_like = findViewById(R.id.iv_like);
        TextView tv_btn_buy_now = findViewById(R.id.tv_btn_buy_now);

        no_coupons = findViewById(R.id.no_coupons);
        ll_coupon = findViewById(R.id.ll_coupon);

        coupon_title = findViewById(R.id.tv_item_title);
        coupon_details = findViewById(R.id.tv_item_details);
        coupon_old_price = findViewById(R.id.tv_old_price);
        coupon_new_preice = findViewById(R.id.tv_new_price);
        coupon_valid_for = findViewById(R.id.tv_valid_for);
        coupon_valid_on = findViewById(R.id.tv_valid_on);
        coupon_quantity = findViewById(R.id.tv_quantity);

        ImageView iv_plus = findViewById(R.id.iv_plus);
        ImageView iv_minus = findViewById(R.id.iv_minus);

        banner_indicator = findViewById(R.id.banner_indicator);
        banner_view_pager.setInterval(4000);
        banner_view_pager.startAutoScroll();
        fragmentManager = getSupportFragmentManager();

        banner_view_pager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setTranslationX(page.getWidth() * -position);

                if (position <= -1.0F || position >= 1.0F) {
                    page.setAlpha(0.0F);
                } else if (position == 0.0F) {
                    page.setAlpha(1.0F);
                } else {
                    page.setAlpha(1.0F - Math.abs(position));
                }
            }
        });

        banner_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tv_btn_menu.setVisibility(View.GONE);
        no_coupons.setVisibility(View.GONE);
        ll_coupon.setVisibility(View.VISIBLE);

        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        tv_total_like.setOnClickListener(this);
        tv_btn_buy_now.setOnClickListener(this);
        iv_plus.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        tv_outlets.setOnClickListener(this);
        tv_btn_menu.setOnClickListener(this);
        iv_like.setOnClickListener(this);
        tv_more_info.setOnClickListener(this);
        tv_btn_contact.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        banner_view_pager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        banner_view_pager.startAutoScroll();
        NetworkConnection connection = new NetworkConnection(OfferActivity.this);
        if (connection.isNetworkConnected()) {
            getData(couponId);
        } else
            Toast.makeText(OfferActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        AlertDialog.Builder alertDialogBuilder;
        switch (id) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_btn_buy_now:

                if (Integer.parseInt(tv_total_amount.getText().toString().trim()) > 0) {
                    int totalAmount = Integer.parseInt(tv_total_amount.getText().toString().trim());

                    startActivity(new Intent(OfferActivity.this, CheckOutActivity.class)
                            .putExtra("flag", "OfferActivity")
                            .putExtra("couponList", arrayList)
                            .putExtra("totalAmount", String.valueOf(totalAmount)));
                } else {
                    Snackbar.make(parentPanel, "Please add some deals!", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_like:
            case R.id.tv_total_like:
                String msg = hitLike(couponId, userId);
                /**switch (msg) {
                    case "Like Successfully":
                        totalLike = totalLike + 1;
                        iv_like.setColorFilter(getBaseContext().getResources().getColor(R.color.color_heart));
                        tv_total_like.setText(String.valueOf(totalLike));
                        Toast.makeText(OfferActivity.this,"You Liked "+tv_title.getText(),Toast.LENGTH_SHORT).show();
                        break;
                    case "Dislike Successfully":
                        totalLike = totalLike - 1;
                        iv_like.setColorFilter(getBaseContext().getResources().getColor(R.color.uber_white));
                        tv_total_like.setText(String.valueOf(totalLike));
                        Toast.makeText(OfferActivity.this,"You Disliked "+tv_title.getText(),Toast.LENGTH_SHORT).show();
                        break;
                }*/
                break;
            case R.id.iv_share:
                share_via_app();
                break;
            case R.id.iv_plus:
                int quantity1 = Integer.parseInt(coupon_quantity.getText().toString().trim());
                if (maxQuantity == 1) {
                    if (quantity1 < 1) {
                        quantity1++;
                        setTotalPayable(Integer.parseInt(offerPrice));
                        coupon_quantity.setText(String.valueOf(quantity1));
                    } else
                        Snackbar.make(parentPanel, R.string.not_more_than_one, Snackbar.LENGTH_SHORT).show();
                } else {
                    quantity1++;
                    setTotalPayable(Integer.parseInt(offerPrice));
                    coupon_quantity.setText(String.valueOf(quantity1));
                }
                arrayList.clear();
                CouponsDetails couponDetails;
                if (Integer.parseInt(coupon_quantity.getText().toString()) > 0) {
                    couponDetails = new CouponsDetails();
                    couponDetails.setMerchantId(merchantId);
                    couponDetails.setCouponId(couponId);
                    couponDetails.setOriginalPrice(Integer.parseInt(price));
                    couponDetails.setNewPrice(Integer.parseInt(offerPrice));
                    couponDetails.setCouponTitle(title);
                    couponDetails.setQuantity(quantity1);
                    arrayList.add(couponDetails);
                }
                break;
            case R.id.iv_minus:
                int quantity2 = Integer.parseInt(coupon_quantity.getText().toString().trim());
                if (quantity2 > 0) {
                    quantity2--;
                    setTotalPayable(-Integer.parseInt(offerPrice));
                    coupon_quantity.setText(String.valueOf(quantity2));
                }
                arrayList.clear();
                if (Integer.parseInt(coupon_quantity.getText().toString()) > 0) {
                    couponDetails = new CouponsDetails();
                    couponDetails.setMerchantId(merchantId);
                    couponDetails.setCouponId(couponId);
                    couponDetails.setOriginalPrice(Integer.parseInt(price));
                    couponDetails.setNewPrice(Integer.parseInt(offerPrice));
                    couponDetails.setCouponTitle(title);
                    couponDetails.setQuantity(quantity2);
                    arrayList.add(couponDetails);
                }
                break;

            case R.id.tv_outlets:
                startActivity(new Intent(OfferActivity.this, MapsActivity.class).putExtra("merchantId", String.valueOf(merchantId)));
                break;
            case R.id.tv_btn_menu:

                new MenuInfo(OfferActivity.this, menuStr).show();
                break;
            case R.id.tv_more_info:
                if (content != null) {
                    new MoreInfoView(OfferActivity.this, content).show();
                }
                break;
            case R.id.tv_btn_contact:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    requestCallPermission();
                } else {

                    if (contact != null) {
                        /**alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle(R.string.contact);
                        alertDialogBuilder.setMessage(contact);
                        alertDialogBuilder.setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + contact));
                                startActivity(callIntent);
                            }
                        })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);*/
                        new ContactInfo(OfferActivity.this, contact).show();
                    } else
                        Snackbar.make(parentPanel, "No contact available", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String hitLike(String couponId, String userId) {
        WebServiceHandler webServiceHandler = new WebServiceHandler(OfferActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int isErr = Integer.parseInt(jsonObject.getString("err"));
                    if (isErr == 0) {
                        msg = jsonObject.getString("msg");
                        OfferActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (msg)
                                {
                                    case "Like Successfully":
                                        totalLike = totalLike + 1;
                                        iv_like.setColorFilter(getBaseContext().getResources().getColor(R.color.color_heart));
                                        tv_total_like.setText(String.valueOf(totalLike));
                                        Toast.makeText(OfferActivity.this,"You Liked "+tv_title.getText(),Toast.LENGTH_SHORT).show();
                                        break;
                                    case "Dislike Successfully":
                                        totalLike = totalLike - 1;
                                        iv_like.setColorFilter(getBaseContext().getResources().getColor(R.color.uber_white));
                                        tv_total_like.setText(String.valueOf(totalLike));
                                        Toast.makeText(OfferActivity.this,"You Disliked "+tv_title.getText(),Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        webServiceHandler.hitLike(couponId, userId);

        return msg;
    }

    private void getData(String couponId) {
        WebServiceHandler webServiceHandler = new WebServiceHandler(OfferActivity.this);
        webServiceHandler.serviceListener = new WebServiceListener() {
            @Override
            public void onResponse(String response) {
                JSONObject main = null;
                String is_error = null;
                int is_menu;
                try {
                    main = new JSONObject(response);
                    is_error = main.getString("err");
                    if (is_error == null || Integer.parseInt(is_error) == 1) {
                        Snackbar.make(parentPanel, "No data found", Snackbar.LENGTH_SHORT).show();
                        OfferActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                error_screen.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        try {
                            shareUrl = main.getString("share_link");
                            is_menu = main.getInt("is_menu");
                            if (is_menu == 1) {
                                menuStr = main.getString("menu");
                                setMenu(menuStr);
                            }
                            else
                                setButton();
                            banner = main.getJSONArray("slider");
                            if (banner.length() > 0) {
                                slidingViewAdapter = new BannerAdapter(fragmentManager, banner);
                                OfferActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        banner_view_pager.setAdapter(slidingViewAdapter);
                                        banner_indicator.setViewPager(banner_view_pager);
                                        banner_view_pager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % banner.length());
                                        banner_view_pager.setStopScrollWhenTouch(true);
                                        slidingViewAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(parentPanel, "Banner images data found", Snackbar.LENGTH_SHORT).show();
                        }
                        JSONObject merchant = null;
                        try {
                            merchant = main.getJSONObject("merchant");
                            if (merchant.length() > 0) {
                                merchantId = merchant.getString("id");
                                merchantName = merchant.getString("name");
                                final String city = merchant.getString("area");
                                description = merchant.getString("description");
                                totalLike = Integer.parseInt(merchant.getString("likes"));
                                contact = merchant.getString("contact");
                                content = merchant.getString("more");

                                OfferActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        title_header.setText(merchantName);
                                        tv_title.setText(merchantName);
                                        tv_location.setText(" " + city);
                                        tv_total_like.setText(String.valueOf(totalLike));
                                        tv_total_amount.setText("0");
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(parentPanel, "Merchant details not available.", Snackbar.LENGTH_SHORT).show();
                        }
                        try {
                            JSONObject cupon = main.getJSONObject("Cupon");
                            if (cupon.length() > 0) {
                                title = cupon.getString("title");
                                couponDesc = cupon.getString("description");
                                validFor = cupon.getString("valid_for_person");
                                validOn = cupon.getString("valid_on");
                                price = cupon.getString("price");
                                offerPrice = cupon.getString("our_price");
                                maxQuantity = cupon.getInt("is_single_purchase");

                                OfferActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        coupon_title.setText(title);
                                        coupon_details.setText(couponDesc);
                                        coupon_old_price.setText("₹ " + price);
                                        coupon_old_price.setPaintFlags(coupon_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        coupon_new_preice.setText("₹ " + offerPrice);
                                        coupon_valid_for.setText(validFor);
                                        coupon_valid_on.setText(validOn);
                                        coupon_quantity.setText("0");
//                                    holder.tv_old_price.setPaintFlags(holder.tv_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            OfferActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Snackbar.make(parentPanel, "No coupons available for this merchant.", Snackbar.LENGTH_SHORT).show();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            no_coupons.setVisibility(View.VISIBLE);
                                            ll_coupon.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(parentPanel, "Response error!", Snackbar.LENGTH_LONG).show();
                    OfferActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            error_screen.setVisibility(View.VISIBLE);

                        }
                    });
                }
            }
        };
        webServiceHandler.getOfferDetailsData(couponId, userId);
    }

    private void setMenu(String menuStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_btn_menu.setVisibility(View.VISIBLE);
                tv_btn_contact.setVisibility(View.VISIBLE);
                tv_more_info.setVisibility(View.VISIBLE);
            }
        });
    }
    private void setButton()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //tv_btn_menu.setVisibility(View.VISIBLE);
                tv_btn_contact.setVisibility(View.VISIBLE);
                tv_more_info.setVisibility(View.VISIBLE);
            }
        });
    }

    public void share_via_app() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        sendIntent.putExtra("address", phoneNumber);
//        sendIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
    }

    private void setTotalPayable(int amount) {

        int totalAmount = Integer.parseInt(tv_total_amount.getText().toString().trim());
        if (totalAmount >= 0) {
            totalAmount = totalAmount + amount;
        }
        tv_total_amount.setText(String.valueOf(totalAmount));
    }

    private class BannerAdapter extends FragmentPagerAdapter {
        private JSONArray banner;

        BannerAdapter(FragmentManager fm, JSONArray banner) {
            super(fm);
            this.banner = banner;
        }

        @Override
        public Fragment getItem(int position) {
            try {
                return FragmentDetailsPageBanner.newInstance(banner.getString(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            return banner.length();
        }
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
            Snackbar.make(parentPanel, "Call Permission Required.", Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(OfferActivity.this, PERMISSIONS_CALL, REQUEST_CALL);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_CALL, REQUEST_CALL);
        }
    }

}