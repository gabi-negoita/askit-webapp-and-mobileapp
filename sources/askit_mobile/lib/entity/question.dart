import 'package:intl/intl.dart';

class Question {
  final int id;
  final String subject;
  final String htmlText;
  final DateTime createdDate;
  final int categoryId;
  final int userId;
  final int approved;
  final String comment;

  Question(
      {this.id,
      this.subject,
      this.htmlText,
      this.createdDate,
      this.categoryId,
      this.userId,
      this.approved,
      this.comment});

  factory Question.fromJson(Map<String, dynamic> json) => Question(
      id: json['id'],
      subject: json['subject'],
      htmlText: json['htmlText'],
      createdDate: DateTime.tryParse(json['createdDate']),
      categoryId: json['categoryId'],
      userId: json['userId'],
      approved: json['approved'],
      comment: json['comment']);

  Map<String, dynamic> toJson() => {
        'subject': subject,
        'htmlText': htmlText,
        'createdDate': DateFormat('yyyy-MM-ddTHH:mm:ss').format(createdDate),
        'categoryId': categoryId.toString(),
        'userId': userId.toString(),
        'approved': approved.toString(),
        'comment': comment
      };
}
