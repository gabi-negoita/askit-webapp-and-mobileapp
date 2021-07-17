import 'package:askit_mobile/api/category_rest_api.dart';
import 'package:askit_mobile/api/question_rest_api.dart';
import 'package:askit_mobile/api/user_rest_api.dart';
import 'package:askit_mobile/entity/category.dart';
import 'package:askit_mobile/entity/question.dart';
import 'package:askit_mobile/entity/question_statistics.dart';
import 'package:askit_mobile/entity/user.dart';

class QuestionModel {
  Question question;
  QuestionStatistics questionStatistics;
  Category category;
  User user;

  QuestionModel();

  Future<QuestionModel> getInstance(Question question) async {
    this.question = question;

    questionStatistics = await QuestionRestApi.getStatistics(question.id);
    category = await CategoryRestApi.findById(question.categoryId);
    user = await UserRestApi.findById(question.userId);

    return this;
  }
}
