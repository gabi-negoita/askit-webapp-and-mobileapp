import 'package:askit_mobile/decoration/colors.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Loader extends StatelessWidget {
  final double size;

  const Loader({Key key, this.size}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
        child: SizedBox(
      width: size,
      height: size,
      child: CircularProgressIndicator(
        backgroundColor: AskitColors.lightGray,
        valueColor: AlwaysStoppedAnimation<Color>(AskitColors.darkGray),
      ),
    ));
  }
}