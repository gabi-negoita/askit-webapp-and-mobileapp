class UserStatistics {
  final int rankingPoints;
  final int postedQuestions;
  final int postedAnswers;

  UserStatistics(
      {this.rankingPoints, this.postedQuestions, this.postedAnswers});

  factory UserStatistics.fromJson(Map<String, dynamic> json) => UserStatistics(
      rankingPoints: json['rankingPoints'],
      postedQuestions: json['postedQuestions'],
      postedAnswers: json['postedAnswers']);
}
