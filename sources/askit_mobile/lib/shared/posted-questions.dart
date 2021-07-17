import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/models/posted_questions_model.dart';
import 'package:askit_mobile/models/questions_model.dart';
import 'package:askit_mobile/routes/view_question_screen.dart';
import 'package:askit_mobile/shared/question.dart';
import 'package:askit_mobile/ui/loader.dart';
import 'package:askit_mobile/utility/date-formatter.dart';
import 'package:flutter/material.dart';

class PostedQuestions extends StatefulWidget {
  final int userId;

  const PostedQuestions({Key key, this.userId}) : super(key: key);

  @override
  _PostedQuestionsState createState() => _PostedQuestionsState();
}

class _PostedQuestionsState extends State<PostedQuestions> {
  ScrollController scrollController;
  PostedQuestionsModel postedQuestionsModel;
  Widget _body;

  @override
  void initState() {
    super.initState();

    postedQuestionsModel = PostedQuestionsModel(userId: widget.userId, clearCache: true);
    _body = Loader();

    loadData();

    scrollController = ScrollController();
    scrollController.addListener(() {
      if (scrollController.position.maxScrollExtent == scrollController.offset) {
        loadData();
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return _body;
  }

  Future<void> loadData() async {
    await postedQuestionsModel.getData();

    setState(() {
      _body = ListView.builder(
        controller: scrollController,
        itemCount: postedQuestionsModel.questionList.length + 1,
        itemBuilder: (BuildContext context, int index) {
          if (index == postedQuestionsModel.questionList.length) {
            if (!postedQuestionsModel.hasMore) {
              return Padding(padding: const EdgeInsets.all(16), child: Center(child: Text('That\'s all!')));
            }

            return Padding(
              padding: const EdgeInsets.all(16),
              child: Loader(size: 24),
            );
          }

          return Column(
                children: [
                  GestureDetector(
                    onTap: () => {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) =>
                                  ViewQuestionScreen(questionId: postedQuestionsModel.questionList[index].id)))
                    },
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Text('${postedQuestionsModel.questionList[index].subject}',
                          style: TextStyle(color: AskitColors.blue, fontWeight: FontWeight.bold)),
                    ),
                  ),
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                        '${DateFormatter.getRelativeTime(postedQuestionsModel.questionList[index].createdDate)}',
                        style: TextStyle(color: AskitColors.darkGray, fontWeight: FontWeight.w400)),
                  ),
                  Divider(color: AskitColors.gray)
                ],
              );
        },
      );
    });
  }
}
