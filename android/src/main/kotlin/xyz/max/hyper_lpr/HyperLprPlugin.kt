package xyz.max.hyper_lpr

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.pcl.ocr.ui.LPRActivity
import com.pcl.ocr.ui.LPRActivity.REQUEST_LPR_CODE
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry


/** HyperLprPlugin */
class HyperLprPlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
    private val CHANNEL = "hyper_lpr"

    private lateinit var channel: MethodChannel

    //  private var delegate: ImagePickerDelegate? = null
    private var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    private var activityBinding: ActivityPluginBinding? = null
    private var application: Application? = null
    private var activity: Activity? = null

    private val PERMISSION_LPR = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val PERMISSION_CODE = 100

    private var result: MethodChannel.Result? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        pluginBinding = flutterPluginBinding;
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        pluginBinding = null
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        setup(
                pluginBinding!!.binaryMessenger,
                (pluginBinding!!.applicationContext as Application),
                activityBinding!!.activity,
                null,
                activityBinding!!)
    }

    override fun onDetachedFromActivity() {
        tearDown()
    }

    private fun setup(
            messenger: BinaryMessenger,
            application: Application,
            activity: Activity,
            registrar: PluginRegistry.Registrar?,
            activityBinding: ActivityPluginBinding) {
        this.activity = activity
        this.application = application
        channel = MethodChannel(messenger, CHANNEL)
        channel.setMethodCallHandler(this)

//    observer = LifeCycleObserver(activity)
        if (registrar != null) {
            // V1 embedding setup for activity listeners.
//      application.registerActivityLifecycleCallbacks(observer)
            registrar.addActivityResultListener(this)
//      registrar.addRequestPermissionsResultListener(delegate)
        } else {
            // V2 embedding setup for activity listeners.
            activityBinding.addActivityResultListener(this)
//      activityBinding.addRequestPermissionsResultListener(delegate)

        }
    }

    private fun tearDown() {
        activityBinding = null
        channel.setMethodCallHandler(null);
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        this.result = result;
        if (activity == null) {
            result.error("no_activity", "image_picker plugin requires a foreground activity.", null);
            return;
        }

        when (call.method) {
          "getPlatformVersion" -> {
            ActivityCompat.requestPermissions(this.activity!!, PERMISSION_LPR, PERMISSION_CODE)
            result.success("MAX Android ${android.os.Build.VERSION.RELEASE}")
          }
          "startScan" -> {
            activity!!.startActivityForResult(Intent(activity, LPRActivity::class.java), REQUEST_LPR_CODE)
          }
            else -> {
                result.notImplemented()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        // 识别成功回调，车牌识别
        if (requestCode == REQUEST_LPR_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val card = data.getStringExtra("card")
                this.result!!.success(card)
//                AlertDialog.Builder(activity!!)
//                        .setMessage(card)
//                        .setNegativeButton("OK") { dialog: DialogInterface?, which: Int -> }
//                        .show()
            }
        }
        return true;
    }

}
