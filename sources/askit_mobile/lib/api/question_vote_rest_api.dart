import 'dart:convert';
import 'dart:developer';

import 'package:askit_mobile/api/rest_api_properties.dart';
import 'package:askit_mobile/entity/question_vote.dart';
import 'package:http/http.dart' as http;

class QuestionVoteRestApi {
  static Future<QuestionVote> findByQuestionIdIdAndUserId(int questionId, int userId) async {
    var queryParameters = {'questionId': questionId.toString(), 'userId': userId.toString()};
    final path = 'api/question-votes';
    final response = await http.get(Uri.http(RestApiProperties.url, path, queryParameters));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<QuestionVote> save(QuestionVote object) async {
    final path = 'api/question-votes';

    http.Response response = await http.post(Uri.http(RestApiProperties.url, path),
        headers: <String, String>{'Content-Type': 'application/json; charset=UTF-8'},
        body: jsonEncode(object.toJson()));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<QuestionVote> update(QuestionVote object) async {
    final path = 'api/question-votes';

    http.Response response = await http.put(Uri.http(RestApiProperties.url, path),
        headers: <String, String>{'Content-Type': 'application/json; charset=UTF-8'},
        body: jsonEncode(object.toJson()));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<QuestionVote> deleteById(int id) async {
    final path = 'api/question-votes/$id';

    http.Response response = await http.delete(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }
}
