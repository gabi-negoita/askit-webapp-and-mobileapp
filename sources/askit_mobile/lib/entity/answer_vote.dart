class AnswerVote {
  final int id;
  final int vote;
  final int answerId;
  final int userId;

  AnswerVote({this.id, this.vote, this.answerId, this.userId});

  factory AnswerVote.fromJson(Map<String, dynamic> json) =>
      AnswerVote(id: json['id'], vote: json['vote'], answerId: json['answerId'], userId: json['userId']);

  @override
  String toString() {
    return 'AnswerVote{id: $id, vote: $vote, answerId: $answerId, userId: $userId}';
  }

  Map<String, dynamic> toJson() => {
        'id': id.toString(),
        'vote': vote.toString(),
        'answerId': answerId.toString(),
        'userId': userId.toString(),
      };
}
