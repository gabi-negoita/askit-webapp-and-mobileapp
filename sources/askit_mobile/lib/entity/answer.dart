import 'package:intl/intl.dart';

class Answer {
  final int id;
  final String htmlText;
  final DateTime createdDate;
  final int questionId;
  final int userId;
  final int approved;
  final String comment;

  Answer(
      {this.id,
      this.htmlText,
      this.createdDate,
      this.questionId,
      this.userId,
      this.approved,
      this.comment});

  factory Answer.fromJson(Map<String, dynamic> json) => Answer(
      id: json['id'],
      htmlText: json['htmlText'],
      createdDate: DateTime.tryParse(json['createdDate']),
      questionId: json['questionId'],
      userId: json['userId'],
      approved: json['approved'],
      comment: json['comment']);

  Map<String, dynamic> toJson() => {
        'htmlText': htmlText,
        'createdDate': DateFormat('yyyy-MM-ddTHH:mm:ss').format(createdDate),
        'questionId': questionId.toString(),
        'userId': userId.toString(),
        'approved': approved.toString(),
        'comment': comment
      };
}
