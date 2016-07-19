package com.edxavier.vueloseaai.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.Voice;
import android.util.Log;

import com.edxavier.vueloseaai.main.vuelos.events.FlightsEvents;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class FlighDetailAsyncTask extends AsyncTask<Context,Void, Void> {
    public AsyncTaskContract delegate;

    @Override
    protected Void doInBackground(Context... params) {
        Context ctx = params[0];
        try {
            Document doc = Jsoup.connect("http://es.delta.com/flightinfo/viewFlightStatusByOrigin?flightNumber=370&flightDate=2016-06-29").get();
            Elements rows = doc.select(".appContainer");
            //Log.e("EDER", rows.html());
            delegate.onAsynctaskFinish(rows.html());
        }catch (Exception e){
            Log.e("EDER", "THREAD Exception "+e.getMessage());
        }
        return null;
    }
}
