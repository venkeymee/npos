package com.bytecodecomp.npos.Activities.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bytecodecomp.npos.Activities.Admin.Business.ListBusinessActivity;
import com.bytecodecomp.npos.Activities.Admin.Packages.ListPackagesActivity;
import com.bytecodecomp.npos.Activities.Admin.Subscriptions.ListSubscriptionsActivity;
import com.bytecodecomp.npos.Plugins.Toasty.Toasty;
import com.bytecodecomp.npos.R;

public class AdminActivity extends AppCompatActivity {

    LinearLayout card_business, card_packages, card_subscriptions, card_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Business list layout
        card_business = (LinearLayout) findViewById(R.id.card_business);
        card_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminActivity.this, ListBusinessActivity.class);
                startActivity(intent);

            }
        });


        //Packages list layout
        card_packages = (LinearLayout) findViewById(R.id.card_packages);
        card_packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminActivity.this, ListPackagesActivity.class);
                startActivity(intent);

            }
        });


        //Subscription list layout
        card_subscriptions = (LinearLayout) findViewById(R.id.card_subscriptions);
        card_subscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminActivity.this, ListSubscriptionsActivity.class);
                startActivity(intent);

            }
        });


        //Notifications list layout
        card_notification = (LinearLayout) findViewById(R.id.card_notification);
        card_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toasty.warning(AdminActivity.this, "Comming soon").show();

//                Intent intent = new Intent(AdminActivity.this, ListBusinessActivity.class);
//                startActivity(intent);

            }
        });






    }
}
