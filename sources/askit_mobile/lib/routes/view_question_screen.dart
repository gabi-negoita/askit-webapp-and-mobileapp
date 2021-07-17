import 'dart:convert';

import 'package:askit_mobile/api/answer_rest_api.dart';
import 'package:askit_mobile/api/question_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/answer.dart';
import 'package:askit_mobile/entity/role.dart';
import 'package:askit_mobile/entity/user.dart';
import 'package:askit_mobile/models/question_model.dart';
import 'package:askit_mobile/shared/answers.dart';
import 'package:askit_mobile/shared/question_detailed.dart';
import 'package:askit_mobile/ui/dialog.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:askit_mobile/ui/sidebar.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inner_drawer/inner_drawer.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ViewQuestionScreen extends StatefulWidget {
  final int questionId;

  const ViewQuestionScreen({Key key, this.questionId}) : super(key: key);

  @override
  _ViewQuestionScreenState createState() => _ViewQuestionScreenState();
}

class _ViewQuestionScreenState extends State<ViewQuestionScreen> {
  SharedPreferences sharedPreferences;
  final GlobalKey<InnerDrawerState> _innerDrawerKey = GlobalKey<InnerDrawerState>();
  User loggedInUser;

  Widget _body;
  QuestionModel _questionModel;
  Widget _answers;

  TextEditingController _postAnswerController;

  @override
  void initState() {
    super.initState();

    loggedInUser = null;

    _body = Loader();
    _questionModel = new QuestionModel();
    _answers = Answers(questionId: widget.questionId);

    _postAnswerController = TextEditingController();

    checkForLoggedInUser();

    fetchData();
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
                        largeTitle: Text('View question', style: TextStyle(color: Colors.white, fontFamily: 'Lato')))
                  ];
                },
                body: Scaffold(resizeToAvoidBottomInset: false, body: _body))));
  }

  void fetchData() async {
    var question = await QuestionRestApi.findById(widget.questionId);
    _questionModel = await _questionModel.getInstance(question);

    // Render UI
    setState(() {
      _body = ListView(children: [
        // Question
        QuestionDetailed(questionModel: _questionModel),
        // Answers
        _totalAnswersWidget(),
        // Post your answer
        loggedInUser != null
            ? Container(
                margin: EdgeInsets.all(8),
                decoration: BoxDecoration(
                    border: Border.all(color: AskitColors.gray, width: 1),
                    borderRadius: BorderRadius.circular(4),
                    color: Colors.white,
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
                      )
                    ]),
                child: Column(children: [
                  // Label
                  Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.all(8),
                        child: Text('Post your answer',
                            style: TextStyle(
                                color: AskitColors.black,
                                fontFamily: 'Lato',
                                fontWeight: FontWeight.bold,
                                fontSize: 18)),
                      )),
                  Divider(height: 8),
                  // Input
                  Padding(
                    padding: const EdgeInsets.all(8),
                    child: TextFormField(
                      controller: _postAnswerController,
                      keyboardType: TextInputType.multiline,
                      maxLines: 3,
                      decoration: InputDecoration(
                        labelText: 'Answer',
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
                  SizedBox(height: 8),
                  // Submit button
                  Align(
                      alignment: Alignment.centerRight,
                      child: Padding(
                          padding: const EdgeInsets.all(8),
                          child: CupertinoButton.filled(
                              padding: EdgeInsets.symmetric(vertical: 8, horizontal: 32),
                              onPressed: () async {
                                if (_postAnswerController.text.length < 15) {
                                  showDialog(
                                      barrierDismissible: false,
                                      context: context,
                                      builder: (BuildContext context) => AskitDialog('Failed',
                                          'Your answer must be at least 15 characters long', AskitDialog.error));
                                  return;
                                }

                                // Create answer
                                int approved = 0;
                                for (Role role in loggedInUser.roles) {
                                  if (role.name.toLowerCase() == 'teacher') {
                                    approved = 1;
                                    break;
                                  }
                                }

                                Answer answer = Answer(
                                    htmlText: _postAnswerController.text,
                                    questionId: widget.questionId,
                                    approved: approved,
                                    userId: loggedInUser.id,
                                    comment: null,
                                    createdDate: DateTime.now());

                                // Save answer
                                Answer savedAnswer = await AnswerRestApi.save(answer);
                                if (savedAnswer == null) {
                                  showDialog(
                                      barrierDismissible: false,
                                      context: context,
                                      builder: (BuildContext context) => AskitDialog(
                                          'Failed',
                                          'Something went wrong while processing your request.\nPlease try again.',
                                          AskitDialog.error));
                                  return;
                                }

                                // Display success message
                                String extra = approved == 1
                                    ? ''
                                    : 'Note that your answer will become public as soon as a reviewer approves it';
                                showDialog(
                                    barrierDismissible: false,
                                    context: context,
                                    builder: (BuildContext context) => AskitDialog(
                                        'Success!', 'Your answer has been submitted. ' + extra, AskitDialog.success));

                                // Clear inputs
                                setState(() {
                                  _postAnswerController.text = '';
                                });
                              },
                              child: Text('Submit'))))
                ]))
            : Container(
                margin: EdgeInsets.all(8),
                decoration: BoxDecoration(
                    border: Border.all(color: AskitColors.darkOrange, width: 0.5),
                    borderRadius: BorderRadius.circular(4)),
                child: Column(children: [
                  // Label
                  Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.all(8),
                        child: Text('Not logged in',
                            style: TextStyle(
                                color: AskitColors.darkOrange,
                                fontFamily: 'Lato',
                                fontWeight: FontWeight.bold,
                                fontSize: 18)),
                      )),
                  Divider(height: 8),
                  Align(
                      alignment: Alignment.centerLeft,
                      child: Padding(
                        padding: const EdgeInsets.all(8),
                        child: Text('You must be logged in to answer this question',
                            style: TextStyle(
                                color: AskitColors.darkOrange,
                                fontFamily: 'Lato',
                                fontWeight: FontWeight.bold,
                                fontSize: 14)),
                      ))
                ]))
      ]);
    });
  }

  Widget _totalAnswersWidget() {
    return Padding(
        padding: EdgeInsets.all(8),
        child: InkWell(
            onTap: () => _displayBottomModalSheet(context),
            child: Container(
                decoration:
                    BoxDecoration(border: Border.all(color: AskitColors.blue), borderRadius: BorderRadius.circular(4)),
                child: ListTile(
                    title: Text(
                  '${_questionModel.questionStatistics.answers} Answers',
                  textAlign: TextAlign.center,
                  style: TextStyle(fontSize: 18, fontFamily: 'Lato', fontWeight: FontWeight.bold),
                )))));
  }

  void _displayBottomModalSheet(context) async {
    showModalBottomSheet(
        isScrollControlled: true,
        elevation: 0,
        context: context,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(4)),
        builder: (BuildContext context) {
          return FractionallySizedBox(
            heightFactor: 0.75,
            child: Container(child: _answers),
          );
        });
  }
}
