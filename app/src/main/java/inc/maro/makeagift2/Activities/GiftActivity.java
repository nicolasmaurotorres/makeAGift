package inc.maro.makeagift2.Activities;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.Target;
import inc.maro.makeagift2.R;
import inc.maro.makeagift2.Services.BehaviourService;
import inc.maro.makeagift2.Services.BehaviourServiceConnector;
import inc.maro.makeagift2.Services.ICallBackBinder;
import inc.maro.makeagift2.Services.Serviceable;

public class GiftActivity extends AppCompatActivity implements Serviceable
{
    private BehaviourServiceConnector behaviourServerServiceConnector = new BehaviourServiceConnector(GiftActivity.this);
    private ICallBackBinder service = null;
    private static ArrayList<Target> targetNames = new ArrayList<>();

    private boolean editedGift = false;
    private Gift possibleGift = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        connectService();
        this.setFinishOnTouchOutside(false); // para que no se vea como otra app en el selector de app abiertas
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); // el ancho y alto del dialogo
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final AutoCompleteTextView targetGiftAutoCompleteTextView =  (AutoCompleteTextView) findViewById(R.id.targetAutoCompleteView);
        final EditText descriptionGift = (EditText) findViewById(R.id.descriptionGiftEditView);
        final EditText dateGift = (EditText) findViewById(R.id.dateGiftEditView);

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        editedGift = false;

        if ( getIntent().hasExtra(Gift.SAVED_GIFT) ) // siginifica que estoy editando un elemento que esta guardado
        {
            possibleGift = (Gift) getIntent().getExtras().get(Gift.SAVED_GIFT);
            targetGiftAutoCompleteTextView.setText(possibleGift.getTarget());
            descriptionGift.setText(possibleGift.getDescription());
            dateGift.setText(possibleGift.getWhenGift());
            //todo ubicacion en el mapa
            //todo posicion x,y como manejarlo
            editedGift = true;
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder messages = new StringBuilder();
                boolean error = false;
                String targetName = targetGiftAutoCompleteTextView.getText().toString();
                String description = descriptionGift.getText().toString();
                String date = dateGift.getText().toString();

                // controlo que los campos no esten vacios
                if (!targetName.isEmpty())
                {
                    messages.append(R.string.missing_target);
                    error = true;
                }
                if (!description.isEmpty())
                {
                    messages.append(R.string.missing_description);
                    error = true;
                }
                if (!error)
                {
                    showToastMessage(messages.toString());
                }
                else
                {
                    if (editedGift)
                    {
                        boolean changedTargetName = false; // controlo si cambio el nombre del target
                        boolean changedTheData = false;   //se edito el gift, controlo si de cambio algun campo
                        if (!possibleGift.getTarget().equals(targetName))
                        {
                            possibleGift.setTarget(targetName);
                            changedTheData = true;
                            changedTargetName= true;
                        }
                        if (!possibleGift.getDescription().equals(description))
                        {
                            possibleGift.setDescription(description);
                            changedTheData = true;
                        }
                        if (!possibleGift.getWhenGift().equals(date))
                        {
                            possibleGift.setWhenToGift(date);
                            changedTheData = true;
                        }
                        if (changedTheData)
                        {
                            if (changedTargetName)
                            {
                                if (!targetNames.contains(possibleGift))
                                {

                                    service.createNewTarget(possibleGift,GiftActivity.this);// es un target nuevo
                                }
                                else
                                {
                                    possibleGift.setIdTarget(targetNames.get(targetNames.indexOf(possibleGift)).getId());    // es un target diferente pero existente, busco el id del existente
                                    Intent i = new Intent(GiftActivity.this.getApplicationContext(), LobbyActivity.class);
                                    i.putExtra(Gift.EDITED_GIFT, possibleGift);
                                    startActivity(i);
                                    service.updateTarget(possibleGift.getId(),possibleGift.getIdTarget());
                                }
                            }
                            else
                            {
                                Intent i = new Intent(GiftActivity.this.getApplicationContext(), LobbyActivity.class);
                                i.putExtra(Gift.EDITED_GIFT, possibleGift);
                                startActivity(i);
                                service.updateGift(possibleGift);
                            }
                        }
                        else
                        {
                            //no cambiaron los datos
                            onBackPressed();
                            editedGift = false; // por si las dudas
                        }
                    }
                    else
                    {
                        service.createNewGift(targetGiftAutoCompleteTextView.getText().toString(), descriptionGift.getText().toString(),dateGift.getText().toString(),"",GiftActivity.this);
                    }
                }

            }
        });
    }

    //*************************************************************************** CALLBACKS ***************************************************************************
    @Override
    public void setCallBackBinder(ICallBackBinder _service)
    {
        this.service = _service;
        service.fillTargetNames(this);
    }

    public void callBackNewGift(Gift gift)
    {
        Intent i = new Intent(GiftActivity.this.getApplicationContext(), LobbyActivity.class);
        i.putExtra(Gift.NEW_GIFT, gift);
        startActivity(i);
    }

    public void callBackFillTargetNames(ArrayList<Target> targetDb)
    {
        targetNames.clear();
        targetNames.addAll(targetDb);
        AutoCompleteTextView targetsView = (AutoCompleteTextView) findViewById(R.id.targetAutoCompleteView);
        String[] targets = new String[targetNames.size()];
        for (int i = 0; i < targetNames.size(); i++)
        {
            targets[i] = targetNames.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, targets);
        targetsView.setThreshold(1);//will start working from first character
        targetsView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        targetsView.setTextColor(Color.RED);
    }

    public void callBackNewTarget(Gift possibleGift)
    {
        Intent i = new Intent(GiftActivity.this.getApplicationContext(), LobbyActivity.class);
        i.putExtra(Gift.EDITED_GIFT, possibleGift);
        startActivity(i);
    }


    /// *************************************************************************** IMPLEMENTACIONES ***************************************************************************
    @Override
    public void connectService()
    {
        getApplicationContext().bindService(new Intent(GiftActivity.this, BehaviourService.class), behaviourServerServiceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void showToastMessage(String message)
    {
        Toast toast1 = Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.close_gift_activity));
        builder.setMessage(getResources().getString(R.string.confirm_question));

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        getApplicationContext().unbindService(behaviourServerServiceConnector);
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}