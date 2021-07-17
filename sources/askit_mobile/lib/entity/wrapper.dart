// import 'package:askit_mobile/entity/category.dart';
//
// class Wrapper<T> {
//   List<T> content;
//   int currentPage;
//   int totalItems;
//   int totalPages;
//
//   Wrapper({this.content, this.currentPage, this.totalItems, this.totalPages});
//
//   factory Wrapper.fromJson(Map<String, dynamic> json) {
//     var list = json['content'] as List;
//     List<Wrapper> objectList = list.map((i) => T.fromJson(i)).toList();
//
//     return CategoryModelWrapper(content: objectList, currentPage: json['currentPage'], totalItems: json['totalItems'], totalPages: json['totalPages']);
//   }
// }