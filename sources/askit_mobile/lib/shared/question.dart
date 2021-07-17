import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/models/question_model.dart';
import 'package:askit_mobile/routes/profile_screen.dart';
import 'package:askit_mobile/routes/view_question_screen.dart';
import 'package:askit_mobile/ui/dialog.dart';
import 'package:askit_mobile/ui/labeled_icon.dart';
import 'package:askit_mobile/utility/date-formatter.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';

class Question extends StatelessWidget {
  final QuestionModel questionModel;

  const Question({Key key, @required this.questionModel})
      : assert(questionModel != null),
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
        margin: EdgeInsets.all(8),
        decoration: BoxDecoration(
            color: Colors.white,
            border: Border.all(color: AskitColors.gray, width: 0.5),
            borderRadius: BorderRadius.all(Radius.circular(4)),
            boxShadow: [
              BoxShadow(
                color: Colors.grey,
                spreadRadius: 0,
                blurRadius: 0,
                offset: Offset(0.5, 0.5),
              ),
              BoxShadow(
                color: Colors.grey,
                spreadRadius: 0,
                blurRadius: 0,
                offset: Offset(-0.5, 0.5),
              ),
            ]),
        child: InkWell(
          child: Ink(
            child: Column(
              children: <Widget>[
                // Details
                Padding(
                    padding: EdgeInsets.all(8),
                    child: Align(
                        alignment: Alignment.centerLeft,
                        child: Wrap(
                          spacing: 16,
                          runSpacing: 8,
                          children: [
                            // Category
                            LabeledIcon(
                                icon: FaIcon(FontAwesomeIcons.th,
                                    color: AskitColors.teal, size: 16),
                                label: this.questionModel.category.title,
                                textSize: 14),
                            // Username
                            GestureDetector(
                              child: LabeledIcon(
                                icon: FaIcon(FontAwesomeIcons.solidUserCircle,
                                    color: AskitColors.blue, size: 16),
                                label: this.questionModel.user.username,
                                textSize: 14,
                              ),
                              onTap: () => {
                                Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => ProfileScreen(
                                            userId:
                                                this.questionModel.user.id)))
                              },
                            ),
                            // Relative time
                            GestureDetector(
                              child: LabeledIcon(
                                icon: FaIcon(FontAwesomeIcons.solidClock,
                                    color: AskitColors.blue, size: 16),
                                label: DateFormatter.getRelativeTime(
                                    this.questionModel.question.createdDate),
                                textSize: 14,
                              ),
                              onTap: () {
                                DateFormat formatter =
                                    DateFormat('MMM d, yyyy, h:mm:ss a');
                                String beautyDate = formatter.format(
                                    this.questionModel.question.createdDate);

                                showDialog(
                                    barrierDismissible: false,
                                    context: context,
                                    builder: (BuildContext context) =>
                                        AskitDialog('Posted on', '$beautyDate',
                                            AskitDialog.info));
                              },
                            ),
                          ],
                        ))),
                Divider(color: AskitColors.gray, height: 0),
                // Question subject
                Padding(
                    padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
                    child: ConstrainedBox(
                        constraints: BoxConstraints(minWidth: double.infinity),
                        child: Text(
                          this.questionModel.question.subject,
                          style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 18,
                              color: AskitColors.black),
                        ))),
                Divider(color: AskitColors.gray, height: 0),
                // Statistics
                Padding(
                    padding: EdgeInsets.all(8),
                    child: Align(
                      alignment: Alignment.centerLeft,
                      child: Wrap(
                        spacing: 16,
                        runSpacing: 8,
                        children: <Widget>[
                          // Votes
                          LabeledIcon(
                            icon: FaIcon(FontAwesomeIcons.solidThumbsUp,
                                color: AskitColors.blue, size: 16),
                            label:
                                '${this.questionModel.questionStatistics.votes} votes',
                            textSize: 14,
                          ),
                          // Answers
                          LabeledIcon(
                            icon: FaIcon(FontAwesomeIcons.solidCheckCircle,
                                color: AskitColors.green, size: 16),
                            label:
                                '${this.questionModel.questionStatistics.answers} answers',
                            textSize: 14,
                          ),
                        ],
                      ),
                    ))
              ],
            ),
          ),
          onTap: () => Navigator.push(
              context,
              MaterialPageRoute(
                  builder: (context) => ViewQuestionScreen(
                      questionId: questionModel.question.id))),
        ));
  }
}
