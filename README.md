UsageTracker README
===================

##Installation:
1. Set up [AWS](http://aws.amazon.com/). 
  * This libary uses your own AWS account.
  * Specifically, you will need to create your own instance of [Amazon SimpleDB](http://aws.amazon.com/simpledb/).  You can create a domain prefix for the library to use if you have other SimpleDB projects.
2. Import library.
  * Download project and import into Eclipse.
  * Mark as a library.
  * In your project, add library to build path.
  * A more detailed explanation can be found [here](http://stackoverflow.com/questions/8248196/how-to-add-a-library-project-to-a-android-project).
3. Set permissions in Manifest file.
```xml
<uses-permission android:name="android.permission.INTERNET"/> 
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.READ_CALL_LOG" /> 
```
4. Add `Service`'s and `BroadcastReceiver`'s to Manifest file.
```xml
<service android:name="com.samgavis.usagetracker.CallCacheService" />
<service android:name="com.samgavis.usagetracker.DataCacheService" />
<receiver android:name="com.samgavis.usagetracker.StartupReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
<receiver android:name="com.samgavis.usagetracker.PhoneStateReceiver">
    <intent-filter>
        <action android:name="android.intent.action.PHONE_STATE" />
    </intent-filter>
</receiver>
```
5. Subclass `PushService`
  * You must provide a `getSimpleDBClient()` method and a `handleError()` method.
  * You must start this service somehow. It is strongly recommended to use a `Startup BroadcastReceiver` to avoid storing too much data to the local database.  You should also make sure to call `setServiceAlarm()` when the app is first opened.
6. Construct `UsageTracker` to capture data.