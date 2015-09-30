package com.kickstarter.ui.viewholders;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kickstarter.KSApplication;
import com.kickstarter.R;
import com.kickstarter.libs.CircleTransform;
import com.kickstarter.libs.CommentUtils;
import com.kickstarter.libs.CurrentUser;
import com.kickstarter.libs.DateTimeUtils;
import com.kickstarter.models.Comment;
import com.kickstarter.models.Project;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentViewHolder extends KsrViewHolder {
  private Project project;
  private Comment comment;

  public @Bind(R.id.avatar) ImageView avatarImageView;
  public @Bind(R.id.creator_label) TextView creatorLabel;
  public @Bind(R.id.user_label) TextView userLabelTextView;
  public @Bind(R.id.name) TextView name;
  public @Bind(R.id.post_date) TextView postDate;
  public @Bind(R.id.comment_body) TextView commentBody;
  @Inject CurrentUser currentUser;  //check if backed project

  public CommentViewHolder(final View view) {
    super(view);

    ((KSApplication) view.getContext().getApplicationContext()).component().inject(this);
    ButterKnife.bind(this, view);
  }

  public void onBind(final Object datum) {
    final Pair<Project, Comment> projectAndComment = (Pair<Project, Comment>) datum;
    project = projectAndComment.first;
    comment = projectAndComment.second;

    final Context context = view.getContext();

    creatorLabel.setVisibility(View.GONE);
    userLabelTextView.setVisibility(View.GONE);

    if (CommentUtils.isUserAuthor(comment, project.creator())) {
      creatorLabel.setVisibility(View.VISIBLE);
    } else if (CommentUtils.isUserAuthor(comment, currentUser.getUser())) {
      userLabelTextView.setVisibility(View.VISIBLE);
    }

    Picasso.with(context).load(comment.author()
      .avatar()
      .small())
      .transform(new CircleTransform())
      .into(avatarImageView);
    name.setText(comment.author().name());
    postDate.setText(DateTimeUtils.relativeDateInWords(comment.createdAt(), false, true));
    commentBody.setText(comment.body());
  }
}