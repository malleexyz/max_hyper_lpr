
import 'dart:async';

import 'package:flutter/services.dart';

class HyperLpr {
  static const MethodChannel _channel =
      const MethodChannel('hyper_lpr');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> get startScan async {
    final String version = await _channel.invokeMethod('startScan');
    return version;
  }

}
