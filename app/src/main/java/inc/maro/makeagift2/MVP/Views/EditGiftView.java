package inc.maro.makeagift2.MVP.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

import butterknife.OnClick;

import inc.maro.makeagift2.Activities.EditGiftActivity;
import inc.maro.makeagift2.Containers.Gift;
import inc.maro.makeagift2.OttoBus.Events.DeleteGiftEvent;
import inc.maro.makeagift2.OttoBus.Events.SaveEditedGift;
import inc.maro.makeagift2.OttoBus.OttoBus;
import inc.maro.makeagift2.R;

public class EditGiftView extends GiftActivityView {

    private Gift showedGift = null;

    public EditGiftView(EditGiftActivity activity) {
        super(activity);
        deleteButton.setVisibility(View.VISIBLE); // por default esta en invisible
    }

    @OnClick(R.id.saveGiftButton)
    public void onClickSaveGiftButton(){
        SaveEditedGift savedEditedGift = new SaveEditedGift(getDescription(),getTarget(),getDate());
        OttoBus.getInstance().post(savedEditedGift);
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
        } else {
            Log.e("ERROR","EN LA BASE DE DATOS NO SE ENCONTRO EL ID DEL REGALO");
        }
    }
}
