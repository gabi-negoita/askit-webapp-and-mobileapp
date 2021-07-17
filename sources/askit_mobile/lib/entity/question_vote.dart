class QuestionVote {
  final int id;
  final int vote;
  final int questionId;
  final int userId;

  QuestionVote({this.id, this.vote, this.questionId, this.userId});

  factory QuestionVote.fromJson(Map<String, dynamic> json) =>
      QuestionVote(id: json['id'], vote: json['vote'], questionId: json['questionId'], userId: json['userId']);

  @override
  String toString() {
    return 'QuestionVote{id: $id, vote: $vote, questionId: $questionId, userId: $userId}';
  }

  Map<String, dynamic> toJson() => {
        'id': id.toString(),
        'vote': vote.toString(),
        'questionId': questionId.toString(),
        'userId': userId.toString(),
      };
}