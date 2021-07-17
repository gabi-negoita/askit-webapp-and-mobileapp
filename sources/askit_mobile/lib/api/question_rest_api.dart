import 'dart:convert';
import 'dart:developer';

import 'package:askit_mobile/api/rest_api_properties.dart';
import 'package:askit_mobile/entity/question_statistics.dart';
import 'package:askit_mobile/entity/question.dart';
import 'package:askit_mobile/entity/question_wrapper.dart';
import 'package:http/http.dart' as http;

class QuestionRestApi {
  static Future<Question> findById(int id) async {
    final path = 'api/questions/' + id.toString();
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return Question.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<QuestionWrapper> findAllByFields(
      {int page,
        int size,
        String sort,
        String order,
        String subject,
        String htmlText,
        int categoryId,
        int userId,
        int approved}) async {
    var queryParameters = {
      'page': (page == null ? '' : page.toString()),
      'size': (size == null ? '' : size.toString()),
      'sort': (sort == null ? '' : sort),
      'order': (order == null ? '' : order),
      'subject': (subject == null ? '' : subject),
      'htmlText': (htmlText == null ? '' : htmlText),
      'categoryId': (categoryId == null ? '' : categoryId.toString()),
      'userId': (userId == null ? '' : userId.toString()),
      'approved': (approved == null ? '' : approved.toString()),
    };

    final response = await http
        .get(Uri.http(RestApiProperties.url, 'api/questions', queryParameters));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionWrapper.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<QuestionWrapper> findByQuery(
      {int page,
      int size,
      String sortBy,
      String order,
      String categoryTitle,
      String createdDate,
      String search,
      int approved}) async {
    var queryParameters = {
      'page': (page == null ? '' : page.toString()),
      'size': (size == null ? '' : size.toString()),
      'sortBy': (sortBy == null ? '' : sortBy),
      'order': (order == null ? '' : order),
      'categoryTitle': (categoryTitle == null ? '' : categoryTitle),
      'createdDate': (createdDate == null ? '' : createdDate),
      'search': (search == null ? '' : search),
      'approved': (approved == null ? '' : approved.toString()),
    };

    final response = await http
        .get(Uri.http(RestApiProperties.url, 'api/questions/query', queryParameters));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionWrapper.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<QuestionStatistics> getStatistics(int id) async {
    final path = 'api/questions/' + id.toString() + '/statistics';
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return QuestionStatistics.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<Question> save(Question object) async {
    final path = 'api/questions';

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

    return Question.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }
}
