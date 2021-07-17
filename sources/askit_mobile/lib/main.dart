import 'package:askit_mobile/routes/login_screen.dart';
import 'package:askit_mobile/routes/questions_screen.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'decoration/themes.dart';

void main() => runApp(AskitMobileApplication());

class AskitMobileApplication extends StatefulWidget {
  @override
  _AskitMobileApplicationState createState() => _AskitMobileApplicationState();
}

class _AskitMobileApplicationState extends State<AskitMobileApplication> {
  Widget _home;
  SharedPreferences sharedPreferences;
  bool isLoggedIn;

  @override
  void initState() {
    super.initState();

    _home = Loader();

    isLoggedIn = false;
    setLoggedIn();
  }

  void setLoggedIn() async {
    sharedPreferences = await SharedPreferences.getInstance();
    String jsonUser = sharedPreferences.getString('user');

    if (jsonUser == null) {
      setState(() {
        _home = LoginScreen();
      });

      return;
    }

    setState(() {
      _home = QuestionsScreen();
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(debugShowCheckedModeBanner: false, title: 'Askit', theme: AskitTheme.getDefault(), home: _home);
  }
}
