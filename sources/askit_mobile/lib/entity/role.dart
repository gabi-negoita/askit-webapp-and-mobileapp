class Role {
  final int id;
  final String name;

  Role({this.id, this.name});

  factory Role.fromJson(Map<String, dynamic> json) => Role(id: json['id'], name: json['name']);

  Map<String, dynamic> toJson() => {'id': id, 'name': name};

  @override
  String toString() {
    return 'Role{id: $id, name: $name}';
  }
}