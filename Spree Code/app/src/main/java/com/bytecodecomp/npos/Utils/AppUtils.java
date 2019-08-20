package com.bytecodecomp.npos.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.bytecodecomp.npos.R;

import java.io.File;
import java.util.Random;


/**
 * Created by Ashiq on 4/12/2017.
 */

public class AppUtils {

    private static long backPressed = 0;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";


    public static void tapToExit(Activity activity) {
        if (backPressed + 2500 > System.currentTimeMillis()){
            activity.finish();
        }
        else{
            showToast(activity.getApplicationContext(), activity.getResources().getString(R.string.tapAgain));
        }
        backPressed = System.currentTimeMillis();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void share(Activity activity, String fileUrl) {//, String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        //shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        Uri uri = Uri.fromFile(new File(fileUrl));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


}
