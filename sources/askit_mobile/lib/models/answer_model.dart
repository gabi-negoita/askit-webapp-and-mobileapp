import 'package:askit_mobile/api/answer_rest_api.dart';
import 'package:askit_mobile/api/answer_vote_rest_api.dart';
import 'package:askit_mobile/api/user_rest_api.dart';
import 'package:askit_mobile/entity/answer.dart';
import 'package:askit_mobile/entity/answer_statistics.dart';
import 'package:askit_mobile/entity/answer_vote.dart';
import 'package:askit_mobile/entity/user.dart';

class AnswerModel {
  Answer answer;
  AnswerStatistics answerStatistics;
  User user;
  AnswerVote answerVote;

  AnswerModel();

  Future<AnswerModel> getInstance(Answer answer) async {
    this.answer = answer;

    answerStatistics = await AnswerRestApi.getStatistics(answer.id);
    user = await UserRestApi.findById(answer.userId);
    answerVote = await AnswerVoteRestApi.findByAnswerIdAndUserId(answer.id, user.id);

    return this;
  }
}
