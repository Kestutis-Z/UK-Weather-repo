package com.haringeymobile.ukweather.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.haringeymobile.ukweather.R;

public abstract class AsyncTaskWithProgressBar<Params, Progress, Result>
		extends AsyncTask<Params, Progress, Result> {

	public Context context;
	protected ProgressDialog progressDialog;

	public AsyncTaskWithProgressBar<Params, Progress, Result> setContext(
			Context context) {
		this.context = context;
		return this;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (context == null)
			return;
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getResources().getString(
				R.string.loading_message));
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(true);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
