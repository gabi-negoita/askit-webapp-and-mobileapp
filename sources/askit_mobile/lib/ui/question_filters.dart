import 'package:askit_mobile/decoration/colors.dart';
import 'package:expansion_tile_card/expansion_tile_card.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

import 'labeled_icon.dart';

class QuestionFilters extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
        child: ExpansionTileCard(
          heightFactorCurve: Curves.easeOut,
          finalPadding: EdgeInsets.zero,
          duration: Duration(milliseconds: 250),
          borderRadius: BorderRadius.circular(4),
          elevation: 0,
          title: LabeledIcon(
            icon: FaIcon(
              FontAwesomeIcons.filter,
              color: AskitColors.blue,
              size: 16,
            ),
            label: 'Filters',
            textSize: 16,
          ),
          children: <Widget>[
            Padding(
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: TextFormField(
                decoration: InputDecoration(
                  border: UnderlineInputBorder(),
                  labelText: 'Search',
                ),
              ),
            ),
            DropdownButton<String>(
              icon: const Icon(Icons.arrow_downward),
              iconSize: 24,
              elevation: 16,
              style: const TextStyle(color: Colors.deepPurple),
              underline: Container(
                height: 2,
                color: Colors.deepPurpleAccent,
              ),
              items: <String>['One', 'Two', 'Free', 'Four'].map<DropdownMenuItem<String>>((String value) {
                return DropdownMenuItem<String>(
                  value: value,
                  child: Text(value),
                );
              }).toList(),
            )
          ],
        ));
  }
}
