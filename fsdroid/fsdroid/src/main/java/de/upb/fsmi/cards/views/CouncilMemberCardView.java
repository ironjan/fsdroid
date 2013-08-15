package de.upb.fsmi.cards.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

import de.upb.fsmi.R;
import de.upb.fsmi.cards.entities.CouncilMember;

@EViewGroup(R.layout.card_council_member)
public class CouncilMemberCardView extends RelativeLayout {

	public CouncilMemberCardView(Context context) {
		super(context);
	}

	@ViewById
	ImageView councilCardImageView;

	@ViewById
	TextView councilCardNickName, councilCardRealName,
			councilCardCourseOfStudy, councilCardCanBeAskedAbout,
			councilCardJob;

	private CouncilMember cm;

	public void bind(CouncilMember cm) {
		this.cm = cm;
		updateViews();
	}

	private void updateViews() {
		if (cm != null) {
			councilCardRealName.setText(cm.getRealName());
			councilCardNickName.setText(cm.getNickName());
			councilCardJob.setText(cm.getJob().toString());
			councilCardCourseOfStudy.setText(cm.getCourseOfStudy());
			councilCardCanBeAskedAbout.setText(cm.getCanBeAskedAbout());
		}
	}

	@Click(R.id.councilCardMail)
	void sendMailToCM() {
		if (cm == null) {
			return;
		}
		String mail = cm.getMail();
		if (mail != null) {
			Toast.makeText(getContext(), "Sending mail to " + mail,
					Toast.LENGTH_LONG).show();
			// Intent intent = new Intent(Intent.ACTION_SEND,
			// Uri.parse("mailto:"
			// + cm.getMail()));
			// getContext().startActivity(intent);
		}
	}
}
