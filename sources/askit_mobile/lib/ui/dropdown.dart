import 'package:askit_mobile/decoration/colors.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Dropdown extends StatefulWidget {
  static of(BuildContext context, {bool root = false}) =>
      root ? context.findRootAncestorStateOfType<DropdownState>() : context.findAncestorStateOfType<DropdownState>();

  final List<String> items;
  final String hint;
  String selected;

  Dropdown(this.items, {this.hint, this.selected, Key key}) : super(key: key);

  void clearSelected() {
    createState();
  }

  @override
  DropdownState createState() => DropdownState();
}

class DropdownState extends State<Dropdown> {
  String _selected;

  @override
  void initState() {
    super.initState();

    _selected = null;
    widget.selected = _selected;
  }

  void clearSelected() {
    setState(() {
      _selected = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: EdgeInsets.all(8),
        child: Container(
            padding: EdgeInsets.symmetric(horizontal: 16),
            decoration: BoxDecoration(
                border: Border.all(color: AskitColors.gray, width: 0.5), borderRadius: BorderRadius.circular(4)),
            child: DropdownButton<String>(
              icon: GestureDetector(
                child: _selected == null ? Icon(Icons.arrow_drop_down) : Icon(Icons.close),
                onTap: () {
                  if (_selected == null) return;

                  setState(() {
                    _selected = null;
                    widget.selected = _selected;
                  });
                },
              ),
              underline: SizedBox(),
              isExpanded: true,
              hint: Text(widget.hint),
              value: _selected,
              items: widget.items.map((String value) {
                return new DropdownMenuItem<String>(value: value, child: new Text(value));
              }).toList(),
              onChanged: (item) {
                setState(() {
                  _selected = item;
                  widget.selected = _selected;
                });
              },
            )));
  }
}
