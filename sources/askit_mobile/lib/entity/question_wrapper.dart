import 'package:askit_mobile/entity/question.dart';

class QuestionWrapper {
  List<Question> content;
  int currentPage;
  int totalItems;
  int totalPages;

  QuestionWrapper(
      {this.content, this.currentPage, this.totalItems, this.totalPages});

  factory QuestionWrapper.fromJson(Map<String, dynamic> json) {
    var list = json['content'] as List;
    List<Question> objectList =
        list.map((i) => Question.fromJson(i)).toList();

    return QuestionWrapper(
        content: objectList,
        currentPage: json['currentPage'],
        totalItems: json['totalItems'],
        totalPages: json['totalPages']);
  }
}