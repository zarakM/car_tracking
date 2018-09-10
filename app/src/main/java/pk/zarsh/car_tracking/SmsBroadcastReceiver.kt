package pk.zarsh.car_tracking

import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle
import android.provider.Telephony
import android.os.Build
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.telephony.SmsMessage
import android.util.Log


/**
 * Created by HP on 6/18/18.
 */
class SmsBroadcastReceiver: BroadcastReceiver() {

    private var mListener: SmsListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.extras

        val pdus = data!!.get("pdus") as Array<Any>

        for (i in pdus.indices) {
            val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
            val sender = smsMessage.displayOriginatingAddress

            //Check the sender to filter messages which we require to read

            Log.d("send",sender)
            if (sender == "+923114384382") {

                val messageBody = smsMessage.messageBody
                Log.d("mess",messageBody)
                //Pass the message text to interface
                mListener!!.messageReceived(messageBody)

            }
        }
    }
    internal fun bindListener(listener: SmsListener) {
        mListener = listener
    }
}
