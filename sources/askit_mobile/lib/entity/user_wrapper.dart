import 'package:askit_mobile/entity/user.dart';

class UserWrapper {
  List<User> content;
  int currentPage;
  int totalItems;
  int totalPages;

  UserWrapper({this.content, this.currentPage, this.totalItems, this.totalPages});

  factory UserWrapper.fromJson(Map<String, dynamic> json) {
    var list = json['content'] as List;
    List<User> objectList = list.map((i) => User.fromJson(i)).toList();

    return UserWrapper(content: objectList, currentPage: json['currentPage'], totalItems: json['totalItems'], totalPages: json['totalPages']);
  }
}