import 'package:askit_mobile/entity/role.dart';
import 'package:intl/intl.dart';

class User {
  final int id;
  final String username;
  final String password;
  final String email;
  final DateTime dateOfBirth;
  final String description;
  final int status;
  final DateTime createdDate;
  List<Role> roles;

  User(
      {this.id,
      this.username,
      this.password,
      this.email,
      this.dateOfBirth,
      this.description,
      this.status,
      this.createdDate,
      this.roles});

  factory User.fromJson(Map<String, dynamic> json) {
    var list = json['roles'] as List;
    List<Role> objectList;
    if (list != null) objectList = list.map((i) => Role.fromJson(i)).toList();

    return User(
        id: json['id'],
        username: json['username'],
        password: json['password'],
        email: json['email'],
        dateOfBirth: DateTime.tryParse(json['dateOfBirth'] ?? ''),
        description: json['description'],
        status: json['status'],
        createdDate: DateTime.tryParse(json['createdDate']),
        roles: objectList);
  }

  Map<String, dynamic> toJson() => {
        'id': id,
        'username': username,
        'password': password,
        'email': email,
        'dateOfBirth': dateOfBirth != null ? DateFormat('yyyy-MM-dd').format(dateOfBirth) : null,
        'description': description,
        'status': status,
        'createdDate': DateFormat('yyyy-MM-ddTHH:mm:ss').format(createdDate),
        'roles': roles
      };

  @override
  String toString() {
    return 'User{id: $id, username: $username, password: $password, email: $email, dateOfBirth: $dateOfBirth, description: $description, status: $status, createdDate: $createdDate, roles: $roles}';
  }
}
