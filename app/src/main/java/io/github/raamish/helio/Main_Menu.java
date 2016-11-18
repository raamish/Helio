package io.github.raamish.helio;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.support.v7.widget.Toolbar;
import android.media.AudioManager;

public class Main_Menu extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static String EXTRA_MESSAGE = "Intent Message";
    private AudioManager mAudioManager;
    private boolean mPhoneIsSilent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        txtSpeechInput = (TextView) findViewById(R.id.textView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAudioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        checkIfPhoneIsSilent();

        ImageButton camerabutton = (ImageButton) findViewById(R.id.cameraButton);
        ImageButton micbutton = (ImageButton) findViewById(R.id.microphoneButton);
//        camerabutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                AlertDialog.Builder bld = new AlertDialog.Builder(Main_Menu.this);
////                bld.setTitle("Wait!");
////                bld.setMessage("Work in Progress!");
////                bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface arg0, int arg1) {
////                        Toast.makeText(getApplicationContext(), "Thank you for your cooperation", Toast.LENGTH_LONG).show();
////                    }
////                });
////                AlertDialog ad = bld.create();
////                ad.show();
//        });


        micbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AlertDialog.Builder bld = new AlertDialog.Builder(Main_Menu.this);
//                bld.setTitle("Recording");
//                bld.setMessage("Speak Up!");
//                bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        Toast.makeText(getApplicationContext(), "Waiting for results", Toast.LENGTH_LONG).show();
//                    }
//                });
//                AlertDialog ad = bld.create();
//                ad.show();
                promptSpeechInput();
            }
        });
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.activity_main_menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void activityDescribe(View v) {
        Intent intent = new Intent(this, DescribeActivity.class);
        startActivity(intent);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech Prompt");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Error Occured Try again",Toast.LENGTH_SHORT ).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode) {
            case REQ_CODE_SPEECH_INPUT : {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    if(result.get(0).equalsIgnoreCase("silence"))
                    {
                        if (mPhoneIsSilent) {
                            //change back to normal mode
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            mPhoneIsSilent = false;
                        }
                        else {
                            //change to silent mode
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            mPhoneIsSilent = true;
                        }
                    }
                    else
                    {
                        String arr[] = result.get(0).split(" ", 2);
                        String firstWord = arr[0];
                        String secondWord = arr[1];
                        switch (firstWord)
                        {
                            case "call":
                                callPhone(secondWord);
                                break;
                            case "open":
                                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                List<ResolveInfo> packageAppsList = this.getPackageManager().queryIntentActivities(intent, 0);
                                int i;

                                int counter = 0;
                                for (ResolveInfo res : packageAppsList) {
                                    if (secondWord.equalsIgnoreCase(res.loadLabel(getPackageManager()).toString())) {
                                        break;
                                    }
                                    ++counter;
                                }
                                String finalPackageName = "";
                                for (i = 1; i < packageAppsList.size(); i++) {
                                    Object obj = packageAppsList.get(i);
                                    String temp = obj.toString().split(" ")[1];
                                    String temp2 = temp.split("/")[0];
                                    if (i == counter)
                                        finalPackageName = temp2;
                                }

                                PackageManager pm = getPackageManager();
                                try
                                {
                                    String packageName = finalPackageName;
                                    Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
                                    startActivity(launchIntent);
                                } catch (Exception e1)
                                {
                                }

                                break;
                            default:
                                txtSpeechInput.setText("Sorry couldn't catch that command. Try again");
                        }
                    }
                }
            }
        }
    }

    public void callPhone(String callNumber) {
        Intent callIntent = new Intent(this,CallActivity.class);
        callIntent.putExtra(EXTRA_MESSAGE, callNumber);
        startActivity(callIntent);
    }

    private void checkIfPhoneIsSilent() {
        int ringermode = mAudioManager.getRingerMode();
        if (ringermode == AudioManager.RINGER_MODE_SILENT) {
            mPhoneIsSilent = true;
        }
        else {
            mPhoneIsSilent = false;
        }
    }
}
