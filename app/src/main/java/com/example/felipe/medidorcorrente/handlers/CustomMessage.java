package com.example.felipe.medidorcorrente.handlers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by desenvolvedor2 on 21/09/16.
 */

public class CustomMessage {
    BackGroundTask backGroundTask;

    public AlertDialog setIpMessage(final Context context){
        backGroundTask = new BackGroundTask();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alerta");
        final EditText input = new EditText(context);

        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);
        builder.setMessage("Coloque o ip do arduino")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Session.setIpAddress(input.getText().toString());
                        backGroundTask.GetData(context);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
