import 'dart:convert';

import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/user.dart';
import 'package:askit_mobile/routes/ask_questions_screen.dart';
import 'package:askit_mobile/routes/login_screen.dart';
import 'package:askit_mobile/routes/profile_screen.dart';
import 'package:askit_mobile/routes/questions_screen.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SideBar extends StatefulWidget {
  @override
  _SideBarState createState() => _SideBarState();
}

class _SideBarState extends State<SideBar> {
  SharedPreferences sharedPreferences;
  User loggedInUser;

  @override
  void initState() {
    super.initState();

    loggedInUser = null;
    checkForLoggedInUser();
  }

  void checkForLoggedInUser() async {
    sharedPreferences = await SharedPreferences.getInstance();

    String jsonUser = sharedPreferences.getString('user');
    if (jsonUser == null) return;

    setState(() {
      loggedInUser = User.fromJson(json.decode(jsonUser));
    });
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
        child: ListView(children: <Widget>[
      SizedBox(height: 160),
      getMenuLabel('PUBLIC'),
      loggedInUser != null
          ? ListTile(
              leading: FaIcon(FontAwesomeIcons.solidUserCircle, color: AskitColors.blue, size: 24),
              title: Text('Profile', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              onTap: () => {
                Navigator.push(context, MaterialPageRoute(builder: (context) => ProfileScreen(userId: loggedInUser.id)))
              },
            )
          : SizedBox(),
      ListTile(
          leading: FaIcon(FontAwesomeIcons.solidQuestionCircle, color: AskitColors.orange, size: 24),
          title: Text('Questions', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
          onTap: () => {Navigator.push(context, MaterialPageRoute(builder: (context) => QuestionsScreen()))}),
      loggedInUser != null
          ? ListTile(
              leading: FaIcon(FontAwesomeIcons.questionCircle, color: AskitColors.orange, size: 24),
              title: Text('Ask question', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              onTap: () => {Navigator.push(context, MaterialPageRoute(builder: (context) => AskQuestionsScreen()))})
          : SizedBox(),
      getDivider(),
      loggedInUser != null ? getMenuLabel('DISCONNECT') : getMenuLabel('GET ACCESS'),
      loggedInUser != null
          ? ListTile(
              leading: FaIcon(FontAwesomeIcons.signOutAlt, color: AskitColors.blue, size: 24),
              title: Text('Log Out', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              onTap: () {
                showDialog(
                    barrierDismissible: false,
                    context: context,
                    builder: (BuildContext context) => CupertinoAlertDialog(
                            title: Text('Log Out', style: TextStyle(fontSize: 18)),
                            content: Text(
                              'Are you sure you want to log out?',
                              style: TextStyle(fontSize: 14),
                            ),
                            actions: [
                              CupertinoDialogAction(
                                child: Text('No', style: TextStyle(fontSize: 14)),
                                onPressed: () => Navigator.pop(context),
                              ),
                              CupertinoDialogAction(
                                child: Text('Yes', style: TextStyle(fontSize: 14)),
                                onPressed: () {
                                  sharedPreferences.remove('user');

                                  Navigator.pop(context);
                                  Navigator.push(context, MaterialPageRoute(builder: (context) => LoginScreen()));
                                },
                              )
                            ]));
              })
          : ListTile(
              leading: FaIcon(FontAwesomeIcons.signInAlt, color: AskitColors.blue, size: 24),
              title: Text('Login', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
              onTap: () => {Navigator.push(context, MaterialPageRoute(builder: (context) => LoginScreen()))})
    ]));
  }

  Padding getMenuLabel(String label) {
    return Padding(
      padding: EdgeInsets.only(left: 16, top: 8, bottom: 8),
      child: Text(label, textAlign: TextAlign.left, style: TextStyle(fontWeight: FontWeight.bold, color: Colors.grey)),
    );
  }

  Divider getDivider() => Divider(thickness: 1);
}
