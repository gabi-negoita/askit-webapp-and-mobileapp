import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/models/answers_model.dart';
import 'package:askit_mobile/shared/answer.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:flutter/material.dart';

class Answers extends StatefulWidget {
  final int questionId;

  const Answers({Key key, @required this.questionId})
      : assert(questionId != null),
        super(key: key);

  @override
  _AnswersState createState() => _AnswersState();
}

class _AnswersState extends State<Answers> {
  final scrollController = ScrollController();
  AnswersModel answersModel;
  Widget _body;

  @override
  void initState() {
    super.initState();

    answersModel = AnswersModel();
    _body = Loader();

    _loadData();

    scrollController.addListener(() {
      if (scrollController.position.maxScrollExtent ==
          scrollController.offset) {
        _loadData();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return _body;
  }

  Future<void> _loadData() async {
    await answersModel.getData(widget.questionId);

    setState(() {
      _body = ListView.builder(
        controller: scrollController,
        itemCount: answersModel.answers.length + 1,
        itemBuilder: (BuildContext context, int index) {
          if (index == answersModel.answers.length) {
            if (!answersModel.hasMore) {
              return Padding(
                padding: const EdgeInsets.all(16),
                child: Center(child: Text('That\'s all!')),
              );
            }

            return Padding(
              padding: const EdgeInsets.all(16),
              child: Loader(),
            );
          }

          return Answer(answerModel: answersModel.answers[index]);
        },
      );
    });
  }
}
