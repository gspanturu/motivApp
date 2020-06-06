package nvs.motivapp.googlesignintest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.support.wearable.authentication.OAuthClient
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType.AGGREGATE_CALORIES_EXPENDED
import com.google.android.gms.fitness.data.DataType.TYPE_CALORIES_EXPENDED
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.text.DateFormat.getTimeInstance
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.jar.Manifest


//https://developer.android.com/training/wearables/apps/creating#pair-phone-with-avd
//https://developer.android.com/training/wearables/apps/auth-wear
//https://console.firebase.google.com/project/motivapp-45177/settings/general/web:MzE0ODJlZWItNDUxYi00MmIxLTg1YTctNThlMTE1NTdlNjFj
class MainActivity : WearableActivity() {

    companion object{
        val REQUEST_CODE_SIGN_IN = 8001
        val REQUEST_CODE_GET_PERMISSION = 1919
    }

    private val TAG = "MainActivity"
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                REQUEST_CODE_GET_PERMISSION);

        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)!= PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "yeet")
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupGoogleSignInClient();

        findViewById<View>(R.id.sign_in_button).setOnClickListener{ signIn() }
        findViewById<View>(R.id.sign_out_button).setOnClickListener { signOut() }
        findViewById<View>(R.id.stepsOfWeek).setOnClickListener { getStepsFromLastWeek() }

        checkAlreadySignedIn()
        // Enables Always-on
        setAmbientEnabled()
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                updateUi(null)
                Toast.makeText(
                    this,
                    R.string.signout_successful,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
            .requestIdToken("762212303946-2bf82g29aa5hkmg11gsqrbh831ujh722.apps.googleusercontent.com")
                .build()
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }
    private fun checkAlreadySignedIn() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUi(account)
    }
    private fun updateUi(account: GoogleSignInAccount?) {
        if (account != null) {
            Toast.makeText(this, "Google Sign-In successfull", Toast.LENGTH_SHORT).show()
            findViewById<View>(R.id.sign_in_button).visibility = View.GONE
            Toast.makeText(this, account.id, Toast.LENGTH_LONG).show()
            findViewById<View>(R.id.sign_out_button).visibility = View.VISIBLE
            findViewById<View>(R.id.stepsOfWeek).visibility = View.VISIBLE
        }
        else{
            findViewById<View>(R.id.sign_in_button).visibility = View.VISIBLE
            findViewById<View>(R.id.sign_out_button).visibility = View.GONE
            findViewById<View>(R.id.stepsOfWeek).visibility = View.GONE
        }
    }

    private fun getStepsFromLastWeek(){
        Log.d(TAG, "got here")
        val cal = Calendar.getInstance()
        val now = Date()
        cal.time = now
        val endTime = cal.timeInMillis
        cal.add(Calendar.WEEK_OF_YEAR, -1)
        val startTime = cal.timeInMillis

        val dateFormat: DateFormat = getDateInstance()
        Log.i(TAG, "Range Start: " + dateFormat.format(startTime))
        Log.i(TAG, "Range End: " + dateFormat.format(endTime))

        val readRequest: DataReadRequest = DataReadRequest.Builder()
            .aggregate(
                TYPE_CALORIES_EXPENDED,
                AGGREGATE_CALORIES_EXPENDED
            )
            .bucketByActivityType(1, TimeUnit.SECONDS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
        val response: Task<DataReadResponse> =
            Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
                .readData(readRequest)

        response.addOnCompleteListener { getTaskCompleted(it) }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    protected fun handleSignInResult(completedTask: Task<GoogleSignInAccount?>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUi(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode() + " message: "+e.message)
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            updateUi(null)
        }
    }

    private fun getTaskCompleted(response: Task<DataReadResponse>) {
        val dataSets = response.result!!.dataSets

        for (dataSet in dataSets){
            Log.i(
                TAG,
                "Data returned for Data type: " + dataSet.dataType.name
            )
            val dateFormat: DateFormat = getTimeInstance()

            for (dp in dataSet.dataPoints) {
                Log.i(TAG, "Data point:")
                Log.i(TAG, "\tType: " + dp.dataType.name)
                Log.i(
                    TAG,
                    "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))
                )
                Log.i(
                    TAG,
                    "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                )
                for (field in dp.dataType.fields) {
                    Log.i(
                        TAG,
                        "\tField: " + field.name.toString() + " Value: " + dp.getValue(field)
                    )
                }
            }
        }
    }
}
