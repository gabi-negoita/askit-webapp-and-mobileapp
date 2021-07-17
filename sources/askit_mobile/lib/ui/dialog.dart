import 'package:askit_mobile/decoration/colors.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class AskitDialog extends StatelessWidget {
  static const int error = -1;
  static const int info = 0;
  static const int warning = 1;
  static const int success = 2;

  final String title;
  final String content;
  final int type;

  AskitDialog(this.title, this.content, this.type);

  @override
  Widget build(BuildContext context) {
    Color foreground;

    switch (type) {
      case AskitDialog.error:
        foreground = AskitColors.darkRed;
        break;
      case AskitDialog.info:
        foreground = AskitColors.black;
        break;
      case AskitDialog.warning:
        foreground = AskitColors.darkOrange;
        break;
      case AskitDialog.success:
        foreground = AskitColors.darkGreen;
        break;
    }
    return CupertinoAlertDialog(
        title: Text(title, style: TextStyle(color: foreground, fontSize: 18)),
        content: Text(
          content,
          style: TextStyle(color: foreground, fontSize: 14),
        ),
        actions: [
          CupertinoDialogAction(
            child: Text('Close', style: TextStyle(fontSize: 14)),
            onPressed: () => Navigator.pop(context),
          )
        ]);
  }
}
