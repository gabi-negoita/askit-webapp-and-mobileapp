import 'package:askit_mobile/decoration/colors.dart';
import 'package:flutter/material.dart';

class AskitTheme {
  static ThemeData getDefault() {
    return ThemeData(primaryColor: AskitColors.blue, fontFamily: 'Lato', iconTheme: IconThemeData(size: 14));
  }
}