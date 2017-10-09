package inc.maro.makeagift2.MVP.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import inc.maro.makeagift2.Activities.GiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.Containers.Target;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.OttoBus.Events.DeleteGiftEvent;
import inc.maro.makeagift2.OttoBus.Events.SaveEditedGift;
import inc.maro.makeagift2.OttoBus.Events.SaveNewGiftEvent;
import inc.maro.makeagift2.OttoBus.OttoBus;
import inc.maro.makeagift2.R;

/**
 * Created by hIT on 4/10/2017.
 */

public class GiftView extends ActivityView {

    @BindView(R.id.descriptionGiftEditText) EditText description;
    @BindView(R.id.dateGiftEditText) EditText date;
    @BindView(R.id.targetAutoCompleteTextView) AutoCompleteTextView targets;
    @BindView(R.id.deleteButton) ImageButton deleteButton;
    @BindView(R.id.toolbar) Toolbar toolbar;
    private Gift showedGift = null;

    public GiftView(GiftActivity activity) {
        super(activity);
        ButterKnife.bind(this,activity);
        getActivity().setFinishOnTouchOutside(false); // para que no se vea como otra app en el selector de app abiertas
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); // seteo el ancho y alto del dialogo
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        deleteButton.setVisibility(View.INVISIBLE); // por default esta en invisible
    }

    public void showToastMessage(String message) {
        Toast toast1 = Toast.makeText(getActivity().getApplicationContext(),message, Toast.LENGTH_SHORT);
        toast1.show();
    }

    public String validateDataGiftAndReturnErrors(){
        String toReturn = "";
        if (!targets.getText().toString().isEmpty()){
            toReturn.concat(String.valueOf(R.string.missing_target));
        }
        if (!description.getText().toString().isEmpty()){
            toReturn.concat(String.valueOf(R.string.missing_description));
        }

        if (!isValidDate(date.getText().toString())){
            toReturn.concat(String.valueOf(R.string.error_data_date));
        }

        return toReturn;
    }

    private boolean isValidDate(String s) {
        //todo implementar fecha
        return true;
    }

    private String getDescription(){
        return description.getText().toString();
    }

    private String getTarget(){
        return targets.getText().toString();
    }

    private String getDate(){
        //todo parse to date or something like that
        return date.getText().toString();
    }

    @OnClick(R.id.saveGiftButton)
    public void onClickSaveGiftButton(){
        if (showedGift != null) {
            // estoy guardando un regalo ya existente
            SaveEditedGift savedEditedGift = new SaveEditedGift(getDescription(),getTarget(),getDate());
            OttoBus.getInstance().post(savedEditedGift);
        } else {
            //estoy guardando un regalo nuevo
            SaveNewGiftEvent event = new SaveNewGiftEvent();
            event.setDescription(getDescription());
            event.setTarget(getTarget());
            event.setDate(getDate());
            OttoBus.getInstance().post(event);
        }
    }

    @OnClick(R.id.cancelGiftButton)
    public void onCancelGiftButton(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.deleteButton)
    public void onDeleteGiftButton(){
        if (showedGift != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getActivity().getString(R.string.delete_gift));
            builder.setMessage(getActivity().getString(R.string.confirm_delete_gift));

            builder.setPositiveButton(getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    OttoBus.getInstance().post(new DeleteGiftEvent());
                    goToLobby();
                }
            });

            builder.setNegativeButton(getActivity().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public void setGift(Gift data) {
        showedGift = data;
        if (showedGift != null) {
            description.setText(showedGift.getDescription());
            targets.setText(showedGift.getTarget());
            date.setText(showedGift.getDate());
            deleteButton.setVisibility(View.VISIBLE); //lo hago visible x que es un regalo editado
        }
    }

    public void goToLobby() {
        Intent i = new Intent(getActivity().getApplicationContext(), LobbyActivity.class);
        getActivity().startActivity(i);
    }

    public void setTargets(ArrayList<Target> serverTargets) {
        String[] targetsArr = new String[serverTargets.size()];
        for (int i = 0; i < serverTargets.size(); i++){
            targetsArr[i] = serverTargets.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, targetsArr);
        targets.setThreshold(1);//will start working from first character
        targets.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        targets.setTextColor(Color.RED);
    }
}
