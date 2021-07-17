import 'package:askit_mobile/decoration/colors.dart';
import 'package:flutter/material.dart';

class LabeledIcon extends StatelessWidget {
  final Widget icon;
  final String label;
  final double textSize;

  LabeledIcon({this.icon, this.label, this.textSize});

  @override
  Widget build(BuildContext context) {
    return RichText(
      text: TextSpan(
        children: [
          WidgetSpan(child: Padding(padding: const EdgeInsets.only(right: 4), child: this.icon)),
          TextSpan(
              text: this.label,
              style: TextStyle(
                  color: AskitColors.black,
                  fontFamily: 'Lato',
                  fontWeight: FontWeight.bold,
                  fontSize: (this.textSize == null) ? 12 : this.textSize)),
        ],
      ),
    );
  }
}
