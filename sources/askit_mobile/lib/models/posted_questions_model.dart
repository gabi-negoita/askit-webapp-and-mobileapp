import 'package:askit_mobile/api/question_rest_api.dart';
import 'package:askit_mobile/entity/question.dart';
import 'package:askit_mobile/models/question_model.dart';

class PostedQuestionsModel {
  int userId;
  bool clearCache;

  List<Question> questionList = [];
  int _nextPage;
  bool hasMore;

  PostedQuestionsModel({this.userId, this.clearCache = false}) {
    _nextPage = 0;
    hasMore = true;
  }

  Future<void> getData() async {
    if (this.clearCache) {
      this.clearCache = false;
      this.hasMore = true;
      this._nextPage = 0;
      this.questionList = [];
    }

    if (!hasMore) return;

    var questionWrapper = await QuestionRestApi.findAllByFields(
        page: _nextPage++,
        size: 15,
        sort: 'createdDate',
        order: 'desc',
        userId: userId,
        approved: 1);

    questionList.addAll(questionWrapper.content);
    // for (var question in questionWrapper.content) {
    //   var questionModel = await QuestionModel().getInstance(question);
    //   questions.add(questionModel);
    // }

    if (questionList.length == questionWrapper.totalItems) {
      hasMore = false;
    }

    return this;
  }
}
