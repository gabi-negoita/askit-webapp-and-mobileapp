import 'answer.dart';

class AnswerWrapper {
  List<Answer> content;
  int currentPage;
  int totalItems;
  int totalPages;

  AnswerWrapper(
      {this.content, this.currentPage, this.totalItems, this.totalPages});

  factory AnswerWrapper.fromJson(Map<String, dynamic> json) {
    var list = json['content'] as List;
    List<Answer> objectList = list.map((i) => Answer.fromJson(i)).toList();

    return AnswerWrapper(
        content: objectList,
        currentPage: json['currentPage'],
        totalItems: json['totalItems'],
        totalPages: json['totalPages']);
  }
}
