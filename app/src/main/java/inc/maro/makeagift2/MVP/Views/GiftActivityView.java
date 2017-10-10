package inc.maro.makeagift2.MVP.Views;

import android.app.Activity;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Containers.Target;
import inc.maro.makeagift2.R;



public abstract class GiftActivityView extends ActivityView {

    protected @BindView(R.id.descriptionGiftEditText) EditText description;
    protected @BindView(R.id.dateGiftEditText) EditText date;
    protected @BindView(R.id.targetAutoCompleteTextView) AutoCompleteTextView targets;
    protected @BindView(R.id.deleteButton) ImageButton deleteButton;
    protected @BindView(R.id.toolbar) Toolbar toolbar;

    public GiftActivityView(Activity activity) {
        super(activity);
        ButterKnife.bind(this,activity);
        getActivity().setFinishOnTouchOutside(false); // para que no se vea como otra app en el selector de app abiertas
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); // seteo el ancho y alto del dialogo
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

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

    protected boolean isValidDate(String s) {
        //todo implementar fecha
        return true;
    }

    protected String getDescription(){
        return description.getText().toString();
    }

    protected String getTarget(){
        return targets.getText().toString();
    }

    protected String getDate(){
        //todo parse to date or something like that
        return date.getText().toString();
    }

    @OnClick(R.id.saveGiftButton)
    public abstract void onClickSaveGiftButton();

    @OnClick(R.id.cancelGiftButton)
    public void onCancelGiftButton(){
        getActivity().onBackPressed();
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

    public void goToLobby() {
        Intent i = new Intent(getActivity().getApplicationContext(), LobbyActivity.class);
        getActivity().startActivity(i);
    }
}
