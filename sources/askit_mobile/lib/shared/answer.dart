import 'dart:math' as math;

import 'package:askit_mobile/api/answer_vote_rest_api.dart';
import 'package:askit_mobile/decoration/colors.dart';
import 'package:askit_mobile/entity/answer_vote.dart';
import 'package:askit_mobile/models/answer_model.dart';
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

class Answer extends StatefulWidget {
  final AnswerModel answerModel;

  const Answer({Key key, @required this.answerModel})
      : assert(answerModel != null),
        super(key: key);

  @override
  _AnswerState createState() => _AnswerState();
}

class _AnswerState extends State<Answer> {
  SharedPreferences sharedPreferences;
  bool isLoggedIn;

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

    votes = widget.answerModel.answerStatistics.votes;

    checkForVotes();
  }

  void checkForVotes() async {
    sharedPreferences = await SharedPreferences.getInstance();
    String jsonUser = sharedPreferences.getString('user');
    if (jsonUser == null) return;

    setState(() {
      isLoggedIn = true;
    });

    if (widget.answerModel.answerVote == null) return;

    setState(() {
      if (widget.answerModel.answerVote.vote == 1) {
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
                      // Username
                      GestureDetector(
                        child: LabeledIcon(
                          icon: FaIcon(FontAwesomeIcons.solidUserCircle, color: AskitColors.blue, size: 16),
                          label: this.widget.answerModel.user.username,
                          textSize: 14,
                        ),
                        onTap: () => {
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => ProfileScreen(userId: this.widget.answerModel.user.id)))
                        },
                      ),
                      // Relative time
                      GestureDetector(
                        child: LabeledIcon(
                          icon: FaIcon(FontAwesomeIcons.solidClock, color: AskitColors.blue, size: 16),
                          label: DateFormatter.getRelativeTime(this.widget.answerModel.answer.createdDate),
                          textSize: 14,
                        ),
                        onTap: () {
                          DateFormat formatter = DateFormat('MMM d, yyyy, h:mm:ss a');
                          String beautyDate = formatter.format(this.widget.answerModel.answer.createdDate);

                          showDialog(
                              barrierDismissible: false,
                              context: context,
                              builder: (BuildContext context) =>
                                  AskitDialog('Posted on', '$beautyDate', AskitDialog.info));
                        },
                      ),
                    ],
                  ))),
          Divider(color: AskitColors.gray, height: 0),
          // Answer content
          Padding(
              padding: EdgeInsets.symmetric(horizontal: 8, vertical: 16),
              child: ConstrainedBox(
                  constraints: BoxConstraints(minWidth: double.infinity),
                  child: Html(
                      useRichText: true,
                      data: widget.answerModel.answer.htmlText,
                      imageProperties: ImageProperties(
                        height: 150,
                        width: 150,
                      )))),
          Divider(color: AskitColors.gray, height: 0),
          // Statistics
          Padding(
              padding: EdgeInsets.all(8),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
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
                                if (widget.answerModel.answerVote == null) {
                                  AnswerVote newAnswerVote = AnswerVote(
                                      vote: 1,
                                      answerId: widget.answerModel.answer.id,
                                      userId: widget.answerModel.answer.userId);
                                  AnswerVote savedAnswerVote = await AnswerVoteRestApi.save(newAnswerVote);
                                  if (savedAnswerVote == null) return;

                                  setState(() {
                                    widget.answerModel.answerVote = savedAnswerVote;
                                    upVoteIcon =
                                        FaIcon(FontAwesomeIcons.solidThumbsUp, color: AskitColors.green, size: 16);
                                    upVoteText = 'Up voted';

                                    votes = votes + 1;
                                  });
                                  return;
                                }

                                if (widget.answerModel.answerVote.vote == 1) {
                                  AnswerVote deletedAnswerVote =
                                      await AnswerVoteRestApi.deleteById(widget.answerModel.answerVote.id);
                                  if (deletedAnswerVote == null) return;

                                  setState(() {
                                    widget.answerModel.answerVote = null;

                                    upVoteIcon = FaIcon(FontAwesomeIcons.solidThumbsUp, size: 16);
                                    upVoteText = 'Up vote';

                                    votes = votes - 1;
                                  });
                                  return;
                                }

                                if (widget.answerModel.answerVote.vote == -1) {
                                  AnswerVote newAnswerVote = AnswerVote(
                                      id: widget.answerModel.answerVote.id,
                                      vote: 1,
                                      answerId: widget.answerModel.answerVote.answerId,
                                      userId: widget.answerModel.answerVote.userId);
                                  AnswerVote updatedAnswerVote = await AnswerVoteRestApi.update(newAnswerVote);
                                  if (updatedAnswerVote == null) return;

                                  setState(() {
                                    widget.answerModel.answerVote = updatedAnswerVote;

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
                                if (widget.answerModel.answerVote == null) {
                                  // Add vote
                                  AnswerVote newAnswerVote = AnswerVote(
                                      vote: -1,
                                      answerId: widget.answerModel.answer.id,
                                      userId: widget.answerModel.answer.userId);
                                  AnswerVote savedAnswerVote = await AnswerVoteRestApi.save(newAnswerVote);
                                  if (savedAnswerVote == null) return;

                                  setState(() {
                                    widget.answerModel.answerVote = savedAnswerVote;

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

                                if (widget.answerModel.answerVote.vote == 1) {
                                  // Update vote
                                  AnswerVote newAnswerVote = AnswerVote(
                                      id: widget.answerModel.answerVote.id,
                                      vote: -1,
                                      answerId: widget.answerModel.answerVote.answerId,
                                      userId: widget.answerModel.answerVote.userId);
                                  AnswerVote updatedAnswerVote = await AnswerVoteRestApi.update(newAnswerVote);
                                  if (updatedAnswerVote == null) return;

                                  setState(() {
                                    widget.answerModel.answerVote = updatedAnswerVote;

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

                                if (widget.answerModel.answerVote.vote == -1) {
                                  // Remove vote
                                  AnswerVote deletedAnswerVote =
                                      await AnswerVoteRestApi.deleteById(widget.answerModel.answerVote.id);
                                  if (deletedAnswerVote == null) return;

                                  setState(() {
                                    widget.answerModel.answerVote = null;

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
                  // Votes
                  Align(
                    alignment: Alignment.centerRight,
                    child: Wrap(
                      spacing: 16,
                      runSpacing: 8,
                      children: <Widget>[
                        // Votes
                        LabeledIcon(
                          icon: FaIcon(FontAwesomeIcons.solidThumbsUp, color: AskitColors.blue, size: 16),
                          label: '$votes votes',
                          textSize: 14,
                        ),
                      ],
                    ),
                  ),
                ],
              ))
        ],
      ),
    );
  }
}
