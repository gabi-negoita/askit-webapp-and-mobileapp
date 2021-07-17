import 'package:askit_mobile/decoration/colors.dart';
import 'package:flutter/material.dart';

class HeaderDivider extends StatelessWidget {
  final String header;

  HeaderDivider({this.header});

  @override
  Widget build(BuildContext context) {
    return Column(children: <Widget>[
      Row(children: <Widget>[
        Expanded(
          child: new Container(
              margin: const EdgeInsets.only(left: 10.0, right: 20.0),
              child: Divider(
                color: AskitColors.gray,
                height: 36,
              )),
        ),
        Text(this.header, style: TextStyle(color: AskitColors.black, fontFamily: 'Lato', fontWeight: FontWeight.bold, fontSize: 16)),
        Expanded(
          child: new Container(
              margin: const EdgeInsets.only(left: 20.0, right: 10.0),
              child: Divider(
                color: AskitColors.gray,
                height: 36,
              )),
        ),
      ]),
    ]);
  }
}
