import 'dart:convert';
import 'dart:developer';

import 'package:askit_mobile/api/rest_api_properties.dart';
import 'package:askit_mobile/entity/user.dart';
import 'package:askit_mobile/entity/user_statistics.dart';
import 'package:http/http.dart' as http;

class UserRestApi {
  static Future<User> findById(int id) async {
    final path = 'api/users/' + id.toString();
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return User.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<User> findByEmail(String email) async {
    final path = 'api/users/login/' + email;
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return User.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<UserStatistics> getStatistics(int id) async {
    final path = 'api/users/' + id.toString() + '/statistics';
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return UserStatistics.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }
}
