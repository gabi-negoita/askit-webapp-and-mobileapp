import 'dart:convert';
import 'dart:developer';

import 'package:askit_mobile/api/rest_api_properties.dart';
import 'package:askit_mobile/entity/category_wrapper.dart';
import 'package:askit_mobile/entity/category.dart';
import 'package:http/http.dart' as http;

class CategoryRestApi {
  static Future<Category> findById(int id) async {
    final path = 'api/categories/' + id.toString();
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return Category.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<Category> findByTitle(String title) async {
    final path = 'api/categories/title/' + title;
    final response = await http.get(Uri.http(RestApiProperties.url, path));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return Category.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }

  static Future<CategoryWrapper> findByFields(
      {int page, int size, String sort, String order, String title}) async {
    var queryParameters = {
      'page': (page == null ? '' : page.toString()),
      'size': (size == null ? '' : size.toString()),
      'sort': (sort == null ? '' : sort),
      'order': (order == null ? '' : order),
      'title': (title == null ? '' : title),
    };

    final response = await http
        .get(Uri.http(RestApiProperties.url, 'api/categories', queryParameters));

    if (response.statusCode != 200) {
      log(response.body);
      return null;
    }

    return CategoryWrapper.fromJson(jsonDecode(utf8.decode(response.bodyBytes)));
  }
}
