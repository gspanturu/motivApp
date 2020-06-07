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
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
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
            .requestScopes(Scope(Scopes.FITNESS_ACTIVITY_READ))
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
        val readRequest = queryFitnessData()

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this)!!)
            .readData(readRequest)
            .addOnSuccessListener { dataReadResponse ->
                printData(dataReadResponse)
            }
    }
    private fun printData(dataReadResult: DataReadResponse) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.buckets.isNotEmpty()) {
            Log.i(TAG, "Number of returned buckets of DataSets is: " + dataReadResult.buckets.size)
            for (bucket in dataReadResult.buckets) {
                bucket.dataSets.forEach { dumpDataSet(it) }
            }
        } else if (dataReadResult.dataSets.isNotEmpty()) {
            Log.i(TAG, "Number of returned DataSets is: " + dataReadResult.dataSets.size)
            dataReadResult.dataSets.forEach { dumpDataSet(it) }
        }
        // [END parse_read_data_result]
    }
    private fun dumpDataSet(dataSet: DataSet) {
        Log.i(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
        val dateFormat: DateFormat = getTimeInstance()
        Log.d(TAG, "dumpDataSet: "+ dataSet.dataPoints.size)
        for (dp in dataSet.dataPoints) {
            Log.i(TAG, "Data point:")
            Log.i(TAG, "\tType: ${dp.dataType.name}")
            Log.i(TAG, "\tStart: ${dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS))}")
            Log.i(TAG, "\tEnd: ${dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))}")
            dp.dataType.fields.forEach {
                Log.i(TAG, "\tField: ${it.name} Value: ${dp.getValue(it)}")
            }
        }
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

    private fun queryFitnessData(): DataReadRequest {
        // [START build_read_data_request]
        // Setting a start and end date using a range of 1 week before this moment.
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val startTime = calendar.timeInMillis

        val dateFormat: DateFormat = getDateInstance()
        Log.i(TAG, "Range Start: ${dateFormat.format(startTime)}")
        Log.i(TAG, "Range End: ${dateFormat.format(endTime)}")

        return DataReadRequest.Builder()
            // The data request can specify multiple data types to return, effectively
            // combining multiple data queries into one call.
            // In this example, it's very unlikely that the request is for several hundred
            // datapoints each consisting of a few steps and a timestamp.  The more likely
            // scenario is wanting to see how many steps were walked per day, for 7 days.
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            // Analogous to a "Group By" in SQL, defines how data should be aggregated.
            // bucketByTime allows for a time span, whereas bucketBySession would allow
            // bucketing by "sessions", which would need to be defined in code.
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()
    }
}
