import 'package:json_annotation/json_annotation.dart';

part 'book.g.dart';

enum BookFormat {
  @JsonValue('digital')
  digital,
  @JsonValue('paperback')
  paperback,
  @JsonValue('hardcover')
  hardcover,
  @JsonValue('audiobook')
  audiobook,
}

enum BookBadge {
  @JsonValue('new')
  newRelease,
  @JsonValue('bestseller')
  bestseller,
  @JsonValue('featured')
  featured,
  @JsonValue('winner')
  winner,
  @JsonValue('nominee')
  nominee,
}

@JsonSerializable()
class Book {
  final String id;
  final String isbn;
  final String title;
  final String author;
  final String coverUrl;
  final double rating;
  final int reviewCount;
  final double price;
  final String currency;
  final BookFormat format;
  final String category;
  final String? subcategory;
  final String publisher;
  final String publishedDate;
  final int pages;
  final String language;
  final String description;
  final bool? isFree;
  final bool? isOwned;
  final BookBadge? badge;

  Book({
    required this.id,
    required this.isbn,
    required this.title,
    required this.author,
    required this.coverUrl,
    required this.rating,
    required this.reviewCount,
    required this.price,
    required this.currency,
    required this.format,
    required this.category,
    this.subcategory,
    required this.publisher,
    required this.publishedDate,
    required this.pages,
    required this.language,
    required this.description,
    this.isFree,
    this.isOwned,
    this.badge,
  });

  factory Book.fromJson(Map<String, dynamic> json) => _$BookFromJson(json);

  Map<String, dynamic> toJson() => _$BookToJson(this);

  String get formatPrice {
    if (isFree == true) return 'FREE';
    return '\$${price.toStringAsFixed(2)}';
  }

  String get formatDisplay {
    return format.name[0].toUpperCase() + format.name.substring(1);
  }
}
