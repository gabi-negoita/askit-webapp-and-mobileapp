import 'package:askit_mobile/entity/category.dart';

class CategoryWrapper {
  List<Category> content;
  int currentPage;
  int totalItems;
  int totalPages;

  CategoryWrapper({this.content, this.currentPage, this.totalItems, this.totalPages});

  factory CategoryWrapper.fromJson(Map<String, dynamic> json) {
    var list = json['content'] as List;
    List<Category> objectList = list.map((i) => Category.fromJson(i)).toList();

    return CategoryWrapper(content: objectList, currentPage: json['currentPage'], totalItems: json['totalItems'], totalPages: json['totalPages']);
  }
}