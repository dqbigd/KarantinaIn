package com.example.karantinain.Main.Chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karantinain.Api.InitRetrofit;
import com.example.karantinain.Main.Home.LocationResponse;
import com.example.karantinain.R;
import com.example.karantinain.Utils.SharedPrefManager;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    EditText etMessage;
    ImageButton imgBtnSend;
    ProgressBar pbChat;

    public static final String TAG  = "ChatFragment";

    private final String URL_SOCKET = "https://nodelug.herokuapp.com/chat";
    private Boolean hasConnection = false;


    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(URL_SOCKET);
        } catch (URISyntaxException e) {}
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        etMessage = view.findViewById(R.id.etMessage);
        imgBtnSend = view.findViewById(R.id.imgBtnSend);
        pbChat = view.findViewById(R.id.pbChat);

        if(savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }

        if(!hasConnection){
            mSocket.connect();
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Socket connected!");
                        }
                    });
                }
            });

            mSocket.on("message", onNewMessage);

        }

        hasConnection = true;

        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMessage.getText().toString().trim().equals("")){
                    String msg = etMessage.getText().toString();
                    String token = SharedPrefManager.getKeyToken(getContext());
                    Log.d("TOKENNJING", token);

                    imgBtnSend.setVisibility(View.GONE);
                    pbChat.setVisibility(View.VISIBLE);

                    Call<SendMessageResponse> call = InitRetrofit.getInstance().sendMessage(token, msg);
                    call.enqueue(new Callback<SendMessageResponse>() {
                        @Override
                        public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getMessage().equals("Ok.")){
                                    pbChat.setVisibility(View.GONE);
                                    imgBtnSend.setVisibility(View.VISIBLE);
                                    etMessage.setText("");
                                }
                            }else {
                                Toast.makeText(getActivity(), "Terdapat gangguan pada server", Toast.LENGTH_SHORT).show();
                                pbChat.setVisibility(View.GONE);
                                imgBtnSend.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Mohon cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: "+t.getMessage());
                            pbChat.setVisibility(View.GONE);
                            imgBtnSend.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().equals("")){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: ");
                    Log.i(TAG, "run: " + args.length);
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    try {
                        message = data.getString("message");

                    } catch (Exception e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasConnection", hasConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}