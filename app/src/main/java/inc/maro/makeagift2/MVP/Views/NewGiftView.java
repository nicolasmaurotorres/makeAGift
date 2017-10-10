package inc.maro.makeagift2.MVP.Views;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import inc.maro.makeagift2.OttoBus.Events.SaveNewGiftEvent;
import inc.maro.makeagift2.OttoBus.OttoBus;
import inc.maro.makeagift2.R;

public class NewGiftView extends GiftActivityView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.deleteButton) ImageButton deleteButton;

    public NewGiftView(Activity activity) {
        super(activity);
        ButterKnife.bind(this,activity);
        getActivity().setFinishOnTouchOutside(false); // para que no se vea como otra app en el selector de app abiertas
        getActivity().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT); // seteo el ancho y alto del dialogo
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        deleteButton.setVisibility(View.INVISIBLE); // por default esta en invisible
    }

    @OnClick(R.id.saveGiftButton)
    public void onClickSaveGiftButton() {
        SaveNewGiftEvent event = new SaveNewGiftEvent(getDescription(),getTarget(),getDate());
        OttoBus.getInstance().post(event);
    }
}
