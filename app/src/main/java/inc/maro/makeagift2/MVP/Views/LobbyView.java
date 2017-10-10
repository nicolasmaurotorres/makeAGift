package inc.maro.makeagift2.MVP.Views;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import inc.maro.makeagift2.Activities.EditGiftActivity;
import inc.maro.makeagift2.Activities.LobbyActivity;
import inc.maro.makeagift2.Activities.NewGiftActivity;
import inc.maro.makeagift2.Containers.GiftDisplayed;
import inc.maro.makeagift2.Helpers.ScreenSizeHelper;
import inc.maro.makeagift2.Listeners.FabOnTouchListenerListener;
import inc.maro.makeagift2.R;

public class LobbyView extends ActivityView{

    @BindView(R.id.coordinatorLayout) ViewGroup contentLobby;
    @BindView(R.id.fabNewGift) FloatingActionButton fabNewGift;

    public LobbyView(LobbyActivity activity) {
        super(activity);
        ButterKnife.bind(this,activity);
        ScreenSizeHelper.setActivity(activity);
        fabNewGift.setBackgroundTintMode(PorterDuff.Mode.LIGHTEN);
    }

    @OnClick(R.id.fabNewGift)
    public void onClickNewGift(){
        Intent i = new Intent(getActivity().getApplicationContext(), NewGiftActivity.class);
        getActivity().startActivity(i);
    }

    public void drawFab(GiftDisplayed gift) {
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) inflater.inflate(R.layout.fab_button, contentLobby, false);
        fab.setOnTouchListener(new FabOnTouchListenerListener((LobbyActivity)(getActivity()), gift));
        fab.setX(ScreenSizeHelper.getInstance().getScreenXPosition(gift.getX()));
        fab.setY(ScreenSizeHelper.getInstance().getScreenYPosition(gift.getY()));
        fab.setVisibility(View.VISIBLE);
        contentLobby.addView(fab); // lo agrego al ViewGroup que lo contiene
    }
}
