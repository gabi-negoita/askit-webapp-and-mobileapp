import 'dart:math' as math;

import 'package:askit_mobile/api/question_vote_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/question_vote.dart';
import 'package:askit_mobile/models/question_model.dart';
import 'package:askit_mobile/routes/profile_screen.dart';
import 'package:askit_mobile/ui/dialog.dart';
import 'package:askit_mobile/ui/labeled_icon.dart';
import 'package:askit_mobile/utility/date-formatter.dart';
import 'package:flutter/material.dart';
import 'package:flutter_html/flutter_html.dart';
import 'package:flutter_html/image_properties.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';

class QuestionDetailed extends StatefulWidget {
  final QuestionModel questionModel;

  const QuestionDetailed({Key key, @required this.questionModel})
      : assert(questionModel != null),
        super(key: key);

  @override
  _QuestionDetailedState createState() => _QuestionDetailedState();
}

class _QuestionDetailedState extends State<QuestionDetailed> {
  SharedPreferences sharedPreferences;
  bool isLoggedIn;
  QuestionVote questionVote;

  Widget upVoteIcon;
  String upVoteText;

  Widget downVoteIcon;
  String downVoteText;

  int votes;

  @override
  void initState() {
    super.initState();

    isLoggedIn = false;

    upVoteIcon = FaIcon(FontAwesomeIcons.solidThumbsUp, size: 16);
    upVoteText = 'Up vote';

    downVoteIcon = Transform(
        alignment: Alignment.center,
        transform: Matrix4.rotationY(math.pi),
        child: FaIcon(FontAwesomeIcons.solidThumbsDown, size: 16));
    downVoteText = 'Down vote';

    votes = widget.questionModel.questionStatistics.votes;

    checkForVotes();
  }

  void checkForVotes() async {
    sharedPreferences = await SharedPreferences.getInstance();
    String jsonUser = sharedPreferences.getString('user');
    if (jsonUser == null) return;

    setState(() {
      isLoggedIn = true;
    });

    questionVote = await QuestionVoteRestApi.findByQuestionIdIdAndUserId(
        widget.questionModel.question.id, widget.questionModel.question.userId);
    if (questionVote == null) return;

    setState(() {
      if (questionVote.vote == 1) {
        upVoteIcon = FaIcon(FontAwesomeIcons.solidThumbsUp, color: AskitColors.green, size: 16);
        upVoteText = 'Up voted';
      } else {
        downVoteIcon = Transform(
            alignment: Alignment.center,
            transform: Matrix4.rotationY(math.pi),
            child: FaIcon(FontAwesomeIcons.solidThumbsDown, color: AskitColors.orange, size: 16));
        downVoteText = 'Down voted';
      }
    });
  }

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
        child: Column(children: <Widget>[
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
                          icon: FaIcon(FontAwesomeIcons.th, color: AskitColors.teal, size: 16),
                          label: this.widget.questionModel.category.title,
                          textSize: 14),
                      // Username
                      GestureDetector(
                        child: LabeledIcon(
                          icon: FaIcon(FontAwesomeIcons.solidUserCircle, color: AskitColors.blue, size: 16),
                          label: this.widget.questionModel.user.username,
                          textSize: 14,
                        ),
                        onTap: () => {
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => ProfileScreen(userId: this.widget.questionModel.user.id)))
                        },
                      ),
                      // Relative time
                      GestureDetector(
                        child: LabeledIcon(
                          icon: FaIcon(FontAwesomeIcons.solidClock, color: AskitColors.blue, size: 16),
                          label: DateFormatter.getRelativeTime(this.widget.questionModel.question.createdDate),
                          textSize: 14,
                        ),
                        onTap: () {
                          DateFormat formatter = DateFormat('MMM d, yyyy, h:mm:ss a');
                          String beautyDate = formatter.format(this.widget.questionModel.question.createdDate);

                          showDialog(
                              barrierDismissible: false,
                              context: context,
                              builder: (BuildContext context) =>
                                  AskitDialog('Posted on', '$beautyDate', AskitDialog.info));
                        },
                      ),
                    ],
                  ))),
          // Question subject
          Divider(color: AskitColors.gray, height: 0),
          Padding(
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: ConstrainedBox(
                  constraints: BoxConstraints(minWidth: double.infinity),
                  child: Text(
                    this.widget.questionModel.question.subject,
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 18, color: AskitColors.black),
                  ))),
          // Question content
          Divider(color: AskitColors.gray, height: 0),
          Padding(
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: ConstrainedBox(
                constraints: BoxConstraints(minWidth: double.infinity),
                child: Html(
                    data: widget.questionModel.question.htmlText,
                    imageProperties: ImageProperties(
                      height: 150,
                      width: 150,
                    )),
              )),
          // Statistics
          Divider(color: AskitColors.gray, height: 0),
          Padding(
              padding: EdgeInsets.all(8),
              child: Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
                isLoggedIn
                    ? Align(
                        alignment: Alignment.centerLeft,
                        child: Wrap(spacing: 16, runSpacing: 8, children: <Widget>[
                          // Up vote
                          GestureDetector(
                            child: LabeledIcon(
                              icon: upVoteIcon,
                              label: upVoteText,
                              textSize: 16,
                            ),
                            onTap: () async {
                              if (questionVote == null) {
                                QuestionVote newQuestionVote = QuestionVote(
                                    vote: 1,
                                    questionId: widget.questionModel.question.id,
                                    userId: widget.questionModel.question.userId);
                                QuestionVote savedQuestionVote = await QuestionVoteRestApi.save(newQuestionVote);
                                if (savedQuestionVote == null) return;

                                setState(() {
                                  questionVote = savedQuestionVote;
                                  print(questionVote);
                                  upVoteIcon =
                                      FaIcon(FontAwesomeIcons.solidThumbsUp, color: AskitColors.green, size: 16);
                                  upVoteText = 'Up voted';

                                  votes = votes + 1;
                                });
                                return;
                              }

                              if (questionVote.vote == 1) {
                                QuestionVote deletedQuestionVote =
                                    await QuestionVoteRestApi.deleteById(questionVote.id);
                                if (deletedQuestionVote == null) return;

                                setState(() {
                                  questionVote = null;

                                  upVoteIcon = FaIcon(FontAwesomeIcons.solidThumbsUp, size: 16);
                                  upVoteText = 'Up vote';

                                  votes = votes - 1;
                                });
                                return;
                              }

                              if (questionVote.vote == -1) {
                                QuestionVote newQuestionVote = QuestionVote(
                                    id: questionVote.id,
                                    vote: 1,
                                    questionId: questionVote.questionId,
                                    userId: questionVote.userId);
                                QuestionVote updatedQuestionVote = await QuestionVoteRestApi.update(newQuestionVote);
                                if (updatedQuestionVote == null) return;

                                setState(() {
                                  questionVote = updatedQuestionVote;

                                  upVoteIcon =
                                      FaIcon(FontAwesomeIcons.solidThumbsUp, color: AskitColors.green, size: 16);
                                  upVoteText = 'Up voted';

                                  downVoteIcon = Transform(
                                      alignment: Alignment.center,
                                      transform: Matrix4.rotationY(math.pi),
                                      child: FaIcon(FontAwesomeIcons.solidThumbsDown, size: 16));
                                  downVoteText = 'Down vote';

                                  votes = votes + 2;
                                });
                                return;
                              }
                            },
                          ),
                          // Down vote
                          GestureDetector(
                            child: LabeledIcon(
                              icon: downVoteIcon,
                              label: downVoteText,
                              textSize: 16,
                            ),
                            onTap: () async {
                              if (questionVote == null) {
                                // Add vote
                                QuestionVote newQuestionVote = QuestionVote(
                                    vote: -1,
                                    questionId: widget.questionModel.question.id,
                                    userId: widget.questionModel.question.userId);
                                QuestionVote savedQuestionVote = await QuestionVoteRestApi.save(newQuestionVote);
                                if (savedQuestionVote == null) return;

                                setState(() {
                                  questionVote = savedQuestionVote;

                                  downVoteIcon = Transform(
                                      alignment: Alignment.center,
                                      transform: Matrix4.rotationY(math.pi),
                                      child: FaIcon(FontAwesomeIcons.solidThumbsDown,
                                          color: AskitColors.orange, size: 16));
                                  downVoteText = 'Down voted';

                                  votes = votes - 1;
                                });
                                return;
                              }

                              if (questionVote.vote == 1) {
                                // Update vote
                                QuestionVote newQuestionVote = QuestionVote(
                                    id: questionVote.id,
                                    vote: -1,
                                    questionId: questionVote.questionId,
                                    userId: questionVote.userId);
                                QuestionVote updatedQuestionVote = await QuestionVoteRestApi.update(newQuestionVote);
                                if (updatedQuestionVote == null) return;

                                setState(() {
                                  questionVote = updatedQuestionVote;

                                  upVoteIcon = FaIcon(FontAwesomeIcons.solidThumbsUp, size: 16);
                                  upVoteText = 'Up vote';

                                  downVoteIcon = Transform(
                                      alignment: Alignment.center,
                                      transform: Matrix4.rotationY(math.pi),
                                      child: FaIcon(FontAwesomeIcons.solidThumbsDown,
                                          color: AskitColors.orange, size: 16));
                                  downVoteText = 'Down voted';

                                  votes = votes - 2;
                                });
                                return;
                              }

                              if (questionVote.vote == -1) {
                                // Remove vote
                                QuestionVote deletedQuestionVote =
                                    await QuestionVoteRestApi.deleteById(questionVote.id);
                                if (deletedQuestionVote == null) return;

                                setState(() {
                                  questionVote = null;

                                  downVoteIcon = Transform(
                                      alignment: Alignment.center,
                                      transform: Matrix4.rotationY(math.pi),
                                      child: FaIcon(FontAwesomeIcons.solidThumbsDown, size: 16));
                                  downVoteText = 'Down vote';

                                  votes = votes + 1;
                                });
                                return;
                              }
                            },
                          ),
                        ]))
                    : LabeledIcon(
                        icon: FaIcon(FontAwesomeIcons.infoCircle, color: AskitColors.darkYellow, size: 16),
                        label: 'You must be logged in to vote',
                        textSize: 14,
                      ),
                Align(
                    alignment: Alignment.centerRight,
                    child: Wrap(spacing: 16, runSpacing: 8, children: <Widget>[
                      // Votes
                      LabeledIcon(
                        icon: FaIcon(FontAwesomeIcons.solidThumbsUp, color: AskitColors.blue, size: 16),
                        label: '$votes votes',
                        textSize: 14,
                      )
                    ]))
              ]))
        ]));
  }
}
