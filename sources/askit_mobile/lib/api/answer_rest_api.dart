import 'dart:convert';
import 'dart:developer';

import 'package:askit_mobile/api/rest_api_properties.dart';
import 'package:askit_mobile/entity/answer.dart';
import 'package:askit_mobile/entity/answer_statistics.dart';
import 'package:askit_mobile/entity/answer_wrapper.dart';
import 'package:http/http.dart' as http;

class AnswerRestApi {
  static Future<Answer> findById(int id) async {
    final path = 'api/answers/' + id.toString();
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return Answer.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<AnswerWrapper> findByQuery(
      {int page,
      int size,
      String sortBy,
      String order,
      int questionId,
      int approved,
      int userId}) async {
    var queryParameters = {
      'page': (page == null ? '' : page.toString()),
      'size': (size == null ? '' : size.toString()),
      'sortBy': (sortBy == null ? '' : sortBy),
      'order': (order == null ? '' : order),
      'questionId': (questionId == null ? '' : questionId.toString()),
      'approved': (approved == null ? '' : approved.toString()),
      'userId': (userId == null ? '' : userId.toString()),
    };

    final response = await http
        .get(Uri.http(RestApiProperties.url, 'api/answers', queryParameters));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return AnswerWrapper.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<Answer> save(Answer object) async {
    final path = 'api/answers';

    http.Response response = await http.post(
        Uri.http(RestApiProperties.url, path),
        headers: <String, String>{
          'Content-Type': 'application/json; charset=UTF-8'
        },
        body: jsonEncode(object.toJson()));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return Answer.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<AnswerStatistics> getStatistics(int id) async {
    final path = 'api/answers/' + id.toString() + '/statistics';
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return AnswerStatistics.fromJson(jsonDecode(response.body));
  }
}
