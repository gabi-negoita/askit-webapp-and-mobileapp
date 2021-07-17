import 'dart:convert';

import 'package:askit_mobile/api/category_rest_api.dart';
import 'package:askit_mobile/api/question_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/category.dart';
import 'package:askit_mobile/entity/category_wrapper.dart';
import 'package:askit_mobile/entity/question.dart';
import 'package:askit_mobile/entity/role.dart';
import 'package:askit_mobile/entity/user.dart';
import 'package:askit_mobile/ui/dialog.dart';
import 'package:askit_mobile/ui/dropdown.dart';
import 'package:askit_mobile/ui/sidebar.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_inner_drawer/inner_drawer.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AskQuestionsScreen extends StatefulWidget {
  const AskQuestionsScreen({Key key}) : super(key: key);

  @override
  _AskQuestionsScreenState createState() => _AskQuestionsScreenState();
}

class _AskQuestionsScreenState extends State<AskQuestionsScreen> {
  SharedPreferences sharedPreferences;
  final GlobalKey<InnerDrawerState> _innerDrawerKey = GlobalKey<InnerDrawerState>();
  User loggedInUser;

  TextEditingController _titleController;
  TextEditingController _bodyController;

  List<String> _categories;
  Dropdown _categoryDropdown;

  @override
  void initState() {
    super.initState();

    checkForLoggedInUser();

    _titleController = TextEditingController();
    _bodyController = TextEditingController();

    _categories = [];
    _categoryDropdown = Dropdown(_categories, hint: '');
    populateCategoryDropdown();
  }

  void checkForLoggedInUser() async {
    sharedPreferences = await SharedPreferences.getInstance();

    String jsonUser = sharedPreferences.getString('user');
    print(jsonUser);
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
                      largeTitle: Text('Ask question', style: TextStyle(color: Colors.white, fontFamily: 'Lato')),
                    )
                  ];
                },
                body: Scaffold(
                    resizeToAvoidBottomInset: false,
                    body: ListView(children: [
                      // Title input
                      Padding(
                        padding: const EdgeInsets.all(8),
                        child: TextFormField(
                          controller: _titleController,
                          keyboardType: TextInputType.multiline,
                          maxLines: null,
                          decoration: InputDecoration(
                            labelText: 'Title',
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
                      // Category dropdown
                      _categoryDropdown,
                      // Body textarea
                      Padding(
                        padding: const EdgeInsets.all(8),
                        child: TextFormField(
                          controller: _bodyController,
                          keyboardType: TextInputType.multiline,
                          maxLines: 3,
                          decoration: InputDecoration(
                            labelText: 'Body',
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
                      // Post question
                      Align(
                          alignment: Alignment.centerRight,
                          child: Padding(
                              padding: EdgeInsets.only(right: 8, top: 16, bottom: 8),
                              child: CupertinoButton.filled(
                                  padding: EdgeInsets.symmetric(vertical: 8, horizontal: 32),
                                  onPressed: () async {
                                    String title = _titleController.text;
                                    String selectedCategory = _categoryDropdown.selected;
                                    String body = _bodyController.text;

                                    if (title.length < 15) {
                                      showDialog(
                                          barrierDismissible: false,
                                          context: context,
                                          builder: (BuildContext context) => AskitDialog('Failed',
                                              'Title must be at least 15 characters long', AskitDialog.error));
                                      return;
                                    }

                                    if (selectedCategory == null) {
                                      showDialog(
                                          barrierDismissible: false,
                                          context: context,
                                          builder: (BuildContext context) =>
                                              AskitDialog('Failed', 'Category is not selected', AskitDialog.error));
                                      return;
                                    }

                                    if (body.length < 30) {
                                      showDialog(
                                          barrierDismissible: false,
                                          context: context,
                                          builder: (BuildContext context) => AskitDialog(
                                              'Failed', 'Body must be at least 30 characters long', AskitDialog.error));
                                      return;
                                    }

                                    Category category = await CategoryRestApi.findByTitle(selectedCategory);
                                    if (category == null) {
                                      showDialog(
                                          barrierDismissible: false,
                                          context: context,
                                          builder: (BuildContext context) => AskitDialog(
                                              'Failed',
                                              'Something went wrong while processing your request.\nPlease try again.',
                                              AskitDialog.error));
                                      return;
                                    }

                                    // Create question
                                    int approved = 0;
                                    for (Role role in loggedInUser.roles) {
                                      if (role.name.toLowerCase() == 'teacher') {
                                        approved = 1;
                                        break;
                                      }
                                    }

                                    Question question = Question(
                                        subject: title,
                                        htmlText: body,
                                        createdDate: DateTime.now(),
                                        categoryId: category.id,
                                        userId: loggedInUser.id,
                                        approved: approved,
                                        comment: null);

                                    // Post question
                                    Question savedQuestion = await QuestionRestApi.save(question);
                                    if (savedQuestion == null) {
                                      showDialog(
                                          barrierDismissible: false,
                                          context: context,
                                          builder: (BuildContext context) => AskitDialog(
                                              'Failed',
                                              'Something went wrong while processing your request.\nPlease try again.',
                                              AskitDialog.error));
                                      return;
                                    }

                                    // Show success message
                                    String extra = approved == 1
                                        ? ''
                                        : 'Note that your question will become public as soon as a reviewer approves it';
                                    showDialog(
                                        barrierDismissible: false,
                                        context: context,
                                        builder: (BuildContext context) => AskitDialog(
                                            'Success',
                                            'Your question has been successfully submitted! ' + extra,
                                            AskitDialog.success));

                                    // Clear inputs
                                    setState(() {
                                      _titleController.text = '';
                                      _categoryDropdown.clearSelected();
                                      // TODO: clear dropdown selected value
                                      _bodyController.text = '';
                                    });
                                  },
                                  child: Text('Submit'))))
                    ])))));
  }

  void populateCategoryDropdown() async {
    CategoryWrapper categoryWrapper = await CategoryRestApi.findByFields(size: 999, sort: 'title');

    List<String> categories = [];
    categoryWrapper.content.forEach((element) {
      categories.add(element.title);
    });

    _categories = categories;

    setState(() {
      _categoryDropdown = Dropdown(_categories, hint: 'Select category');
    });
  }
}
