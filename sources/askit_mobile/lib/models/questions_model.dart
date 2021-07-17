import 'package:askit_mobile/api/question_rest_api.dart';
import 'package:askit_mobile/models/question_model.dart';

class QuestionsModel {
  String search;
  String sortBy;
  String order;
  String categoryTitle;
  bool clearCache;

  List<QuestionModel> questions = [];
  int _nextPage;
  bool hasMore;

  QuestionsModel(
      {this.search,
      this.sortBy,
      this.order,
      this.categoryTitle,
      this.clearCache = false}) {
    _nextPage = 0;
    hasMore = true;
  }

  Future<void> getData() async {
    if (this.clearCache) {
      this.clearCache = false;
      this.hasMore = true;
      this._nextPage = 0;
      this.questions = [];
    }

    if (!hasMore) return;

    var questionWrapper = await QuestionRestApi.findByQuery(
        page: _nextPage++,
        search: this.search,
        sortBy: this.sortBy,
        order: this.order,
        categoryTitle: this.categoryTitle,
        approved: 1);

    for (var question in questionWrapper.content) {
      var questionModel = await QuestionModel().getInstance(question);
      questions.add(questionModel);
    }

    if (questions.length == questionWrapper.totalItems) {
      hasMore = false;
    }

    return this;
  }
}
