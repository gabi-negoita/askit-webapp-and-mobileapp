class DateFormatter {
  static const int SECOND = 1;
  static const int MINUTE = 60 * SECOND;
  static const int HOUR = 60 * MINUTE;
  static const int DAY = 24 * HOUR;
  static const int MONTH = 30 * DAY;
  static const int YEAR = 12 * MONTH;

  static String getRelativeTime(DateTime rawDate) {
    int dateInMilliseconds = rawDate.millisecondsSinceEpoch;
    int nowInMilliseconds = DateTime.now().millisecondsSinceEpoch;
    int secondsPassed = ((nowInMilliseconds - dateInMilliseconds) / 1000).floor();

    // Get seconds
    if (secondsPassed < MINUTE) return 'less than a minute ago';

    // Get within 2 minutes
    if (secondsPassed < 2 * MINUTE) return 'a minute ago';

    // Get minutes
    if (secondsPassed < HOUR) return '${(secondsPassed / MINUTE).floor()} minutes ago';

    // Get within 1 hour
    if (secondsPassed < 2 * HOUR) return 'an hour ago';

    // Get hours
    if (secondsPassed < DAY) return '${(secondsPassed / HOUR).floor()} hours ago';

    // Get within 2 days
    if (secondsPassed < 2 * DAY) return 'yesterday';

    // Get days
    if (secondsPassed < MONTH) return '${(secondsPassed / DAY).floor()} days ago';

    // Get months
    if (secondsPassed < 2 * MONTH) return 'a month ago';

    // Get months
    if (secondsPassed < YEAR) return '${(secondsPassed / MONTH).floor()} months ago';

    // Get within 2 years
    if (secondsPassed < 2 * YEAR) return 'a year ago';

    // Get years
    return '${(secondsPassed / YEAR).floor()} years ago';
  }
}
