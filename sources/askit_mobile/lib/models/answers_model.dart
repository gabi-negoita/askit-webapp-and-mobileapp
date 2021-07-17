import 'package:askit_mobile/api/answer_rest_api.dart';

import 'answer_model.dart';

class AnswersModel {
  List<AnswerModel> answers = [];
  int _nextPage;
  bool hasMore;

  AnswersModel() {
    _nextPage = 0;
    hasMore = true;
  }

  Future<void> getData(int questionId) async {
    if (!hasMore) return;

    var answerWrapper = await AnswerRestApi.findByQuery(
        page: _nextPage++, questionId: questionId, approved: 1);

    for (var answer in answerWrapper.content) {
      var answerModel = await AnswerModel().getInstance(answer);
      answers.add(answerModel);
    }

    if (answers.length == answerWrapper.totalItems) {
      hasMore = false;
    }

    return this;
  }
}
