package ru.rastaapps.examauto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SecretCode extends BroadcastReceiver {
    public SecretCode() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SECRET_CODE")){
            Intent i = new Intent(context, MasterActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("secretcode", true);
            context.startActivity(i);

        }
    }
}
