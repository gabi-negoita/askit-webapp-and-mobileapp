class AnswerStatistics {
  final int votes;

  AnswerStatistics({this.votes});

  factory AnswerStatistics.fromJson(Map<String, dynamic> json) =>
      AnswerStatistics(votes: json['votes']);
}
