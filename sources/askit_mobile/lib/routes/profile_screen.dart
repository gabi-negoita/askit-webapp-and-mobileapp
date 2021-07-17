import 'package:askit_mobile/api/question_rest_api.dart';
import 'package:askit_mobile/api/user_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/question.dart';
import 'package:askit_mobile/entity/question_wrapper.dart';
import 'package:askit_mobile/entity/user.dart';
import 'package:askit_mobile/entity/user_statistics.dart';
import 'package:askit_mobile/models/posted_questions_model.dart';
import 'package:askit_mobile/shared/posted-questions.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:askit_mobile/ui/sidebar.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_inner_drawer/inner_drawer.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ProfileScreen extends StatefulWidget {
  final int userId;

  const ProfileScreen({Key key, this.userId}) : super(key: key);

  @override
  _ProfileScreenState createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  SharedPreferences sharedPreferences;
  final GlobalKey<InnerDrawerState> _innerDrawerKey = GlobalKey<InnerDrawerState>();

  Widget profileBannerData;
  Widget statisticsData;
  PostedQuestions postedQuestions;

  @override
  void initState() {
    super.initState();

    postedQuestions = PostedQuestions(userId: widget.userId);

    profileBannerData = Loader();
    statisticsData = Loader();

    loadData();
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
                        largeTitle: Text('Profile', style: TextStyle(color: Colors.white, fontFamily: 'Lato')))
                  ];
                },
                body: Scaffold(body: ListView(children: [
                  // Profile banner
                  Container(
                      margin: EdgeInsets.all(8),
                      decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(color: AskitColors.gray, width: 0.5),
                          borderRadius: BorderRadius.all(Radius.circular(4)),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.grey,
                              spreadRadius: 0,
                              blurRadius: 0,
                              offset: Offset(0.5, 0.5),
                            ),
                            BoxShadow(
                              color: Colors.grey,
                              spreadRadius: 0,
                              blurRadius: 0,
                              offset: Offset(-0.5, 0.5),
                            ),
                          ]),
                      child: profileBannerData),
                  // Statistics
                  Container(
                      margin: EdgeInsets.all(8),
                      decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(color: AskitColors.gray, width: 0.5),
                          borderRadius: BorderRadius.all(Radius.circular(4)),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.grey,
                              spreadRadius: 0,
                              blurRadius: 0,
                              offset: Offset(0.5, 0.5),
                            ),
                            BoxShadow(
                              color: Colors.grey,
                              spreadRadius: 0,
                              blurRadius: 0,
                              offset: Offset(-0.5, 0.5),
                            ),
                          ]),
                      child: statisticsData),
                  // Posted questions
                  Container(
                      height: 240,
                      margin: EdgeInsets.all(8),
                      decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(color: AskitColors.gray, width: 0.5),
                          borderRadius: BorderRadius.all(Radius.circular(4)),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.grey,
                              spreadRadius: 0,
                              blurRadius: 0,
                              offset: Offset(0.5, 0.5),
                            ),
                            BoxShadow(
                              color: Colors.grey,
                              spreadRadius: 0,
                              blurRadius: 0,
                              offset: Offset(-0.5, 0.5),
                            ),
                          ]),
                      child: Column(children: <Widget>[
                        Padding(
                            padding: EdgeInsets.all(8),
                            child: Align(
                                alignment: Alignment.topLeft,
                                child: Text('Questions posted',
                                    style: TextStyle(
                                        color: AskitColors.black,
                                        fontFamily: 'Lato',
                                        fontWeight: FontWeight.bold,
                                        fontSize: 18)))),
                        Divider(color: AskitColors.gray, height: 0),
                        Expanded(
                            child: Padding(padding: EdgeInsets.all(8), child: postedQuestions))
                      ])),
                ])))));
  }

  void loadData() async {
    User user = await UserRestApi.findById(widget.userId);
    if (user == null) return;

    UserStatistics statistics = await UserRestApi.getStatistics(widget.userId);
    if (statistics == null) return;

    setState(() {
      profileBannerData = Column(children: <Widget>[
        // Details
        Padding(
            padding: EdgeInsets.all(8),
            child: Align(
                alignment: Alignment.topLeft,
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Username
                    Text('${user.username}',
                        style: TextStyle(
                            color: AskitColors.black,
                            fontFamily: 'Lato',
                            fontWeight: FontWeight.bold,
                            fontSize: 24)),
                    // Age
                    user.dateOfBirth != null
                        ? Text('${DateTime.now().year - user.dateOfBirth.year} years old',
                        style: TextStyle(
                            color: AskitColors.darkGray,
                            fontFamily: 'Lato',
                            fontWeight: FontWeight.w400,
                            fontSize: 16))
                        : SizedBox()
                  ],
                ))),
        Divider(color: AskitColors.gray, height: 0),
        Padding(
            padding: EdgeInsets.all(8),
            child: Align(
                alignment: Alignment.topLeft,
                child: user.description != null
                    ? Text('${user.description}',
                    style: TextStyle(
                        color: AskitColors.black,
                        fontFamily: 'Lato',
                        fontWeight: FontWeight.bold,
                        fontSize: 16))
                    : Text('No description',
                    style: TextStyle(
                        fontSize: 16, fontStyle: FontStyle.italic, fontFamily: 'Lato', color: Colors.grey))))
      ]);
      statisticsData = Column(children: <Widget>[
        // Details
        Padding(
            padding: EdgeInsets.all(8),
            child: Align(
                alignment: Alignment.topLeft,
                child: Text('Statistics',
                    style: TextStyle(
                        color: AskitColors.black,
                        fontFamily: 'Lato',
                        fontWeight: FontWeight.bold,
                        fontSize: 18)))),
        Divider(color: AskitColors.gray, height: 0),
        Padding(
            padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            child: Align(
                alignment: Alignment.center,
                child: Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
                  Column(children: [
                    Text('${statistics.rankingPoints}',
                        style: TextStyle(color: AskitColors.purple, fontFamily: 'Lato', fontSize: 48)),
                    Text('RANKING\nPOINTS',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                            color: AskitColors.black,
                            fontFamily: 'Lato',
                            fontWeight: FontWeight.bold,
                            fontSize: 14)),
                  ]),
                  Column(children: [
                    Text('${statistics.postedQuestions}',
                        style: TextStyle(color: AskitColors.orange, fontFamily: 'Lato', fontSize: 48)),
                    Text('QUESTIONS\nPOSTED',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                            color: AskitColors.black,
                            fontFamily: 'Lato',
                            fontWeight: FontWeight.bold,
                            fontSize: 14)),
                  ]),
                  Column(children: [
                    Text('${statistics.postedAnswers}',
                        style: TextStyle(color: AskitColors.blue, fontFamily: 'Lato', fontSize: 48)),
                    Text('ANSWERS\nPOSTED',
                        textAlign: TextAlign.center,
                        style: TextStyle(
                            color: AskitColors.black,
                            fontFamily: 'Lato',
                            fontWeight: FontWeight.bold,
                            fontSize: 14)),
                  ]),
                ])))
      ]);
    });
  }
}
