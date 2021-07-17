import 'dart:convert';
import 'dart:developer';

import 'package:askit_mobile/api/rest_api_properties.dart';
import 'package:askit_mobile/entity/answer_vote.dart';
import 'package:http/http.dart' as http;

class AnswerVoteRestApi {
  static Future<AnswerVote> findByAnswerIdAndUserId(int answerId, int userId) async {
    var queryParameters = {'answerId': answerId.toString(), 'userId': userId.toString()};
    final path = 'api/answer-votes';
    final response = await http.get(Uri.http(RestApiProperties.url, path, queryParameters));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return AnswerVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<AnswerVote> save(AnswerVote object) async {
    final path = 'api/answer-votes';

    http.Response response = await http.post(Uri.http(RestApiProperties.url, path),
        headers: <String, String>{'Content-Type': 'application/json; charset=UTF-8'},
        body: jsonEncode(object.toJson()));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return AnswerVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<AnswerVote> update(AnswerVote object) async {
    final path = 'api/answer-votes';

    http.Response response = await http.put(Uri.http(RestApiProperties.url, path),
        headers: <String, String>{'Content-Type': 'application/json; charset=UTF-8'},
        body: jsonEncode(object.toJson()));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return AnswerVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<AnswerVote> deleteById(int id) async {
    final path = 'api/answer-votes/$id';

    http.Response response = await http.delete(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return AnswerVote.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }
}
