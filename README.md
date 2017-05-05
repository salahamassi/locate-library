# Locate library

# What is it? 

This library simplified the way to get current location for user (lat,lng) it's based on google location library,
with more option like request permissions for android 6 and above.

It easily get your current location by one line !

------ 

# How to use it? 

## First Install
**Maven**

```xml
<dependency>
  <groupId>net.salah.locate</groupId>
  <artifactId>locate</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```

**Gradle**

```gradle

dependencies {
   compile 'net.salah.locate:locate:1.0.2'
}
```

------ 
## Second Usage
make your activity or fragment class implements Locate.LocationResult 

```java
 class MainActivity extends AppCompatActivity implements Locate.LocationResult 
```
put this code where you need

```java
  Locate.with(MainActivity.this)
                        .addLocationResultListener(MainActivity.this)
                        .build();
```

and you get the result in overridden method 'onGetLocationSuccess(double lat, double lng)' or 'onGetLocationFailed(@Nullable String message)'
### `onGetLocationSuccess`

```java
  @Override
    public void onGetLocationSuccess(double lat, double lng) {
        ((TextView)findViewById(R.id.result)).setText(getString(R.string.lat)+" : "+lat+"\n"+getString(R.string.lng)+" : "+lng);
    }
```

### `onGetLocationFailed`
```java
    @Override
    public void onGetLocationFailed(@Nullable String message) {
        ((TextView)findViewById(R.id.result)).setText(message);
    }
```

------ 

# ScreenShot

![permission](https://cloud.githubusercontent.com/assets/17902030/25717753/25e96ba0-310c-11e7-9df9-58ffba63856c.gif)

### `Locate` methods
* `debuggingMode()` 
 	* for tracking and print all operation happened and library in android studio logcat not working to yet :sweat_smile: 
* `setPriority(Priority priority)`
	 * used to  set Priority for LocationRequest based on google location library more here
  * [LocationRequest](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest)
* `setInterval(long interval)`
	* used to  set interval for LocationRequest based on google location library for example if you set it "60000" that means that you'll get updates every 60 seconds.
    more here
  * [LocationRequest](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest)
*  `setFastInterval(long fastInterval)`
	* used to  set fast interval for LocationRequest based on google location library 
   more here 
   * [LocationRequest](https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest)

  ------ 
# Developer contact 
   * [Facebook](https://www.facebook.com/profile.php?id=100006656534009)
   * [Twitter](https://twitter.com/salahamassi)


  
