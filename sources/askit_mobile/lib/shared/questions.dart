import 'package:askit_mobile/models/questions_model.dart';
import 'package:askit_mobile/shared/question.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:flutter/material.dart';

class Questions extends StatefulWidget {
  final String search;
  final String sortBy;
  final String order;
  final String categoryTitle;
  final ScrollController scrollController;

  const Questions(
      {Key key,
      this.search,
      this.sortBy,
      this.order,
      this.categoryTitle,
      this.scrollController})
      : super(key: key);

  @override
  _QuestionsState createState() => _QuestionsState();
}

class _QuestionsState extends State<Questions> {
  ScrollController scrollController;
  QuestionsModel questionsModel;
  Widget _body;

  @override
  void initState() {
    super.initState();

    questionsModel = QuestionsModel(
        search: widget.search,
        sortBy: widget.sortBy,
        order: widget.order,
        categoryTitle: widget.categoryTitle,
        clearCache: true);
    _body = Loader();

    loadData();

    scrollController = widget.scrollController;
    scrollController.addListener(() {
      if (scrollController.position.maxScrollExtent ==
          scrollController.offset) {
        loadData();
      }
    });
  }


  @override
  Widget build(BuildContext context) {
    return _body;
  }

  Future<void> loadData() async {
    await questionsModel.getData();

    setState(() {
      _body = ListView.builder(
        // controller: scrollController,
        itemCount: questionsModel.questions.length + 1,
        itemBuilder: (BuildContext context, int index) {
          if (index == questionsModel.questions.length) {
            if (!questionsModel.hasMore) {
              return Padding(
                  padding: const EdgeInsets.all(16),
                  child: Center(child: Text('That\'s all!')));
            }

            return Padding(
              padding: const EdgeInsets.all(16),
              child: Loader(),
            );
          }

          return Question(questionModel: questionsModel.questions[index]);
        },
      );
    });
  }
}
