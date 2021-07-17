import 'dart:convert';

import 'package:askit_mobile/api/user_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/user.dart';
import 'package:askit_mobile/routes/questions_screen.dart';
import 'package:askit_mobile/ui/dialog.dart';
import 'package:askit_mobile/ui/sidebar.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inner_drawer/inner_drawer.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:validators/validators.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key key}) : super(key: key);

  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  SharedPreferences sharedPreferences;
  final GlobalKey<InnerDrawerState> _innerDrawerKey = GlobalKey<InnerDrawerState>();

  TextEditingController _emailController;
  TextEditingController _passwordController;

  final _emailFormKey = GlobalKey<FormState>();
  final _passwordFormKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();

    _emailController = TextEditingController();
    _passwordController = TextEditingController();
  }

  @override
  Widget build(BuildContext context) {
    return InnerDrawer(
        boxShadow: [BoxShadow()],
        key: _innerDrawerKey,
        onTapClose: true,
        colorTransitionChild: Colors.transparent,
        colorTransitionScaffold: Colors.transparent,
        duration: Duration(milliseconds: 500),
        proportionalChildArea: false,
        leftAnimationType: InnerDrawerAnimation.linear,
        leftChild: Scaffold(body: SafeArea(child: SideBar())),
        scaffold: CupertinoPageScaffold(
            child: NestedScrollView(
                headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
                  return <Widget>[
                    CupertinoSliverNavigationBar(
                      backgroundColor: AskitColors.blue,
                      leading: ElevatedButton.icon(
                          style: ButtonStyle(
                              backgroundColor: MaterialStateProperty.all<Color>(AskitColors.blue),
                              elevation: MaterialStateProperty.all<double>(0)),
                          onPressed: () => _innerDrawerKey.currentState.toggle(),
                          icon: FaIcon(FontAwesomeIcons.bars, size: 16),
                          label: Text('Menu',
                              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, fontFamily: 'Lato'))),
                      largeTitle: Text('Login', style: TextStyle(color: Colors.white, fontFamily: 'Lato')),
                    )
                  ];
                },
                body: Scaffold(
                    resizeToAvoidBottomInset: false,
                    body: ListView(children: [
                      // Email input
                      Padding(
                        padding: EdgeInsets.all(8),
                        child: Form(
                          key: _emailFormKey,
                          child: TextFormField(
                            validator: (val) => !isEmail(val) ? "Invalid email" : null,
                            controller: _emailController,
                            decoration: InputDecoration(
                              labelText: 'Email',
                              contentPadding: EdgeInsets.all(16),
                              focusedBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: AskitColors.blue, width: 0.5),
                              ),
                              enabledBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: AskitColors.gray, width: 0.5),
                              ),
                            ),
                          ),
                        ),
                      ),
                      // Password input
                      Padding(
                        padding: const EdgeInsets.all(8),
                        child: Form(
                          key: _passwordFormKey,
                          child: TextFormField(
                            validator: (val) => val.length < 8 ? "Must be at least 8 characters long" : null,
                            controller: _passwordController,
                            obscuringCharacter: '*',
                            obscureText: true,
                            decoration: InputDecoration(
                              labelText: 'Password',
                              contentPadding: EdgeInsets.all(16),
                              focusedBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: AskitColors.blue, width: 0.5),
                              ),
                              enabledBorder: OutlineInputBorder(
                                borderSide: BorderSide(color: AskitColors.gray, width: 0.5),
                              ),
                            ),
                          ),
                        ),
                      ),
                      // Post question
                      Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
                        // Continue as guest
                        CupertinoButton(
                            child: Text('Continue as guest',
                                style: TextStyle(color: AskitColors.blue, fontWeight: FontWeight.bold)),
                            onPressed: () =>
                                {Navigator.push(context, MaterialPageRoute(builder: (context) => QuestionsScreen()))}),
                        // Log in button
                        Align(
                            alignment: Alignment.centerRight,
                            child: Padding(
                                padding: EdgeInsets.only(right: 8, top: 16, bottom: 8),
                                child: CupertinoButton.filled(
                                    padding: EdgeInsets.symmetric(vertical: 8, horizontal: 32),
                                    child: Text('Login'),
                                    onPressed: () async {
                                      if (_emailFormKey.currentState.validate() &&
                                          _passwordFormKey.currentState.validate()) {
                                        String email = _emailController.text;
                                        String password = _passwordController.text;

                                        User user = await UserRestApi.findByEmail(email);

                                        if (user == null) {
                                          showDialog(
                                              barrierDismissible: false,
                                              context: context,
                                              builder: (BuildContext context) => AskitDialog('Invalid credentials',
                                                  'Your email or password are incorrect', AskitDialog.error));
                                          return;
                                        }

                                        if ('{noop}$password' != user.password) {
                                          showDialog(
                                              barrierDismissible: false,
                                              context: context,
                                              builder: (BuildContext context) => AskitDialog('Invalid credentials',
                                                  'Your email or password are incorrect', AskitDialog.error));
                                          return;
                                        }

                                        sharedPreferences = await SharedPreferences.getInstance();
                                        sharedPreferences.setString('user', json.encode(user.toJson()));

                                        Navigator.pop(context);
                                        Navigator.push(
                                            context, MaterialPageRoute(builder: (context) => QuestionsScreen()));
                                      }
                                    })))
                      ])
                    ])))));
  }
}
