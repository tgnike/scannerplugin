# ReceiveScannerIntent

A flutter plugin that enables flutter apps to receive text from internal barcode scanners.

# Installation

add this to pubspec.yaml in `dependencies` section

``` yaml
  scanner_intent: 
    git:
      url: https://github.com/tgnike/scannerplugin.git
```

# Example for StatefulWidget

```dart
class _ViewState extends State<View> {
  late StreamSubscription _intentDataStreamSubscription;

  @override
  void initState() {
    super.initState();

    // init listen to intent
    _intentDataStreamSubscription =
        ReceiveScannerIntent.getTextStream()!.listen((String value) {
      // do something with scanned text
    }, onError: (err) {});
  }

   @override
  void dispose() {
    // cancel subscription
    _intentDataStreamSubscription.cancel();
    super.dispose();
  }

}
```

# Android settings

Intent action: `com.dwexample.ACTION | com.unicosm.ACTION | com.android.scanner.broadcast | android.intent.ACTION_DECODE_DATA`
Intent category: `android.intent.category.DEFAULT`
Data field: `data | scandata | com.symbol.datawedge.data_string | barcode_string`
Intent delivery: `Broadcast intent`
