package com.uc.scanner_intent

import android.content.Context
import android.content.ContextWrapper;
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.URLConnection
import android.os.Build
import io.flutter.embedding.android.FlutterActivity
//import android.os.Bundle
import android.util.Log
import android.content.BroadcastReceiver;
import io.flutter.plugin.common.BinaryMessenger;


class ReceiveScannerIntentPlugin() :
        MethodCallHandler,
        StreamHandler,
        FlutterPlugin {


    private var barcode: String? = null
    private var intentActionName: String = "com.scanner.intent.ACTION"
    private var scannerIntentReceiver: BroadcastReceiver? = null
    private var applicationContext: Context? = null
    private var methodChannel: MethodChannel? = null
    private var eventChannel: EventChannel? = null

    private var eventSinkText: EventChannel.EventSink? = null


    override fun onAttachedToEngine(binding: FlutterPluginBinding) {
        onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
      }

      
    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
    applicationContext = null;
    methodChannel?.setMethodCallHandler(null);
    methodChannel = null;
    eventChannel?.setStreamHandler(null);
    eventChannel = null;
  }

    fun onAttachedToEngine(applicationContext: Context, messenger: BinaryMessenger) {
        this.applicationContext = applicationContext;
        methodChannel = MethodChannel(messenger, "receive_scanner_intent/messages");
        eventChannel  = EventChannel(messenger, "receive_scanner_intent/events-text");
        eventChannel?.setStreamHandler(this);
        methodChannel?.setMethodCallHandler(this);
      }

    override fun onListen(arguments: Any?, events: EventSink) {
       
      var filter: IntentFilter = IntentFilter();
      filter.addCategory(Intent.CATEGORY_DEFAULT);
      filter.addAction("com.dwexample.ACTION");
      filter.addAction("com.unicosm.ACTION");
      filter.addAction("com.android.scanner.broadcast");

        scannerIntentReceiver = createScannerIntentReceiver(events);
        applicationContext?.registerReceiver(
            scannerIntentReceiver, filter);
       
        when (arguments) {
            "text" -> eventSinkText = events
        }
    }

    override fun onCancel(arguments: Any?) {
        when (arguments) {
            "text" -> eventSinkText = null
        }
    }
    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            call.method == "getInitialText" -> result.success(barcode)
            call.method == "reset" -> {
                barcode = null
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }


    private fun createScannerIntentReceiver(events:EventSink):BroadcastReceiver {
        return object:BroadcastReceiver() {
          override fun onReceive(context:Context, intent:Intent) {
            
            val status1: String = intent.getStringExtra("com.symbol.datawedge.data_string") ?: ""
            val status2: String = intent.getStringExtra("data") ?: ""
            val status3: String = intent.getStringExtra("scandata") ?: ""


           val status = status1 + status2 + status3

            events.success(status)
          }
        }
      }



}
