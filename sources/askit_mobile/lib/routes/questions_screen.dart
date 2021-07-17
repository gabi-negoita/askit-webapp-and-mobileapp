import 'package:askit_mobile/api/category_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/category_wrapper.dart';
import 'package:askit_mobile/models/questions_model.dart';
import 'package:askit_mobile/shared/question.dart';
import 'package:askit_mobile/shared/questions.dart';
import 'package:askit_mobile/ui/dropdown.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:askit_mobile/ui/sidebar.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter_inner_drawer/inner_drawer.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:shared_preferences/shared_preferences.dart';

class QuestionsScreen extends StatefulWidget {
  const QuestionsScreen({Key key}) : super(key: key);

  @override
  _QuestionsScreenState createState() => _QuestionsScreenState();
}

class _QuestionsScreenState extends State<QuestionsScreen> {
  SharedPreferences sharedPreferences;
  final GlobalKey<InnerDrawerState> _innerDrawerKey =
      GlobalKey<InnerDrawerState>();
  final scrollController = ScrollController();

  Widget _questions;

  TextEditingController _searchController;

  List<String> _sortByList;
  Dropdown _sortByDropdown;

  List<String> _categoryList;
  Dropdown _categoryDropdown;

  @override
  void initState() {
    super.initState();

    _searchController = TextEditingController();
    _sortByList = [
      'Oldest',
      'Newest',
      'Most votes',
      'Least votes',
      'Most answers',
      'Least answers',
      'Title A to Z',
      'Title Z to A'
    ];
    _sortByDropdown = Dropdown(_sortByList, hint: 'Sort by');

    _categoryList = [];
    _categoryDropdown = Dropdown(_categoryList, hint: 'Select category');
    populateCategoryDropdown();

    _questions = Loader();

    fetchData();
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
              controller: scrollController,
                headerSliverBuilder:
                    (BuildContext context, bool innerBoxIsScrolled) {
                  return <Widget>[
                    CupertinoSliverNavigationBar(
                      backgroundColor: AskitColors.blue,
                      leading: ElevatedButton.icon(
                          style: ButtonStyle(
                              backgroundColor: MaterialStateProperty.all<Color>(
                                  AskitColors.blue),
                              elevation: MaterialStateProperty.all<double>(0)),
                          onPressed: () =>
                              _innerDrawerKey.currentState.toggle(),
                          icon: FaIcon(FontAwesomeIcons.bars, size: 16),
                          label: Text('Menu',
                              style: TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                  fontFamily: 'Lato'))),
                      largeTitle: Text('Questions',
                          style: TextStyle(
                              color: Colors.white, fontFamily: 'Lato')),
                      trailing: ElevatedButton.icon(
                          style: ButtonStyle(
                              backgroundColor: MaterialStateProperty.all<Color>(
                                  AskitColors.blue),
                              elevation: MaterialStateProperty.all<double>(0)),
                          onPressed: () => displayModal(context),
                          icon: FaIcon(FontAwesomeIcons.filter, size: 16),
                          label: Text('Filter',
                              style: TextStyle(
                                  fontSize: 16,
                                  fontWeight: FontWeight.bold,
                                  fontFamily: 'Lato'))),
                    )
                  ];
                },
                body: Scaffold(
                    resizeToAvoidBottomInset: false, body: _questions))));
  }

  void populateCategoryDropdown() async {
    CategoryWrapper categoryWrapper =
        await CategoryRestApi.findByFields(size: 999, sort: 'title');

    List<String> categories = [];
    categoryWrapper.content.forEach((element) {
      categories.add(element.title);
    });

    setState(() {
      _categoryList.addAll(categories);
    });
  }

  void fetchData({String search, String sortBy, String categoryTitle}) {
    String sort;
    String order;

    switch (sortBy) {
      case 'Oldest':
        sort = 'created_date';
        break;
      case 'Newest':
        sort = 'created_date';
        order = 'desc';
        break;
      case 'Least votes':
        sort = 'votes';
        break;
      case 'Most votes':
        sort = 'votes';
        order = 'desc';
        break;
      case 'Least answers':
        sort = 'answers';
        break;
      case 'Most answers':
        sort = "answers";
        order = "desc";
        break;
      case 'Title A to Z':
        sort = 'subject';
        break;
      case 'Title Z to A':
        sort = 'subject';
        order = 'desc';
        break;
    }

    setState(() {
      _questions = Questions(
          key: UniqueKey(),
          scrollController: scrollController,
          search: search,
          sortBy: sort,
          order: order,
          categoryTitle: categoryTitle);
    });
  }

  void displayModal(context) async {
    showModalBottomSheet(
        // isScrollControlled: true,
        elevation: 0,
        context: context,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
        builder: (BuildContext context) {
          return Wrap(children: [
            // Search
            Padding(
              padding: EdgeInsets.all(8),
              child: TextFormField(
                controller: _searchController,
                decoration: InputDecoration(
                  contentPadding: EdgeInsets.symmetric(horizontal: 16),
                  border: UnderlineInputBorder(),
                  labelText: 'Search',
                  focusedBorder: OutlineInputBorder(
                    borderSide:
                        BorderSide(color: AskitColors.blue, width: 0.5),
                  ),
                  enabledBorder: OutlineInputBorder(
                    borderSide:
                        BorderSide(color: AskitColors.gray, width: 0.5),
                  ),
                ),
              ),
            ),
            // Sort by
            _sortByDropdown,
            // Select category
            _categoryDropdown,
            // Apply filters
            Align(
                alignment: Alignment.centerRight,
                child: Padding(
                    padding: EdgeInsets.only(right: 8, top: 16, bottom: 8),
                    child: CupertinoButton.filled(
                        padding:
                            EdgeInsets.symmetric(vertical: 8, horizontal: 32),
                        child: Text('Apply'),
                        onPressed: () {
                          Navigator.pop(context);
                          fetchData(
                              search: _searchController.text,
                              sortBy: _sortByDropdown.selected,
                              categoryTitle: _categoryDropdown.selected);
                        })))
          ]);
        });
  }
}
