package pk.zarsh.car_tracking

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v4.app.ActivityCompat
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.provider.Telephony
import android.content.IntentFilter
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.*
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var smsBroadcastReceiver:SmsBroadcastReceiver
    lateinit var string: String
    var lat =""
    var lon =""

    lateinit var lat1:String
    lateinit var lat2:String
    lateinit var lat3:String
    lateinit var lat4:String
    lateinit var lat5:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        smsBroadcastReceiver = SmsBroadcastReceiver()
        registerReceiver(smsBroadcastReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        val obj = object : SmsListener {
            override fun messageReceived(messageText: String) {
                string =messageText
                Log.d("location",messageText)
            }
        }
        smsBroadcastReceiver.bindListener(obj)

        val location = findViewById<Button>(R.id.locationate)
        location.setOnClickListener({
            if(::string.isInitialized)
            {
                Log.d("loc1",string)
                for (i in 7..15)
                {
                    lat = lat+string[i]
                    Log.d("loc2",lat)
                }
                for (i in 19..27)
                {
                    lon = lon+string[i]
                    Log.d("loc3",lon)
                }
            }

            val uri="http://childbook.000webhostapp.com/locations.php"
            val request = object : StringRequest(Request.Method.POST, uri, object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    if(response == "yes")
                    {
                        Log.d("colo",response)
                    }
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError) {

                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val param = HashMap<String, String>()
                    param.put("lat",lat)
                    Log.d("lat",lat)
                    Log.d("lon",lon)
                    param.put("lon",lon)
                    return param
                }
            }
            Volley.newRequestQueue(this).add(request)
        })

        val active=findViewById<Button>(R.id.locate)
        active.setOnClickListener {
            if(::string.isInitialized)
            {
                Log.d("loc1",string)
                for (i in 7..15)
                {
                    lat = lat+string[i]
                    Log.d("loc2",lat)
                }
                for (i in 19..27)
                {
                    lon = lon+string[i]
                    Log.d("loc3",lon)
                }
            }
            val url = "https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lon + "&travelmode=driving"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        val recyclerview = findViewById<ListView>(R.id.locationlist)
        val uri ="http://childbook.000webhostapp.com/location_s.php"
        val postRequestsa = JsonObjectRequest( Request.Method.POST, uri, null, Response.Listener { response ->
            try {
                val jsonArray = response.getJSONArray("temp")
                for (i in 0..4){
                    var json = jsonArray.getJSONObject(i)
                    var lat = json.getString("lat")
                    var lon = json.getString("lon")
                    Log.d("ideass",i.toString())
                    Log.d("lat",lat)
                    Log.d("lon",lon)

                    if(i==0){
                        lat1=lat+" "+lon
                        Log.d("lat0",lat)
                    }
                    if(i==1){
                        lat2=lat+" "+lon
                        Log.d("lat1",lat)

                    }
                    if(i==2){
                        lat3=lat+" "+lon
                        Log.d("lat2",lat)

                    }
                    if(i==3){
                        lat4=lat+" "+lon
                        Log.d("lat3",lat)
                    }
                    if(i==4){
                        lat5=lat+" "+lon
                        Log.d("lat4",lat)
                    }
                }
                val values = arrayOf(lat1, lat2, lat3, lat4, lat5)
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,values)
                recyclerview.adapter=adapter
            } catch (e: JSONException) {
                Log.d("error", " " + e.message)
            }
        }, object : Response.ErrorListener {
            override fun onErrorResponse(error: VolleyError) {
                Log.d("error", " " + error.message)
            }
        })
        Volley.newRequestQueue(this).add(postRequestsa)

        recyclerview.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View,
                                     position: Int, id: Long) {
                val url = "https://www.google.com/maps/dir/?api=1&destination=30.099482,67.003723&travelmode=driving"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted, yay! Do the
                        // SMS related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                }
                return
            }
        }
    }

    fun isSmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), 0)
    }

    fun showRequestPermissionsInfoAlertDialog() {
        showRequestPermissionsInfoAlertDialog(true)
    }

    fun showRequestPermissionsInfoAlertDialog(makeSystemRequest: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permit SMS") // Your own title
        builder.setMessage("Agreed") // Your own message

        builder.setPositiveButton("ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                if (makeSystemRequest) {
                    requestReadAndSendSmsPermission()
                }
            }
        })
        builder.setCancelable(false)
        builder.show()
    }
}
