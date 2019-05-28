package com.richer.cartoonapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.richer.cartoonapp.Acitivity.SubscribeActivity;
import com.richer.cartoonapp.R;

public class MeFragment extends Fragment {

    private Button btSubscribe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment,container,false);

        btSubscribe = view.findViewById(R.id.subscribe_list);

        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SubscribeActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
