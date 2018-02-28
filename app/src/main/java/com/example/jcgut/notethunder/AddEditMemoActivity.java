package com.example.jcgut.notethunder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by jcgut on 02/27/2018.
 */

public class AddEditMemoActivity extends Activity {
    private EditText txtNote;
    private Button btnSave,btnCancel;
    private MemoSerialize memoSerialize;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.memofield_layout);
        this.txtNote = findViewById(R.id.txtNote);
        this.btnSave = findViewById(R.id.btnSave);
        this.btnCancel = findViewById(R.id.btnCancel);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            memoSerialize = (MemoSerialize) bundle.get("MEMO");
            if (memoSerialize != null){
                this.txtNote.setText(memoSerialize.getText());
            }
        }

        this.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onSavedClicked();
            }
        });

        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClicked();
            }
        });
    }

    public void onSavedClicked(){
        MemoDatabaseAccess databaseAccess = MemoDatabaseAccess.getInstance(this);
        databaseAccess.open();
        if(memoSerialize == null) {
            //Add memo
            MemoSerialize tmp = new MemoSerialize();
                    tmp.setText((txtNote.getText().toString()));
            databaseAccess.save(tmp);
        } else {
            memoSerialize.setText((txtNote.getText().toString()));
            databaseAccess.update(memoSerialize);
        }
        databaseAccess.close();
        this.finish();
    }
    public void onCancelClicked(){
        this.finish();
    }
}
