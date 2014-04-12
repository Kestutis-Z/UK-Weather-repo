package com.haringeymobile.ukweather;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity {

	private static final int LINKS_COLOUR = R.color.blue3;

	private int linkTextColour;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		Resources res = getResources();
		linkTextColour = res.getColor(LINKS_COLOUR);

		TextView aboutTextView_1 = (TextView) findViewById(R.id.about_textview_part_1);
		final SpannableString s1 = new SpannableString(
				res.getText(R.string.about_text_part_1));
		Linkify.addLinks(s1, Linkify.EMAIL_ADDRESSES);
		displayText(aboutTextView_1, s1);

		TextView aboutTextView_2 = (TextView) findViewById(R.id.about_textview_part_2);
		final SpannableString s2 = new SpannableString(
				res.getText(R.string.about_text_part_2));
		Linkify.addLinks(s2, Linkify.WEB_URLS);
		displayText(aboutTextView_2, s2);

	}

	private void displayText(TextView aboutTextView_1, final SpannableString s1) {
		MovementMethod m1 = aboutTextView_1.getMovementMethod();
		if ((m1 == null) || !(m1 instanceof LinkMovementMethod))
			aboutTextView_1.setMovementMethod(LinkMovementMethod.getInstance());
		aboutTextView_1.setLinkTextColor(linkTextColour);
		aboutTextView_1.setText(s1);
	}

}
