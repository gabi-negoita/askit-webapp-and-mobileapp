class QuestionStatistics {
  final int votes;
  final int answers;

  QuestionStatistics({this.votes, this.answers});

  factory QuestionStatistics.fromJson(Map<String, dynamic> json) =>
      QuestionStatistics(votes: json['votes'], answers: json['answers']);
}
